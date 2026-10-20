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

import java.util.Date;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoRepository.findAll());
        return "prestamos/lista";
    }

    @GetMapping("/new")
    public String mostrarFormularioCreacion(Model model) {
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(new Date());
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @PostMapping
    public String guardarPrestamo(@Valid @ModelAttribute("prestamo") Prestamo prestamo,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            // Si hay errores, recargamos los desplegables y devolvemos al formulario
            model.addAttribute("usuarios", usuarioRepository.findAll());
            model.addAttribute("libros", libroRepository.findAll());
            return "prestamos/form";
        }
        if (prestamo.getPrestamoId() != null && prestamo.getPrestamoId() > 0) {
            // Si estamos editando, mantenemos la fecha de préstamo original
            Prestamo prestamoExistente = prestamoRepository.findById(prestamo.getPrestamoId()).orElse(null);
            if (prestamoExistente != null) {
                prestamo.setFechaPrestamo(prestamoExistente.getFechaPrestamo());
            }
        } else {
            // Si es nuevo, asignamos la fecha actual
            prestamo.setFechaPrestamo(new Date());
        }
        prestamoRepository.save(prestamo);
        return "redirect:/prestamos";
    }

    @GetMapping("/edit/{id}")
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

    @GetMapping("/delete/{id}")
    public String eliminarPrestamo(@PathVariable Integer id) {
        prestamoRepository.deleteById(id);
        return "redirect:/prestamos";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
