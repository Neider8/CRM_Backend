package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ordenesventa")
public class OrdenVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_venta")
    private Integer idOrdenVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente") // ON DELETE CASCADE es manejado por la BD.
    private Cliente cliente;

    @Column(name = "fecha_pedido", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "estado_orden", nullable = false, length = 20)
    private String estadoOrden; // 'Pendiente', 'Confirmada', 'En Producción', 'Entregada', 'Anulada'

    @Column(name = "total_orden", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOrden;

    @Column(name = "observaciones_orden", columnDefinition = "TEXT")
    private String observacionesOrden;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "ordenVenta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetalleOrdenVenta> detallesOrdenVenta;

    @OneToMany(mappedBy = "ordenVenta", fetch = FetchType.LAZY) // ON DELETE SET NULL en BD
    private Set<OrdenProduccion> ordenesProduccion;

    @OneToMany(mappedBy = "ordenVenta", fetch = FetchType.LAZY) // ON DELETE SET NULL en BD
    private Set<PagoCobro> pagosCobros;


    // Constructores
    public OrdenVenta() {
    }

    public OrdenVenta(Cliente cliente, LocalDate fechaEntregaEstimada, String estadoOrden, BigDecimal totalOrden, String observacionesOrden) {
        this.cliente = cliente;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estadoOrden = estadoOrden;
        this.totalOrden = totalOrden;
        this.observacionesOrden = observacionesOrden;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaPedido == null) { // Permite establecerlo explícitamente si es necesario
            fechaPedido = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdOrdenVenta() {
        return idOrdenVenta;
    }

    public void setIdOrdenVenta(Integer idOrdenVenta) {
        this.idOrdenVenta = idOrdenVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public BigDecimal getTotalOrden() {
        return totalOrden;
    }

    public void setTotalOrden(BigDecimal totalOrden) {
        this.totalOrden = totalOrden;
    }

    public String getObservacionesOrden() {
        return observacionesOrden;
    }

    public void setObservacionesOrden(String observacionesOrden) {
        this.observacionesOrden = observacionesOrden;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<DetalleOrdenVenta> getDetallesOrdenVenta() {
        return detallesOrdenVenta;
    }

    public void setDetallesOrdenVenta(Set<DetalleOrdenVenta> detallesOrdenVenta) {
        this.detallesOrdenVenta = detallesOrdenVenta;
    }

    public Set<OrdenProduccion> getOrdenesProduccion() {
        return ordenesProduccion;
    }

    public void setOrdenesProduccion(Set<OrdenProduccion> ordenesProduccion) {
        this.ordenesProduccion = ordenesProduccion;
    }

    public Set<PagoCobro> getPagosCobros() {
        return pagosCobros;
    }

    public void setPagosCobros(Set<PagoCobro> pagosCobros) {
        this.pagosCobros = pagosCobros;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenVenta that = (OrdenVenta) o;
        return Objects.equals(idOrdenVenta, that.idOrdenVenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdenVenta);
    }

    // toString
    @Override
    public String toString() {
        return "OrdenVenta{" +
                "idOrdenVenta=" + idOrdenVenta +
                ", clienteId=" + (cliente != null ? cliente.getIdCliente() : "null") +
                ", fechaPedido=" + fechaPedido +
                ", estadoOrden='" + estadoOrden + '\'' +
                ", totalOrden=" + totalOrden +
                '}';
    }
}