package org.example.biblioapp.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usuarioId;

    private String nombre;
    private String apellidos;

    //email único utilizado como username/identificador de autenticación
    @Column(nullable = false, unique = true)
    private String email;
    private String telefono;

    //almacenará la contraseña cifrada con BCrypt (longitud mínima recomendada 60)
    @Column(nullable = false, length = 255)
    private String password;

    //estado del usuario exigido por Spring Security
    @Column(nullable = false)
    private boolean enabled = true;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="fecha_registro")

    private Date fechaRegistro;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="usuarios_roles",
            joinColumns = @JoinColumn(name="usuario_id"),
            inverseJoinColumns = @JoinColumn(name= "id_rol")
    )

    private Set<Rol> roles = new HashSet<>();

    //el usuario no solo hace login. tambien solicita prestamos

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Prestamo> prestamos = new ArrayList<>();

    public Usuario() {}

    @PrePersist
    public void prePersist() {

        if (this.fechaRegistro == null) {
            this.fechaRegistro = new Date();
        }
    }


    // Getters y Setters

    public void addRol (Rol rol){
        this.roles.add(rol);
    }

    public void removeRol (Rol rol){
        this.roles.remove(rol);
    }

    public Integer getUsuarioId() {
        return usuarioId; }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId; }

    public String getNombre() {
        return nombre; }

    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getApellidos() {
        return apellidos; }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos; }

    public String getEmail() {
        return email; }

    public void setEmail(String email) {
        this.email = email; }

    public String getTelefono() {
        return telefono; }

    public void setTelefono(String telefono) {
        this.telefono = telefono; }

    public String getPassword() {
        return password; }

    public void setPassword(String password) {
        this.password = password; }

    public Date getFechaRegistro() {
        return fechaRegistro; }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro; }


    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled; }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled; }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }
    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }
}
