package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "inventarioinsumos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ubicacion_inventario", "id_insumo"})
})
public class InventarioInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario_insumo")
    private Integer idInventarioInsumo;

    @Column(name = "ubicacion_inventario", nullable = false, length = 100)
    private String ubicacionInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo") // ON DELETE CASCADE en la BD
    private Insumo insumo;

    @Column(name = "cantidad_stock", nullable = false, precision = 10, scale = 3, columnDefinition = "DECIMAL(10,3) DEFAULT 0")
    private BigDecimal cantidadStock;

    @Column(name = "ultima_actualizacion", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "inventarioInsumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MovimientoInventarioInsumo> movimientosInventarioInsumo;

    // Constructores
    public InventarioInsumo() {
        this.cantidadStock = BigDecimal.ZERO; // Valor por defecto
    }

    public InventarioInsumo(String ubicacionInventario, Insumo insumo, BigDecimal cantidadStock) {
        this.ubicacionInventario = ubicacionInventario;
        this.insumo = insumo;
        this.cantidadStock = (cantidadStock != null) ? cantidadStock : BigDecimal.ZERO;
    }

    @PrePersist
    protected void onCreate() {
        ultimaActualizacion = LocalDateTime.now();
        if (this.cantidadStock == null) {
            this.cantidadStock = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdInventarioInsumo() {
        return idInventarioInsumo;
    }

    public void setIdInventarioInsumo(Integer idInventarioInsumo) {
        this.idInventarioInsumo = idInventarioInsumo;
    }

    public String getUbicacionInventario() {
        return ubicacionInventario;
    }

    public void setUbicacionInventario(String ubicacionInventario) {
        this.ubicacionInventario = ubicacionInventario;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(BigDecimal cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Set<MovimientoInventarioInsumo> getMovimientosInventarioInsumo() {
        return movimientosInventarioInsumo;
    }

    public void setMovimientosInventarioInsumo(Set<MovimientoInventarioInsumo> movimientosInventarioInsumo) {
        this.movimientosInventarioInsumo = movimientosInventarioInsumo;
    }

    // equals y hashCode
    // La unicidad está dada por (ubicacionInventario, idInsumo) o por idInventarioInsumo una vez asignado
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventarioInsumo that = (InventarioInsumo) o;
        if (idInventarioInsumo != null) {
            return idInventarioInsumo.equals(that.idInventarioInsumo);
        }
        return Objects.equals(ubicacionInventario, that.ubicacionInventario) &&
                Objects.equals(insumo, that.insumo); // Insumo debe tener equals/hashCode bien definidos
    }

    @Override
    public int hashCode() {
        if (idInventarioInsumo != null) {
            return Objects.hash(idInventarioInsumo);
        }
        return Objects.hash(ubicacionInventario, insumo);
    }

    // toString
    @Override
    public String toString() {
        return "InventarioInsumo{" +
                "idInventarioInsumo=" + idInventarioInsumo +
                ", ubicacionInventario='" + ubicacionInventario + '\'' +
                ", insumoId=" + (insumo != null ? insumo.getIdInsumo() : "null") +
                ", cantidadStock=" + cantidadStock +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}