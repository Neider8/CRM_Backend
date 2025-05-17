package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "movimientosinventarioinsumos")
public class MovimientoInventarioInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_insumo")
    private Integer idMovimientoInsumo;

    @Column(name = "tipo_movimiento", nullable = false, length = 10)
    private String tipoMovimiento; // 'Entrada', 'Salida'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inventario_insumo", nullable = false) // ON DELETE CASCADE en la BD
    private InventarioInsumo inventarioInsumo;

    @Column(name = "cantidad_movimiento", nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidadMovimiento;

    @Column(name = "fecha_movimiento", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "descripcion_movimiento", columnDefinition = "TEXT")
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioInsumo() {
    }

    public MovimientoInventarioInsumo(String tipoMovimiento, InventarioInsumo inventarioInsumo, BigDecimal cantidadMovimiento, String descripcionMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        this.inventarioInsumo = inventarioInsumo;
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
    public Integer getIdMovimientoInsumo() {
        return idMovimientoInsumo;
    }

    public void setIdMovimientoInsumo(Integer idMovimientoInsumo) {
        this.idMovimientoInsumo = idMovimientoInsumo;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public InventarioInsumo getInventarioInsumo() {
        return inventarioInsumo;
    }

    public void setInventarioInsumo(InventarioInsumo inventarioInsumo) {
        this.inventarioInsumo = inventarioInsumo;
    }

    public BigDecimal getCantidadMovimiento() {
        return cantidadMovimiento;
    }

    public void setCantidadMovimiento(BigDecimal cantidadMovimiento) {
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
        MovimientoInventarioInsumo that = (MovimientoInventarioInsumo) o;
        return Objects.equals(idMovimientoInsumo, that.idMovimientoInsumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimientoInsumo);
    }

    // toString
    @Override
    public String toString() {
        return "MovimientoInventarioInsumo{" +
                "idMovimientoInsumo=" + idMovimientoInsumo +
                ", tipoMovimiento='" + tipoMovimiento + '\'' +
                ", inventarioInsumoId=" + (inventarioInsumo != null ? inventarioInsumo.getIdInventarioInsumo() : "null") +
                ", cantidadMovimiento=" + cantidadMovimiento +
                ", fechaMovimiento=" + fechaMovimiento +
                '}';
    }
}