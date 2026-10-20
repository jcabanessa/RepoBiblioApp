package org.example.biblioapp.controller;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
            // Recuperamos el usuario original para mantener su fecha de registro intacta
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getUsuarioId()).orElse(null);
            if (usuarioExistente != null) {
                usuario.setFechaRegistro(usuarioExistente.getFechaRegistro());
            }
        }
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario invalido" + id));
        model.addAttribute("usuario", usuario);
        return "usuarios/form";
    }

    @GetMapping("/delete/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }
}
