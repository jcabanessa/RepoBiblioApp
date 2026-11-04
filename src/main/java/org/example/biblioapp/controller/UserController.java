package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public UserController(UsuarioRepository usuarioRepository, PrestamoRepository prestamoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @GetMapping("/user")
    public String areaUsuario(Authentication authentication, Model model) {
        Usuario usuario = obtenerUsuario(authentication);
        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamos", prestamoRepository.findByUsuarioEmailIgnoreCaseOrderByFechaPrestamoDesc(usuario.getEmail()));
        return "user/dashboard";
    }

    @GetMapping("/user/prestamos")
    public String misPrestamos(Authentication authentication, Model model) {
        Usuario usuario = obtenerUsuario(authentication);
        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamos", prestamoRepository.findByUsuarioEmailIgnoreCaseOrderByFechaPrestamoDesc(usuario.getEmail()));
        return "user/prestamos";
    }

    private Usuario obtenerUsuario(Authentication authentication) {
        return usuarioRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("El usuario autenticado no existe en la base de datos"));
    }
}
