package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "inventarioproductos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ubicacion_inventario", "id_producto"})
})
public class InventarioProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario_producto")
    private Integer idInventarioProducto;

    @Column(name = "ubicacion_inventario", nullable = false, length = 100)
    private String ubicacionInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto") // ON DELETE CASCADE en la BD
    private Producto producto;

    @Column(name = "cantidad_stock", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer cantidadStock;

    @Column(name = "ultima_actualizacion", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "inventarioProducto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MovimientoInventarioProducto> movimientosInventarioProducto;

    // Constructores
    public InventarioProducto() {
        this.cantidadStock = 0; // Valor por defecto
    }

    public InventarioProducto(String ubicacionInventario, Producto producto, Integer cantidadStock) {
        this.ubicacionInventario = ubicacionInventario;
        this.producto = producto;
        this.cantidadStock = (cantidadStock != null) ? cantidadStock : 0;
    }

    @PrePersist
    protected void onCreate() {
        ultimaActualizacion = LocalDateTime.now();
        if (this.cantidadStock == null) {
            this.cantidadStock = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdInventarioProducto() {
        return idInventarioProducto;
    }

    public void setIdInventarioProducto(Integer idInventarioProducto) {
        this.idInventarioProducto = idInventarioProducto;
    }

    public String getUbicacionInventario() {
        return ubicacionInventario;
    }

    public void setUbicacionInventario(String ubicacionInventario) {
        this.ubicacionInventario = ubicacionInventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public Set<MovimientoInventarioProducto> getMovimientosInventarioProducto() {
        return movimientosInventarioProducto;
    }

    public void setMovimientosInventarioProducto(Set<MovimientoInventarioProducto> movimientosInventarioProducto) {
        this.movimientosInventarioProducto = movimientosInventarioProducto;
    }

    // equals y hashCode
    // La unicidad está dada por (ubicacionInventario, idProducto) o por idInventarioProducto una vez asignado
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventarioProducto that = (InventarioProducto) o;
        if (idInventarioProducto != null) {
            return idInventarioProducto.equals(that.idInventarioProducto);
        }
        // Si el ID no está, comparar por la combinación única de negocio
        return Objects.equals(ubicacionInventario, that.ubicacionInventario) &&
                Objects.equals(producto, that.producto); // Producto debe tener equals/hashCode bien definidos
    }

    @Override
    public int hashCode() {
        if (idInventarioProducto != null) {
            return Objects.hash(idInventarioProducto);
        }
        return Objects.hash(ubicacionInventario, producto);
    }

    // toString
    @Override
    public String toString() {
        return "InventarioProducto{" +
                "idInventarioProducto=" + idInventarioProducto +
                ", ubicacionInventario='" + ubicacionInventario + '\'' +
                ", productoId=" + (producto != null ? producto.getIdProducto() : "null") +
                ", cantidadStock=" + cantidadStock +
                ", ultimaActualizacion=" + ultimaActualizacion +
                '}';
    }
}