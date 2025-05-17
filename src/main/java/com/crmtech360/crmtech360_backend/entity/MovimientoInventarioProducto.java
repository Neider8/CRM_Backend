package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "movimientosinventarioproductos")
public class MovimientoInventarioProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_producto")
    private Integer idMovimientoProducto;

    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private String tipoMovimiento; // 'Entrada', 'Salida'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inventario_producto", nullable = false) // ON DELETE CASCADE en la BD
    private InventarioProducto inventarioProducto;

    @Column(name = "cantidad_movimiento", nullable = false)
    private Integer cantidadMovimiento;

    @Column(name = "fecha_movimiento", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "descripcion_movimiento", columnDefinition = "TEXT")
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioProducto() {
    }

    public MovimientoInventarioProducto(String tipoMovimiento, InventarioProducto inventarioProducto, Integer cantidadMovimiento, String descripcionMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        this.inventarioProducto = inventarioProducto;
        this.cantidadMovimiento = cantidadMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
    }

    @PrePersist
    protected void onCreate() {
        if (fechaMovimiento == null) {
            fechaMovimiento = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Integer getIdMovimientoProducto() {
        return idMovimientoProducto;
    }

    public void setIdMovimientoProducto(Integer idMovimientoProducto) {
        this.idMovimientoProducto = idMovimientoProducto;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public InventarioProducto getInventarioProducto() {
        return inventarioProducto;
    }

    public void setInventarioProducto(InventarioProducto inventarioProducto) {
        this.inventarioProducto = inventarioProducto;
    }

    public Integer getCantidadMovimiento() {
        return cantidadMovimiento;
    }

    public void setCantidadMovimiento(Integer cantidadMovimiento) {
        this.cantidadMovimiento = cantidadMovimiento;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getDescripcionMovimiento() {
        return descripcionMovimiento;
    }

    public void setDescripcionMovimiento(String descripcionMovimiento) {
        this.descripcionMovimiento = descripcionMovimiento;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimientoInventarioProducto that = (MovimientoInventarioProducto) o;
        return Objects.equals(idMovimientoProducto, that.idMovimientoProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimientoProducto);
    }

    // toString
    @Override
    public String toString() {
        return "MovimientoInventarioProducto{" +
                "idMovimientoProducto=" + idMovimientoProducto +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", inventarioProductoId=" + (inventarioProducto != null ? inventarioProducto.getIdInventarioProducto() : "null") +
                ", cantidadMovimiento=" + cantidadMovimiento +
                ", fechaMovimiento=" + fechaMovimiento +
                '}';
    }
}