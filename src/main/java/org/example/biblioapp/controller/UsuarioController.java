package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/new")
    public String mostrarFormularioCreacion(Model model) {
        Usuario usuario = new Usuario();
        usuario.setFechaRegistro(new Date());
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    @PostMapping
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        if (usuario.getUsuarioId() != null && usuario.getUsuarioId() > 0) {
            // Estamos editando: recuperamos el usuario original
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getUsuarioId()).orElse(null);
            if (usuarioExistente != null) {
                usuario.setFechaRegistro(usuarioExistente.getFechaRegistro());

                // Si el campo contraseña viene vacío, mantenemos la contraseña anterior
                if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                    usuario.setPassword(usuarioExistente.getPassword());
                } else {
                    // Si introdujo una nueva contraseña, la encriptamos
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                }
            }
        } else {
            // Es un usuario nuevo: encriptamos obligatoriamente la contraseña
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario invalido" + id));
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }

    /*@PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        // Encriptamos la contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }*/
}
