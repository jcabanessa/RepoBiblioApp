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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

import java.util.Date;
import java.util.Calendar;

@Controller

public class PrestamoController {

    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    //inyectamos los 3 repositorios

    public PrestamoController(PrestamoRepository prestamoRepository,
                              LibroRepository libroRepository,
                              UsuarioRepository usuarioRepository){
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping ("/admin/prestamos")
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoRepository.findAll());
        return "prestamos/lista";
    }

    @GetMapping("/admin/prestamos/new")
    public String mostrarFormularioCreacion(Model model) {
        Prestamo prestamo = new Prestamo();

        //cargamos las fechas por defecto antes de cargar la pantalla

        java.sql.Date hoy = new java.sql.Date(System.currentTimeMillis()); //usamos java.sql.date para forzar el formato YYYY-MM-DD
        prestamo.setFechaPrestamo(hoy);

        Calendar cal = Calendar.getInstance();
        cal.setTime(hoy);
        cal.add(Calendar.DAY_OF_MONTH, 15);
        java.sql.Date devolucion = new java.sql.Date(cal.getTimeInMillis());

        //asignado a fechaDevolucion
        prestamo.setFechaDevolucion(devolucion); //15 días

        prestamo.setEstado("Activo");
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @PostMapping ("/admin/prestamos")
    public String guardarPrestamo(@ModelAttribute("prestamo") Prestamo prestamo) {

        // Si SÍ hay ID (estamos editando), Spring Boot ya ha metido en el objeto 'prestamo'
        // exactamente las fechas modificadas en el formulario web.

        prestamoRepository.save(prestamo);
        return "redirect:/admin/prestamos";
    }

    @GetMapping("/prestamos/mis-prestamos")
    public String verMisPrestamos(Model model, Principal principal) {
        // 1. Identificamos quién está conectado
        String emailUsuario = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);

        // 2. Buscamos sus préstamos y los inyectamos en la variable "prestamos" del HTML
        if (usuario != null) {
            model.addAttribute("prestamos", prestamoRepository.findByUsuario(usuario));
        }

        // 3. Devolvemos la vista (asumiendo que guardamos el HTML en templates/usuarios/)
        return "usuarios/lista_mis_prestamos";
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
        prestamoRepository.deleteById(id);
        return "redirect:/admin/prestamos";
    }



    @GetMapping("/prestamos/solicitar/{libroId}")
    public String solicitarPrestamo(@PathVariable Integer libroId, Principal  principal) {
        //obtener mail de usuario logueado a través de Spring Security
        String emailUusuario = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(emailUusuario).orElse(null);

        //buscar libro seleccionado
        Libro libro = libroRepository.findById(libroId).orElse(null);

        //si usuario existe y hay stock del libro creamos prestamo

        if (usuario != null && libro != null && libro.getCantidad() > 0) {
            Prestamo prestamo = new Prestamo();

            //fechas
            Date hoy = new Date();
            prestamo.setFechaPrestamo(hoy);

            //devolucion
            Calendar cal = Calendar.getInstance();
            cal.setTime(hoy);
            cal.add(Calendar.DAY_OF_MONTH, 15);
            prestamo.setFechaDevolucion(cal.getTime());


            prestamo.setUsuario(usuario);
            prestamo.setLibro(libro);
            prestamo.setEstado("Activo");
            prestamoRepository.save(prestamo);

            //restar 1 al stock del libro
            libro.setCantidad(libro.getCantidad() - 1);
            libroRepository.save(libro);
        }
        return "redirect:/catalogo";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
