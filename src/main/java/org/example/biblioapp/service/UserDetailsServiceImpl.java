package org.example.biblioapp.service;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usamos el email como identificador de acceso
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Obtenemos el rol del usuario (por defecto USER si es nulo)
        String rol = (usuario.getRol() != null && !usuario.getRol().isEmpty()) ? usuario.getRol() : "USER";

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(rol)
                .build();
    }
}
