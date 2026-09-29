package org.example.biblioapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    // Endpoint GET en http://localhost:8080/hello
    @GetMapping("/hello")
    public String helloWorld() {
        return "¡Hola Mundo desde Spring Boot!";
    }
}
