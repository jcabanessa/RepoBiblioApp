package org.example.biblioapp.controller;

import jakarta.validation.Valid;
import org.example.biblioapp.dto.PrestamoForm;
import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public PrestamoController(PrestamoRepository prestamoRepository,
                              UsuarioRepository usuarioRepository,
                              LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    @GetMapping("/prestamos")
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoRepository.findAll());
        return "prestamos/lista";
    }

    @GetMapping("/admin/prestamos/new")
    public String nuevoPrestamo(Model model) {
        PrestamoForm form = new PrestamoForm();
        form.setFechaPrestamo(LocalDate.now());
        form.setEstado("ACTIVO");
        prepararFormulario(model, form);
        return "prestamos/form";
    }

    @GetMapping("/admin/prestamos/edit/{id}")
    public String editarPrestamo(@PathVariable Integer id, Model model) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de préstamo inválido: " + id));

        PrestamoForm form = new PrestamoForm();
        form.setPrestamoId(prestamo.getPrestamoId());
        form.setUsuarioId(prestamo.getUsuario().getUsuarioId());
        form.setLibroId(prestamo.getLibro().getLibroId());
        form.setFechaPrestamo(prestamo.getFechaPrestamo());
        form.setFechaDevolucion(prestamo.getFechaDevolucion());
        form.setEstado(prestamo.getEstado());
        prepararFormulario(model, form);
        return "prestamos/form";
    }

    @PostMapping("/admin/prestamos/save")
    public String guardarPrestamo(@Valid @ModelAttribute("prestamoForm") PrestamoForm form,
                                  BindingResult result,
                                  Model model) {
        if (form.getFechaPrestamo() != null && form.getFechaDevolucion() != null
                && form.getFechaDevolucion().isBefore(form.getFechaPrestamo())) {
            result.rejectValue("fechaDevolucion", "fecha.invalid", "La devolución no puede ser anterior al préstamo");
        }

        Usuario usuario = null;
        Libro libro = null;
        if (form.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(form.getUsuarioId()).orElse(null);
            if (usuario == null) {
                result.rejectValue("usuarioId", "usuario.invalid", "El usuario seleccionado no existe");
            }
        }
        if (form.getLibroId() != null) {
            libro = libroRepository.findById(form.getLibroId()).orElse(null);
            if (libro == null) {
                result.rejectValue("libroId", "libro.invalid", "El libro seleccionado no existe");
            }
        }

        if (result.hasErrors()) {
            prepararFormulario(model, form);
            return "prestamos/form";
        }

        Prestamo prestamo = form.getPrestamoId() == null
                ? new Prestamo()
                : prestamoRepository.findById(form.getPrestamoId())
                    .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(form.getFechaPrestamo());
        prestamo.setFechaDevolucion(form.getFechaDevolucion());
        prestamo.setEstado(form.getEstado() == null || form.getEstado().isBlank() ? "ACTIVO" : form.getEstado());
        prestamoRepository.save(prestamo);

        return "redirect:/prestamos?saved";
    }

    @PostMapping("/admin/prestamos/delete/{id}")
    public String eliminarPrestamo(@PathVariable Integer id) {
        prestamoRepository.deleteById(id);
        return "redirect:/prestamos?deleted";
    }

    private void prepararFormulario(Model model, PrestamoForm form) {
        model.addAttribute("prestamoForm", form);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        model.addAttribute("estados", new String[]{"ACTIVO", "DEVUELTO", "ATRASADO"});
    }
}
