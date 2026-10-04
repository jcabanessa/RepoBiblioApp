package org.example.biblioapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @GetMapping("/hello")
    public String helloWorld() {
        return "¡Hola Mundo desde Spring Boot!";
    }
}