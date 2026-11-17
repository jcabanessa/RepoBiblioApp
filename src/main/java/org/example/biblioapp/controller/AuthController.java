package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.model.Rol;
import org.example.biblioapp.repository.RolRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;

@Controller

public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

       //muestra la página de login personalizado

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; //devuelve la vista templates/login.html
    }

     //ruta para pagina de inicio pública
     @GetMapping("/registro")

     public String mostrarFormularioRegistro(Model model) {
         Usuario usuario = new Usuario();
         model.addAttribute("usuario", usuario);
         return "usuarios/registro_publico";
     }



    @PostMapping("/registro")
    public String procesarRegistroPublico(@ModelAttribute("usuario") Usuario usuario) {
        // Encriptar el password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Activa la cuenta para que el usuario pueda hacer login
        usuario.setEnabled(true);

        // Usamos java.sql.Date para evitar el warning de Hibernate en consola
        usuario.setFechaRegistro(new java.sql.Date(System.currentTimeMillis()));

        // Asigna rol USER por defecto
        Rol rolUser = rolRepository.findByNombre("ROLE_USER").orElse(null);
        if (rolUser != null) {
            usuario.addRol(rolUser);
        }

        usuarioRepository.save(usuario);

        // Redirige al login con mensaje de éxito
        return "redirect:/login?registrado=true";
    }
}

