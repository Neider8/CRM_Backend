package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "pagoscobros")
public class PagoCobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_cobro")
    private Integer idPagoCobro;

    @Column(name = "tipo_transaccion", nullable = false, length = 10)
    private String tipoTransaccion; // 'Pago', 'Cobro'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_venta") // ON DELETE SET NULL en la BD
    private OrdenVenta ordenVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_compra") // ON DELETE SET NULL en la BD
    private OrdenCompra ordenCompra;

    @Column(name = "fecha_registro_transaccion", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaRegistroTransaccion;

    @Column(name = "fecha_pago_cobro")
    private LocalDate fechaPagoCobro;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "monto_transaccion", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTransaccion;

    @Column(name = "referencia_transaccion", length = 100)
    private String referenciaTransaccion;

    @Column(name = "estado_transaccion", nullable = false, length = 20)
    private String estadoTransaccion; // 'Pendiente', 'Pagado', 'Cobrado', 'Vencido', 'Anulado'

    @Column(name = "observaciones_transaccion", columnDefinition = "TEXT")
    private String observacionesTransaccion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public PagoCobro() {
    }

    public PagoCobro(String tipoTransaccion, OrdenVenta ordenVenta, OrdenCompra ordenCompra, LocalDate fechaPagoCobro, String metodoPago, BigDecimal montoTransaccion, String referenciaTransaccion, String estadoTransaccion, String observacionesTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
        this.ordenVenta = ordenVenta;
        this.ordenCompra = ordenCompra;
        this.fechaPagoCobro = fechaPagoCobro;
        this.metodoPago = metodoPago;
        this.montoTransaccion = montoTransaccion;
        this.referenciaTransaccion = referenciaTransaccion;
        this.estadoTransaccion = estadoTransaccion;
        this.observacionesTransaccion = observacionesTransaccion;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaRegistroTransaccion == null) {
            fechaRegistroTransaccion = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdPagoCobro() {
        return idPagoCobro;
    }

    public void setIdPagoCobro(Integer idPagoCobro) {
        this.idPagoCobro = idPagoCobro;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public OrdenVenta getOrdenVenta() {
        return ordenVenta;
    }

    public void setOrdenVenta(OrdenVenta ordenVenta) {
        this.ordenVenta = ordenVenta;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public LocalDateTime getFechaRegistroTransaccion() {
        return fechaRegistroTransaccion;
    }

    public void setFechaRegistroTransaccion(LocalDateTime fechaRegistroTransaccion) {
        this.fechaRegistroTransaccion = fechaRegistroTransaccion;
    }

    public LocalDate getFechaPagoCobro() {
        return fechaPagoCobro;
    }

    public void setFechaPagoCobro(LocalDate fechaPagoCobro) {
        this.fechaPagoCobro = fechaPagoCobro;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setMontoTransaccion(BigDecimal montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    public void setReferenciaTransaccion(String referenciaTransaccion) {
        this.referenciaTransaccion = referenciaTransaccion;
    }

    public String getEstadoTransaccion() {
        return estadoTransaccion;
    }

    public void setEstadoTransaccion(String estadoTransaccion) {
        this.estadoTransaccion = estadoTransaccion;
    }

    public String getObservacionesTransaccion() {
        return observacionesTransaccion;
    }

    public void setObservacionesTransaccion(String observacionesTransaccion) {
        this.observacionesTransaccion = observacionesTransaccion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagoCobro pagoCobro = (PagoCobro) o;
        return Objects.equals(idPagoCobro, pagoCobro.idPagoCobro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPagoCobro);
    }

    // toString
    @Override
    public String toString() {
        return "PagoCobro{" +
                "idPagoCobro=" + idPagoCobro +
                ", tipoTransaccion='" + tipoTransaccion + '\'' +
                ", ordenVentaId=" + (ordenVenta != null ? ordenVenta.getIdOrdenVenta() : "null") +
                ", ordenCompraId=" + (ordenCompra != null ? ordenCompra.getIdOrdenCompra() : "null") +
                ", montoTransaccion=" + montoTransaccion +
                ", estadoTransaccion='" + estadoTransaccion + '\'' +
                '}';
    }
}