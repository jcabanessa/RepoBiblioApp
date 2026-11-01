package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Mostrar el formulario de registro público
    @GetMapping("/usuarios/new")
    public String mostrarFormularioRegistro(Model model) {
        Usuario usuario = new Usuario();
        usuario.setFechaRegistro(new Date());
        model.addAttribute("usuario", usuario);
        return "usuarios/form"; // Puedes reutilizar el formulario o crear uno específico
    }

    // Procesar el registro público (Forzando rol USER)
    @PostMapping("/usuarios/registro")
    public String registrarUsuarioPublico(@ModelAttribute("usuario") Usuario usuario) {
        // Forzamos siempre el rol USER para registros públicos
        usuario.setRol("USER");

        // Encriptamos la contraseña obligatoriamente
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        usuarioRepository.save(usuario);
        return "redirect:/login?registrado";
    }
}
