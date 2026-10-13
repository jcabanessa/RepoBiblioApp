package org.example.biblioapp.repository;

import org.example.biblioapp.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    // Buscar préstamos activos de un usuario específico
    List<Prestamo> findByUsuarioUsuarioIdAndEstado(int usuarioId, String estado);
}
