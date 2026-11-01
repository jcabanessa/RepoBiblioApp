package org.example.biblioapp.controller;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@Controller
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/prestamos")
    public String listarPrestamos(Model model, Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN") || a.getAuthority().contains("ADMIN"));

        if (isAdmin) {
            // Si es ADMIN, ve todos los préstamos de la biblioteca
            model.addAttribute("prestamos", prestamoRepository.findAll());
        } else {
            // Si es USER, ve únicamente sus propios préstamos basándonos en su email (principal.getName())
            String email = principal.getName();
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
            if (usuario != null) {
                model.addAttribute("prestamos", prestamoRepository.findByUsuario(usuario));
            } else {
                model.addAttribute("prestamos", java.util.Collections.emptyList());
            }
        }
        return "prestamos/lista";
    }

    @GetMapping("/admin/prestamos/new")
    public String mostrarFormularioCreacion(Model model) {
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        prestamo.setEstado("Activo");
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @PostMapping("/admin/prestamos")
    public String guardarPrestamo(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            // Si hay errores, recargamos los desplegables y devolvemos al formulario
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("libros", libroRepository.findAll());
            return "prestamos/form";
        }

        // 1. Recuperar el usuario y libro reales de la BD
        if (prestamo.getUsuario() != null && prestamo.getUsuario().getUsuarioId() != null) {
            Usuario usuarioReal = usuarioRepository.findById(prestamo.getUsuario().getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            prestamo.setUsuario(usuarioReal);
        }

        if (prestamo.getLibro() != null && prestamo.getLibro().getLibroId() != null) {
            Libro libroReal = libroRepository.findById(prestamo.getLibro().getLibroId())
                    .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
            prestamo.setLibro(libroReal);
        }

        // 2. LÓGICA DE INVENTARIO Y STOCK
        if (prestamo.getPrestamoId() != null && prestamo.getPrestamoId() > 0) {
            // ESTAMOS EDITANDO
            Prestamo prestamoExistente = prestamoRepository.findById(prestamo.getPrestamoId()).orElse(null);
            if (prestamoExistente != null) {
                prestamo.setFechaPrestamo(prestamoExistente.getFechaPrestamo());
                // DETECCIÓN DE CAMBIO DE ESTADO A "Devuelto"
                // Si antes estaba Activo (o cualquier otro) y ahora se marca como Devuelto
                if (!"Devuelto".equalsIgnoreCase(prestamoExistente.getEstado())
                        && "Devuelto".equalsIgnoreCase(prestamo.getEstado())) {

                    Libro libro = prestamo.getLibro();
                    libro.setCantidad(libro.getCantidad() + 1); // Devolvemos el ejemplar al stock
                    libroRepository.save(libro);
                }
            }
        } else {
            // ES UN PRÉSTAMO NUEVO
            Libro libro = prestamo.getLibro();

            // Validar que hay stock disponible antes de prestar
            if (libro.getCantidad() <= 0) {
                result.rejectValue("libro", "error.libro", "No hay ejemplares disponibles de este libro.");
                model.addAttribute("usuarios", usuarioRepository.findAll());
                model.addAttribute("libros", libroRepository.findAll());
                return "prestamos/form";
            }

            // Descontamos 1 al stock del libro
            libro.setCantidad(libro.getCantidad() - 1);
            libroRepository.save(libro);

            prestamo.setFechaPrestamo(new Date());
            if (prestamo.getEstado() == null || prestamo.getEstado().isEmpty()) {
                prestamo.setEstado("Activo");
            }
        }

        prestamoRepository.save(prestamo);
        return "redirect:/prestamos";
    }

    @GetMapping("/admin/prestamos/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id,Model model) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de préstamo inválido: " + id));

        // Nos aseguramos de que no sean nulos para evitar fallos de renderizado en el select
        if (prestamo.getUsuario() == null) {
            prestamo.setUsuario(new Usuario());
        }
        if (prestamo.getLibro() == null) {
            prestamo.setLibro(new Libro());
        }
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @GetMapping("/admin/prestamos/delete/{id}")
    public String eliminarPrestamo(@PathVariable Integer id) {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);

        if (prestamo != null && prestamo.getLibro() != null) {
            // Al eliminar el préstamo (o registrar su devolución), devolvemos el ejemplar al stock
            Libro libro = prestamo.getLibro();
            libro.setCantidad(libro.getCantidad() + 1);
            libroRepository.save(libro);

            prestamoRepository.deleteById(id);
        }

        return "redirect:/prestamos";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
