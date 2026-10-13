package org.example.biblioapp.model;

import jakarta.persistence.*;
import java.util.List;
import org.example.biblioapp.model.Usuario;
import org.example.biblioapp.model.Libro;
import org.example.biblioapp.model.Prestamo;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empresaId;

    private String nombre;
    private String direccion;
    private String telefono;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Libro> libros;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Prestamo> prestamos;

    // Constructor vacío
    public Empresa() {}

    // Getters y Setters
    public int getEmpresaId() { return empresaId; }
    public void setEmpresaId(int empresaId) { this.empresaId = empresaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
