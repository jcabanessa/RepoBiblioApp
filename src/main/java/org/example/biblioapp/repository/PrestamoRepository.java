package org.example.biblioapp.repository;

import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    // Método para filtrar préstamos por usuario
    List<Prestamo> findByUsuario(Usuario usuario);
}
