package org.example.biblioapp.controller;

import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @PostMapping
    public String guardarPrestamo(@ModelAttribute("prestamo") Prestamo prestamo) {
        prestamoRepository.save(prestamo);
        return "redirect:/prestamos";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de préstamo inválido: " + id));
        model.addAttribute("prestamo", prestamo);
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("libros", libroRepository.findAll());
        return "prestamos/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarPrestamo(@PathVariable int id) {
        prestamoRepository.deleteById(id);
        return "redirect:/prestamos";
    }
}
