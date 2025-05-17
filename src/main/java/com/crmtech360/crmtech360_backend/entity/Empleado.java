package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true, length = 20)
    private String numeroDocumento;

    @Column(name = "nombre_empleado", nullable = false, length = 255)
    private String nombreEmpleado;

    @Column(name = "cargo_empleado", length = 100)
    private String cargoEmpleado;

    @Column(name = "area_empleado", length = 50)
    private String areaEmpleado;

    @Column(name = "salario_empleado", precision = 10, scale = 2)
    private BigDecimal salarioEmpleado;

    @Column(name = "fecha_contratacion_empleado")
    private LocalDate fechaContratacionEmpleado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relación con Usuarios (un empleado puede ser un usuario)
    @OneToOne(mappedBy = "empleado", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Usuario usuario;

    // Relación con TareasProduccion (un empleado puede tener muchas tareas)
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY) // No CascadeType.ALL aquí, las tareas no se borran con el empleado
    private Set<TareaProduccion> tareasProduccion;


    // Constructores
    public Empleado() {
    }

    public Empleado(String tipoDocumento, String numeroDocumento, String nombreEmpleado, String cargoEmpleado, String areaEmpleado, BigDecimal salarioEmpleado, LocalDate fechaContratacionEmpleado) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreEmpleado = nombreEmpleado;
        this.cargoEmpleado = cargoEmpleado;
        this.areaEmpleado = areaEmpleado;
        this.salarioEmpleado = salarioEmpleado;
        this.fechaContratacionEmpleado = fechaContratacionEmpleado;
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
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getCargoEmpleado() {
        return cargoEmpleado;
    }

    public void setCargoEmpleado(String cargoEmpleado) {
        this.cargoEmpleado = cargoEmpleado;
    }

    public String getAreaEmpleado() {
        return areaEmpleado;
    }

    public void setAreaEmpleado(String areaEmpleado) {
        this.areaEmpleado = areaEmpleado;
    }

    public BigDecimal getSalarioEmpleado() {
        return salarioEmpleado;
    }

    public void setSalarioEmpleado(BigDecimal salarioEmpleado) {
        this.salarioEmpleado = salarioEmpleado;
    }

    public LocalDate getFechaContratacionEmpleado() {
        return fechaContratacionEmpleado;
    }

    public void setFechaContratacionEmpleado(LocalDate fechaContratacionEmpleado) {
        this.fechaContratacionEmpleado = fechaContratacionEmpleado;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<TareaProduccion> getTareasProduccion() {
        return tareasProduccion;
    }

    public void setTareasProduccion(Set<TareaProduccion> tareasProduccion) {
        this.tareasProduccion = tareasProduccion;
    }

    // equals y hashCode (basado en numeroDocumento si está disponible, sino idEmpleado)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        if (numeroDocumento != null ? !numeroDocumento.equals(empleado.numeroDocumento) : empleado.numeroDocumento != null) {
            if (numeroDocumento == null || empleado.numeroDocumento == null) {
                return idEmpleado != null && idEmpleado.equals(empleado.idEmpleado);
            }
            return false;
        }
        if (numeroDocumento == null && empleado.numeroDocumento == null) {
            return idEmpleado != null && idEmpleado.equals(empleado.idEmpleado);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = numeroDocumento != null ? numeroDocumento.hashCode() : 0;
        if (numeroDocumento == null) {
            result = 31 * result + (idEmpleado != null ? idEmpleado.hashCode() : 0);
        }
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado=" + idEmpleado +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", nombreEmpleado='" + nombreEmpleado + '\'' +
                ", cargoEmpleado='" + cargoEmpleado + '\'' +
                '}';
    }
}