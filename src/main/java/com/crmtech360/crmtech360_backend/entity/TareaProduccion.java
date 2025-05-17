package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "tareasproduccion")
public class TareaProduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea_produccion")
    private Integer idTareaProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_produccion") // ON DELETE CASCADE en la BD
    private OrdenProduccion ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado") // ON DELETE SET NULL en la BD
    private Empleado empleado;

    @Column(name = "nombre_tarea", nullable = false, length = 100)
    private String nombreTarea;

    @Column(name = "fecha_inicio_tarea")
    private LocalDateTime fechaInicioTarea;

    @Column(name = "fecha_fin_tarea")
    private LocalDateTime fechaFinTarea;

    @Column(name = "duracion_estimada_tarea")
    private LocalTime duracionEstimadaTarea;

    @Column(name = "duracion_real_tarea")
    private LocalTime duracionRealTarea;

    @Column(name = "estado_tarea", length = 20)
    private String estadoTarea; // 'Pendiente', 'En Curso', 'Completada', 'Bloqueada'

    @Column(name = "observaciones_tarea", columnDefinition = "TEXT")
    private String observacionesTarea;

    // Constructores
    public TareaProduccion() {
    }

    public TareaProduccion(OrdenProduccion ordenProduccion, Empleado empleado, String nombreTarea, LocalDateTime fechaInicioTarea, LocalTime duracionEstimadaTarea, String estadoTarea, String observacionesTarea) {
        this.ordenProduccion = ordenProduccion;
        this.empleado = empleado;
        this.nombreTarea = nombreTarea;
        this.fechaInicioTarea = fechaInicioTarea;
        this.duracionEstimadaTarea = duracionEstimadaTarea;
        this.estadoTarea = estadoTarea;
        this.observacionesTarea = observacionesTarea;
    }

    // Getters y Setters
    public Integer getIdTareaProduccion() {
        return idTareaProduccion;
    }

    public void setIdTareaProduccion(Integer idTareaProduccion) {
        this.idTareaProduccion = idTareaProduccion;
    }

    public OrdenProduccion getOrdenProduccion() {
        return ordenProduccion;
    }

    public void setOrdenProduccion(OrdenProduccion ordenProduccion) {
        this.ordenProduccion = ordenProduccion;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public LocalDateTime getFechaInicioTarea() {
        return fechaInicioTarea;
    }

    public void setFechaInicioTarea(LocalDateTime fechaInicioTarea) {
        this.fechaInicioTarea = fechaInicioTarea;
    }

    public LocalDateTime getFechaFinTarea() {
        return fechaFinTarea;
    }

    public void setFechaFinTarea(LocalDateTime fechaFinTarea) {
        this.fechaFinTarea = fechaFinTarea;
    }

    public LocalTime getDuracionEstimadaTarea() {
        return duracionEstimadaTarea;
    }

    public void setDuracionEstimadaTarea(LocalTime duracionEstimadaTarea) {
        this.duracionEstimadaTarea = duracionEstimadaTarea;
    }

    public LocalTime getDuracionRealTarea() {
        return duracionRealTarea;
    }

    public void setDuracionRealTarea(LocalTime duracionRealTarea) {
        this.duracionRealTarea = duracionRealTarea;
    }

    public String getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public String getObservacionesTarea() {
        return observacionesTarea;
    }

    public void setObservacionesTarea(String observacionesTarea) {
        this.observacionesTarea = observacionesTarea;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TareaProduccion that = (TareaProduccion) o;
        return Objects.equals(idTareaProduccion, that.idTareaProduccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTareaProduccion);
    }

    // toString
    @Override
    public String toString() {
        return "TareaProduccion{" +
                "idTareaProduccion=" + idTareaProduccion +
                ", ordenProduccionId=" + (ordenProduccion != null ? ordenProduccion.getIdOrdenProduccion() : "null") +
                ", empleadoId=" + (empleado != null ? empleado.getIdEmpleado() : "null") +
                ", nombreTarea='" + nombreTarea + '\'' +
                ", estadoTarea='" + estadoTarea + '\'' +
                '}';
    }
}