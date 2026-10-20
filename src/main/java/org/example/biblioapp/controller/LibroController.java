package org.example.biblioapp.controller;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "libros/lista";
    }

    @GetMapping("/new")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros/form";
    }

    @PostMapping
    public String guardarLibro(@ModelAttribute("libro") Libro libro) {
        libroRepository.save(libro);
        return "redirect:/libros";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de libro inválido: " + id));
        model.addAttribute("libro", libro);
        return "libros/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarLibro(@PathVariable Integer id) {
        libroRepository.deleteById(id);
        return "redirect:/libros";
    }
}
