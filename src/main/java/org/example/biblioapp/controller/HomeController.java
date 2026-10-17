package org.example.biblioapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String paginaInicio() {
        return "index"; // Esto busca el archivo index.html en src/main/resources/templates/
    }
}
