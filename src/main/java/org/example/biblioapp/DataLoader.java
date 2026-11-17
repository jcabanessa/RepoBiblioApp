package org.example.biblioapp;

import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.model.Rol;
import org.example.biblioapp.repository.LibroRepository;
import org.example.biblioapp.repository.RolRepository;
import org.example.biblioapp.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    //inyectamos los repositorios necesarios

    public DataLoader(LibroRepository libroRepository,
                      RolRepository rolRepository,
                      UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder) {
        this.libroRepository = libroRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

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

        // Inicializar roles si no existen

        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol("ROLE_ADMIN")));

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseGet(() -> rolRepository.save(new Rol("ROLE_USER")));

        //Crear usuario Administrador inicial si no existe

        if (usuarioRepository.findByEmail("admin@biblioapp.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("administrador");
            admin.setApellidos("Sistema");
            admin.setEmail("admin@biblioapp.com");
            admin.setTelefono("635251484");

            // pwd se guarda de forma segura

            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setFechaRegistro(new Date());
            admin.addRol(rolAdmin);
            admin.addRol(rolUser);
            usuarioRepository.save(admin);
            System.out.println(">>> Usuario administrador generado correctamente.");
        }else{
            System.out.println(">>> El administrador ya existe en la BBDD, saltando creación .");
        }

        //crear usuario lector estándar si no existe

        if (usuarioRepository.findByEmail("user@biblioapp.com").isEmpty()) {
            Usuario user = new Usuario();
            user.setNombre("user");
            user.setApellidos("Biblioteca");
            user.setEmail("user@biblioapp.com");
            user.setTelefono("635251484");

            //la contraseña se guarda cifrada de forma segura
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEnabled(true);
            user.setFechaRegistro(new Date());
            user.addRol(rolUser);
            usuarioRepository.save(user);
            System.out.println(">>> Usuario user generado correctamente.");
        }
    }
}
