package org.example.biblioapp.controller;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class CatalogoController {

    private final LibroRepository libroRepository;

    public CatalogoController(LibroRepository libroRepository) {

        this.libroRepository = libroRepository;
    }

    //esta es la ruta común que configuramos en SecurityConfig


@GetMapping("/catalogo")

public String verCatalogo(Model model) {

    //obtenemos todos los libros
    model.addAttribute("libros", libroRepository.findAll());

    // reutilizamos la misma vista de lista

    return "libros/lista";
    }
}
