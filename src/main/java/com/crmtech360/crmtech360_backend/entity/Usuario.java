package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    // Relación con Empleado (Un Usuario está asociado a un Empleado)
    // La FK id_empleado es UNIQUE en la tabla Usuarios.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado", unique = true)
    // ON DELETE SET NULL es manejado por la BD. JPA lo tratará como opcional.
    private Empleado empleado;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena; // Se almacenará hasheada

    @Column(name = "rol_usuario", nullable = false, length = 20)
    private String rolUsuario; // 'Administrador', 'Gerente', 'Operario', 'Ventas'

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Usuario() {
    }

    public Usuario(Empleado empleado, String nombreUsuario, String contrasena, String rolUsuario) {
        this.empleado = empleado;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena; // La contraseña debe ser hasheada antes de persistir
        this.rolUsuario = rolUsuario;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // equals y hashCode (basado en nombreUsuario si está disponible, sino idUsuario)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        if (nombreUsuario != null ? !nombreUsuario.equals(usuario.nombreUsuario) : usuario.nombreUsuario != null) {
            if (nombreUsuario == null || usuario.nombreUsuario == null) {
                return idUsuario != null && idUsuario.equals(usuario.idUsuario);
            }
            return false;
        }
        if (nombreUsuario == null && usuario.nombreUsuario == null) {
            return idUsuario != null && idUsuario.equals(usuario.idUsuario);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = nombreUsuario != null ? nombreUsuario.hashCode() : 0;
        if (nombreUsuario == null) {
            result = 31 * result + (idUsuario != null ? idUsuario.hashCode() : 0);
        }
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", empleadoId=" + (empleado != null ? empleado.getIdEmpleado() : "null") +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", rolUsuario='" + rolUsuario + '\'' +
                '}';
    }
}