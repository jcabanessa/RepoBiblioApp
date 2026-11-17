package org.example.biblioapp.repository;

import org.example.biblioapp.model.Prestamo;
import org.example.biblioapp.model.Usuario;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    List<Prestamo> findByUsuario(Usuario usuario);
}
