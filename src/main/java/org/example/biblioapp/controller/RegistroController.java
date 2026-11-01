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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
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
        model.addAttribute("esPerfil", false); // No es perfil, es registro nuevo
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

    //Modificación de perfil personal
    @GetMapping("/usuarios/perfil")
    public String verPerfil(Model model, Principal principal) {
        String email = principal.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));

        usuario.setPassword(""); // Vaciamos la contraseña por seguridad
        model.addAttribute("usuario", usuario);
        model.addAttribute("esPerfil", true); // Activamos la bandera para reutilizar el formulario
        return "usuarios/form";
    }

    @PostMapping("/usuarios/perfil")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuarioForm, Principal principal, RedirectAttributes redirectAttributes) {
        String emailActual = principal.getName();
        Usuario usuarioBD = usuarioRepository.findByEmail(emailActual)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Comprobamos si intenta cambiar el correo
        if (usuarioForm.getEmail() != null && !usuarioForm.getEmail().equals(usuarioBD.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Por motivos de seguridad, no puedes cambiar tu correo electrónico desde el perfil personal. Si necesitas modificarlo, contacta con un administrador.");
            return "redirect:/usuarios/perfil";
        }

        // Actualizamos solo los datos permitidos
        usuarioBD.setNombre(usuarioForm.getNombre());
        usuarioBD.setApellidos(usuarioForm.getApellidos());
        usuarioBD.setTelefono(usuarioForm.getTelefono());

        // Si escribió una contraseña nueva, la encriptamos; si no, dejamos la que tenía
        if (usuarioForm.getPassword() != null && !usuarioForm.getPassword().trim().isEmpty()) {
            usuarioBD.setPassword(passwordEncoder.encode(usuarioForm.getPassword()));
        }

        // El rol NUNCA se toca aquí por seguridad
        usuarioRepository.save(usuarioBD);
        return "redirect:/?actualizado";
    }
}
