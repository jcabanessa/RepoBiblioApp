package org.example.biblioapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prestamoId;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaPrestamo;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaDevolucion;

    @PrePersist
    public void prePersist() {
        if (this.fechaPrestamo == null) {
            this.fechaPrestamo = new Date();
        }
    }

    // VALIDACIÓN: Comprueba que la fecha de devolución sea posterior a la de préstamo
    @AssertTrue(message = "La fecha de devolución debe ser posterior a la fecha de préstamo")
    public boolean isFechaDevolucionValida() {
        if (fechaPrestamo == null || fechaDevolucion == null) {
            return true; // Si alguna está vacía, dejamos que otras validaciones actúen
        }
        return fechaDevolucion.after(fechaPrestamo);
    }

    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    public Prestamo() {}

    // Getters y Setters
    public Integer getPrestamoId() { return prestamoId; }
    public void setPrestamoId(Integer prestamoId) { this.prestamoId = prestamoId; }
    public Date getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Date fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
}
