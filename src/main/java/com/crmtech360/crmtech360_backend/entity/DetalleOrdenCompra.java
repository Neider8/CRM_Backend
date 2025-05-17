package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detallesordencompra")
public class DetalleOrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_compra")
    private Integer idDetalleCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden_compra", nullable = false) // ON DELETE CASCADE en la BD
    private OrdenCompra ordenCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false) // ON DELETE RESTRICT en la BD
    private Insumo insumo;

    @Column(name = "cantidad_compra", nullable = false)
    private Integer cantidadCompra;

    @Column(name = "precio_unitario_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitarioCompra;

    @Column(name = "subtotal_compra", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalCompra;

    // Constructores
    public DetalleOrdenCompra() {
    }

    public DetalleOrdenCompra(OrdenCompra ordenCompra, Insumo insumo, Integer cantidadCompra, BigDecimal precioUnitarioCompra, BigDecimal subtotalCompra) {
        this.ordenCompra = ordenCompra;
        this.insumo = insumo;
        this.cantidadCompra = cantidadCompra;
        this.precioUnitarioCompra = precioUnitarioCompra;
        this.subtotalCompra = subtotalCompra;
    }

    // Getters y Setters
    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(Integer idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(OrdenCompra ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public Integer getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(Integer cantidadCompra) {
        this.cantidadCompra = cantidadCompra;
    }

    public BigDecimal getPrecioUnitarioCompra() {
        return precioUnitarioCompra;
    }

    public void setPrecioUnitarioCompra(BigDecimal precioUnitarioCompra) {
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    public BigDecimal getSubtotalCompra() {
        return subtotalCompra;
    }

    public void setSubtotalCompra(BigDecimal subtotalCompra) {
        this.subtotalCompra = subtotalCompra;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleOrdenCompra that = (DetalleOrdenCompra) o;
        return Objects.equals(idDetalleCompra, that.idDetalleCompra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDetalleCompra);
    }

    // toString
    @Override
    public String toString() {
        return "DetalleOrdenCompra{" +
                "idDetalleCompra=" + idDetalleCompra +
                ", ordenCompraId=" + (ordenCompra != null ? ordenCompra.getIdOrdenCompra() : "null") +
                ", insumoId=" + (insumo != null ? insumo.getIdInsumo() : "null") +
                ", cantidadCompra=" + cantidadCompra +
                ", precioUnitarioCompra=" + precioUnitarioCompra +
                '}';
    }
}