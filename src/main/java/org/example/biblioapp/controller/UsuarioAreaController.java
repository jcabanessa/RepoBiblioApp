package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UsuarioAreaController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    //Ruta : /user/mis-prestamos

    @GetMapping("/mis-prestamos")
    public String misPrestamos(Model model, Principal principal) {
        // principal.getName() nos da el email del usuario logueado
        String emailUsuarioLogueado = principal.getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogueado).orElse(null);

        if (usuario != null) {
            model.addAttribute("prestamos", usuario.getPrestamos());
        }

        return "usuarios/lista_mis_prestamos.html"; //crear vista simple
    }
}
