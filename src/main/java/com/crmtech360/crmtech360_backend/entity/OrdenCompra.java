package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ordenescompra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_compra")
    private Integer idOrdenCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor") // ON DELETE SET NULL en la BD
    private Proveedor proveedor;

    @Column(name = "fecha_pedido_compra", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaPedidoCompra;

    @Column(name = "fecha_entrega_estimada_compra")
    private LocalDate fechaEntregaEstimadaCompra;

    @Column(name = "fecha_entrega_real_compra")
    private LocalDate fechaEntregaRealCompra;

    @Column(name = "estado_compra", nullable = false, length = 20)
    private String estadoCompra; // 'Pendiente', 'Enviada', 'Recibida Parcial', 'Recibida Total', 'Anulada'

    @Column(name = "total_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCompra;

    @Column(name = "observaciones_compra", columnDefinition = "TEXT")
    private String observacionesCompra;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetalleOrdenCompra> detallesOrdenCompra;

    @OneToMany(mappedBy = "ordenCompra", fetch = FetchType.LAZY) // ON DELETE SET NULL en BD
    private Set<PagoCobro> pagosCobros;


    // Constructores
    public OrdenCompra() {
    }

    public OrdenCompra(Proveedor proveedor, LocalDate fechaEntregaEstimadaCompra, String estadoCompra, BigDecimal totalCompra, String observacionesCompra) {
        this.proveedor = proveedor;
        this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra;
        this.estadoCompra = estadoCompra;
        this.totalCompra = totalCompra;
        this.observacionesCompra = observacionesCompra;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaPedidoCompra == null) {
            fechaPedidoCompra = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdOrdenCompra() {
        return idOrdenCompra;
    }

    public void setIdOrdenCompra(Integer idOrdenCompra) {
        this.idOrdenCompra = idOrdenCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDateTime getFechaPedidoCompra() {
        return fechaPedidoCompra;
    }

    public void setFechaPedidoCompra(LocalDateTime fechaPedidoCompra) {
        this.fechaPedidoCompra = fechaPedidoCompra;
    }

    public LocalDate getFechaEntregaEstimadaCompra() {
        return fechaEntregaEstimadaCompra;
    }

    public void setFechaEntregaEstimadaCompra(LocalDate fechaEntregaEstimadaCompra) {
        this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra;
    }

    public LocalDate getFechaEntregaRealCompra() {
        return fechaEntregaRealCompra;
    }

    public void setFechaEntregaRealCompra(LocalDate fechaEntregaRealCompra) {
        this.fechaEntregaRealCompra = fechaEntregaRealCompra;
    }

    public String getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(String estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public BigDecimal getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(BigDecimal totalCompra) {
        this.totalCompra = totalCompra;
    }

    public String getObservacionesCompra() {
        return observacionesCompra;
    }

    public void setObservacionesCompra(String observacionesCompra) {
        this.observacionesCompra = observacionesCompra;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<DetalleOrdenCompra> getDetallesOrdenCompra() {
        return detallesOrdenCompra;
    }

    public void setDetallesOrdenCompra(Set<DetalleOrdenCompra> detallesOrdenCompra) {
        this.detallesOrdenCompra = detallesOrdenCompra;
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
        OrdenCompra that = (OrdenCompra) o;
        return Objects.equals(idOrdenCompra, that.idOrdenCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrdenCompra);
    }

    // toString
    @Override
    public String toString() {
        return "OrdenCompra{" +
                "idOrdenCompra=" + idOrdenCompra +
                ", proveedorId=" + (proveedor != null ? proveedor.getIdProveedor() : "null") +
                ", fechaPedidoCompra=" + fechaPedidoCompra +
                ", estadoCompra='" + estadoCompra + '\'' +
                ", totalCompra=" + totalCompra +
                '}';
    }
}