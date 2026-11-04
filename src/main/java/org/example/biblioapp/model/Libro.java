package org.example.biblioapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer libroId;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 180, message = "El título no puede superar 180 caracteres")
    @Column(nullable = false, length = 180)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 140, message = "El autor no puede superar 140 caracteres")
    @Column(nullable = false, length = 140)
    private String autor;

    @Size(max = 32, message = "El ISBN no puede superar 32 caracteres")
    @Column(length = 32)
    private String isbn;

    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private int cantidad;

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamos = new ArrayList<>();

    public Libro() {
    }

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
