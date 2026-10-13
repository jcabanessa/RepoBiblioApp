package org.example.biblioapp.repository;

import org.example.biblioapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Opcional: Spring Data JPA te permite crear métodos personalizados solo escribiendo su nombre
    List<Usuario> findByEmpresaEmpresaId(int empresaId);
}
