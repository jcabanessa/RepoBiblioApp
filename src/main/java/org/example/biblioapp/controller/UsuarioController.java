package org.example.biblioapp.controller;

import jakarta.validation.Valid;
import org.example.biblioapp.dto.UsuarioAdminForm;
import org.example.biblioapp.model.Rol;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PrestamoRepository prestamoRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista";
    }

    @GetMapping("/admin/usuarios/new")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuarioForm", new UsuarioAdminForm());
        model.addAttribute("roles", Rol.values());
        return "usuarios/form";
    }

    @GetMapping("/admin/usuarios/edit/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario inválido: " + id));

        UsuarioAdminForm form = new UsuarioAdminForm();
        form.setUsuarioId(usuario.getUsuarioId());
        form.setNombre(usuario.getNombre());
        form.setApellidos(usuario.getApellidos());
        form.setEmail(usuario.getEmail());
        form.setTelefono(usuario.getTelefono());
        form.setRol(usuario.getRol());

        model.addAttribute("usuarioForm", form);
        model.addAttribute("roles", Rol.values());
        return "usuarios/form";
    }

    @PostMapping("/admin/usuarios/save")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioForm") UsuarioAdminForm form,
                                 BindingResult result,
                                 Model model,
                                 Authentication authentication) {
        String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();
        Optional<Usuario> porEmail = usuarioRepository.findByEmailIgnoreCase(email);
        if (porEmail.isPresent() && !porEmail.get().getUsuarioId().equals(form.getUsuarioId())) {
            result.rejectValue("email", "email.exists", "Ya existe un usuario con este correo");
        }
        if (form.getUsuarioId() == null && (form.getPassword() == null || form.getPassword().length() < 8)) {
            result.rejectValue("password", "password.required", "Para un usuario nuevo indica una contraseña de al menos 8 caracteres");
        }
        if (form.getPassword() != null && !form.getPassword().isBlank() && form.getPassword().length() < 8) {
            result.rejectValue("password", "password.short", "La contraseña debe tener al menos 8 caracteres");
        }

        if (result.hasErrors()) {
            model.addAttribute("roles", Rol.values());
            return "usuarios/form";
        }

        Usuario usuario;
        boolean esUsuarioActual = false;
        if (form.getUsuarioId() == null) {
            usuario = new Usuario();
            usuario.setFechaRegistro(LocalDate.now());
        } else {
            usuario = usuarioRepository.findById(form.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            esUsuarioActual = authentication != null && authentication.getName().equalsIgnoreCase(usuario.getEmail());
        }

        usuario.setNombre(form.getNombre().trim());
        usuario.setApellidos(form.getApellidos().trim());
        usuario.setEmail(email);
        usuario.setTelefono(form.getTelefono());

        if (!esUsuarioActual || usuario.getUsuarioId() == null) {
            usuario.setRol(form.getRol() == null ? Rol.USER : form.getRol());
        }

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios?saved";
    }

    @PostMapping("/admin/usuarios/delete/{id}")
    public String eliminarUsuario(@PathVariable Integer id, Authentication authentication) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (authentication != null && authentication.getName().equalsIgnoreCase(usuario.getEmail())) {
            return "redirect:/usuarios?deleteError=self";
        }
        if (prestamoRepository.existsByUsuarioUsuarioId(id)) {
            return "redirect:/usuarios?deleteError=prestamos";
        }

        usuarioRepository.delete(usuario);
        return "redirect:/usuarios?deleted";
    }
}
