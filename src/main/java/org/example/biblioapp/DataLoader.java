package org.example.biblioapp;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(LibroRepository libroRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Solo inserta si la base de datos está completamente vacía
            if (libroRepository.count() == 0) {
                Libro l1 = new Libro();
                l1.setTitulo("Don Quijote de la Mancha");
                l1.setAutor("Miguel de Cervantes");
                l1.setIsbn("978-8424115531");
                l1.setCantidad(5);

                Libro l2 = new Libro();
                l2.setTitulo("Cien años de soledad");
                l2.setAutor("Gabriel García Márquez");
                l2.setIsbn("978-8439732471");
                l2.setCantidad(3);

                Libro l3 = new Libro();
                l3.setTitulo("1984");
                l3.setAutor("George Orwell");
                l3.setIsbn("978-8499890944");
                l3.setCantidad(4);

                libroRepository.saveAll(List.of(l1, l2, l3));
                System.out.println(">>> Datos iniciales de libros cargados correctamente.");
            }

            /*if (usuarioRepository.count() == 0) {
                Usuario u1 = new Usuario();
                u1.setNombre("Ana");
                u1.setApellidos("García");
                u1.setEmail("ana.garcia@email.com");
                u1.setTelefono("600123456");
                u1.setPassword("ana123");
                u1.setFechaRegistro(new Date());

                usuarioRepository.save(u1);
                System.out.println(">>> Datos iniciales de usuarios cargados correctamente.");
            }*/
        };
    }
}