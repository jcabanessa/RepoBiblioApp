package org.example.biblioapp.service;

import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // declaramos el repositorio como inmutable

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usamos el email como identificador de acceso
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        //extrae los roles dinámicos que el usuario tiene realmente en la bbdd

        var authorities = usuario.getRoles().stream()
                .map(rol -> {

                    // pasamos a mayusculas

                    String nombreRol = rol.getNombre().toUpperCase();

                    //si en la base de datos no tiene el prefijo, se lo añadimos

                    if (!nombreRol.startsWith("ROLE_")){
                        nombreRol = "ROLE_" + nombreRol;
                    }

                    return new SimpleGrantedAuthority(nombreRol);
                })


                .collect(Collectors.toList());

        //System.out.print(">>>>>PERMISOS CARGADOS PARA " + email + ": " + authorities);


        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.isEnabled(),
                true,true,true,
                authorities
        );
    }
}
