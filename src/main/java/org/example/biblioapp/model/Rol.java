package org.example.biblioapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name="rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Long idRol;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre; // "ROL_ADMIN" , "ROL_USER"

    public Rol() {
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    //GETTERS Y SETTERS

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol rol)) return false;
        return Objects.equals(idRol, rol.idRol) && Objects.equals(nombre, rol.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRol, nombre);
    }
}