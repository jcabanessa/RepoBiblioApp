package org.example.biblioapp;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.model.Rol;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.PrestamoRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(LibroRepository libroRepository,
                                   UsuarioRepository usuarioRepository,
                                   PrestamoRepository prestamoRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (libroRepository.count() == 0) {
                Libro l1 = libro("Don Quijote de la Mancha", "Miguel de Cervantes", "978-8424115531", 5);
                Libro l2 = libro("Cien años de soledad", "Gabriel García Márquez", "978-8439732471", 3);
                Libro l3 = libro("1984", "George Orwell", "978-8499890944", 4);
                libroRepository.saveAll(List.of(l1, l2, l3));
            }

            Usuario admin = usuarioRepository.findByEmailIgnoreCase("admin@biblioteca.local").orElseGet(() -> {
                Usuario u = usuario("Admin", "Biblioteca", "admin@biblioteca.local", "600000001",
                        "Admin123!", Rol.ADMIN, passwordEncoder);
                return usuarioRepository.save(u);
            });

            Usuario demo = usuarioRepository.findByEmailIgnoreCase("usuario@biblioteca.local").orElseGet(() -> {
                Usuario u = usuario("Usuario", "Demo", "usuario@biblioteca.local", "600000002",
                        "Usuario123!", Rol.USER, passwordEncoder);
                return usuarioRepository.save(u);
            });

            // Compatibilidad con datos antiguos del P2: completa seguridad si faltase.
            for (Usuario u : usuarioRepository.findAll()) {
                boolean cambiado = false;
                if (u.getRol() == null) {
                    u.setRol(Rol.USER);
                    cambiado = true;
                }
                if (u.getPassword() == null || u.getPassword().isBlank()) {
                    u.setPassword(passwordEncoder.encode("Cambiar123!"));
                    cambiado = true;
                }
                if (u.getApellidos() == null || u.getApellidos().isBlank()) {
                    u.setApellidos("Sin apellidos");
                    cambiado = true;
                }
                if (cambiado) {
                    usuarioRepository.save(u);
                }
            }

            if (prestamoRepository.count() == 0 && !libroRepository.findAll().isEmpty()) {
                Prestamo p = new Prestamo();
                p.setUsuario(demo);
                p.setLibro(libroRepository.findAll().getFirst());
                p.setFechaPrestamo(LocalDate.now());
                p.setFechaDevolucion(LocalDate.now().plusDays(14));
                p.setEstado("ACTIVO");
                prestamoRepository.save(p);
            }
        };
    }

    private Libro libro(String titulo, String autor, String isbn, int cantidad) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setIsbn(isbn);
        libro.setCantidad(cantidad);
        return libro;
    }

    private Usuario usuario(String nombre, String apellidos, String email, String telefono,
                            String password, Rol rol, PasswordEncoder encoder) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setPassword(encoder.encode(password));
        usuario.setRol(rol);
        return usuario;
    }
}
