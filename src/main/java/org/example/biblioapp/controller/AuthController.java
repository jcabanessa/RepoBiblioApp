package org.example.biblioapp.controller;

import jakarta.validation.Valid;
import org.example.biblioapp.dto.RegistroForm;
import org.example.biblioapp.model.Rol;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registro(Model model) {
        model.addAttribute("registroForm", new RegistroForm());
        return "register";
    }

    @PostMapping("/register")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult result) {
        String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();

        if (form.getPassword() == null || !form.getPassword().equals(form.getConfirmarPassword())) {
            result.rejectValue("confirmarPassword", "password.mismatch", "Las contraseñas no coinciden");
        }
        if (!email.isBlank() && usuarioRepository.existsByEmailIgnoreCase(email)) {
            result.rejectValue("email", "email.exists", "Ya existe una cuenta con este correo electrónico");
        }
        if (result.hasErrors()) {
            return "register";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(form.getNombre().trim());
        usuario.setApellidos(form.getApellidos().trim());
        usuario.setEmail(email);
        usuario.setTelefono(form.getTelefono());
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        usuario.setRol(Rol.USER);
        usuarioRepository.save(usuario);

        return "redirect:/login?registered";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}
