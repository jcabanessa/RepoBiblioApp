package org.example.biblioapp.controller;

import jakarta.validation.Valid;
import org.example.biblioapp.model.Libro;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.PrestamoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class LibroController {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroController(LibroRepository libroRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @GetMapping("/libros")
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroRepository.findAll());
        return "libros/lista";
    }

    @GetMapping("/admin/libros/new")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros/form";
    }

    @GetMapping("/admin/libros/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de libro inválido: " + id));
        model.addAttribute("libro", libro);
        return "libros/form";
    }

    @PostMapping("/admin/libros/save")
    public String guardarLibro(@Valid @ModelAttribute("libro") Libro libro,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "libros/form";
        }
        libroRepository.save(libro);
        return "redirect:/libros?saved";
    }

    @PostMapping("/admin/libros/delete/{id}")
    public String eliminarLibro(@PathVariable Integer id) {
        if (prestamoRepository.existsByLibroLibroId(id)) {
            return "redirect:/libros?deleteError=prestamos";
        }
        libroRepository.deleteById(id);
        return "redirect:/libros?deleted";
    }
}
