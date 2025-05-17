package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ordenesproduccion")
public class OrdenProduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_produccion")
    private Integer idOrdenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_venta") // ON DELETE SET NULL en la BD
    private OrdenVenta ordenVenta;

    @Column(name = "fecha_creacion", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_inicio_produccion")
    private LocalDate fechaInicioProduccion;

    @Column(name = "fecha_fin_estimada_produccion")
    private LocalDate fechaFinEstimadaProduccion;

    @Column(name = "fecha_fin_real_produccion")
    private LocalDate fechaFinRealProduccion;

    @Column(name = "estado_produccion", nullable = false, length = 20)
    private String estadoProduccion; // 'Pendiente', 'En Proceso', 'Terminada', 'Retrasada', 'Anulada'

    @Column(name = "observaciones_produccion", columnDefinition = "TEXT")
    private String observacionesProduccion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "ordenProduccion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TareaProduccion> tareasProduccion;

    // Constructores
    public OrdenProduccion() {
    }

    public OrdenProduccion(OrdenVenta ordenVenta, LocalDate fechaInicioProduccion, LocalDate fechaFinEstimadaProduccion, String estadoProduccion, String observacionesProduccion) {
        this.ordenVenta = ordenVenta;
        this.fechaInicioProduccion = fechaInicioProduccion;
        this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion;
        this.estadoProduccion = estadoProduccion;
        this.observacionesProduccion = observacionesProduccion;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdOrdenProduccion() {
        return idOrdenProduccion;
    }

    public void setIdOrdenProduccion(Integer idOrdenProduccion) {
        this.idOrdenProduccion = idOrdenProduccion;
    }

    public OrdenVenta getOrdenVenta() {
        return ordenVenta;
    }

    public void setOrdenVenta(OrdenVenta ordenVenta) {
        this.ordenVenta = ordenVenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaInicioProduccion() {
        return fechaInicioProduccion;
    }

    public void setFechaInicioProduccion(LocalDate fechaInicioProduccion) {
        this.fechaInicioProduccion = fechaInicioProduccion;
    }

    public LocalDate getFechaFinEstimadaProduccion() {
        return fechaFinEstimadaProduccion;
    }

    public void setFechaFinEstimadaProduccion(LocalDate fechaFinEstimadaProduccion) {
        this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion;
    }

    public LocalDate getFechaFinRealProduccion() {
        return fechaFinRealProduccion;
    }

    public void setFechaFinRealProduccion(LocalDate fechaFinRealProduccion) {
        this.fechaFinRealProduccion = fechaFinRealProduccion;
    }

    public String getEstadoProduccion() {
        return estadoProduccion;
    }

    public void setEstadoProduccion(String estadoProduccion) {
        this.estadoProduccion = estadoProduccion;
    }

    public String getObservacionesProduccion() {
        return observacionesProduccion;
    }

    public void setObservacionesProduccion(String observacionesProduccion) {
        this.observacionesProduccion = observacionesProduccion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<TareaProduccion> getTareasProduccion() {
        return tareasProduccion;
    }

    public void setTareasProduccion(Set<TareaProduccion> tareasProduccion) {
        this.tareasProduccion = tareasProduccion;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenProduccion that = (OrdenProduccion) o;
        return Objects.equals(idOrdenProduccion, that.idOrdenProduccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdenProduccion);
    }

    // toString
    @Override
    public String toString() {
        return "OrdenProduccion{" +
                "idOrdenProduccion=" + idOrdenProduccion +
                ", ordenVentaId=" + (ordenVenta != null ? ordenVenta.getIdOrdenVenta() : "null") +
                ", estadoProduccion='" + estadoProduccion + '\'' +
                ", fechaInicioProduccion=" + fechaInicioProduccion +
                ", fechaFinEstimadaProduccion=" + fechaFinEstimadaProduccion +
                '}';
    }
}