package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Integer idInsumo;

    @Column(name = "nombre_insumo", nullable = false, length = 255)
    private String nombreInsumo;

    @Column(name = "descripcion_insumo", columnDefinition = "TEXT")
    private String descripcionInsumo;

    @Column(name = "unidad_medida_insumo", nullable = false, length = 50)
    private String unidadMedidaInsumo;

    @Column(name = "stock_minimo_insumo", columnDefinition = "INTEGER DEFAULT 0")
    private Integer stockMinimoInsumo;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InsumoPorProducto> insumosPorProducto;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetalleOrdenCompra> detallesOrdenCompra;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InventarioInsumo> inventariosInsumo;


    // Constructores
    public Insumo() {
        this.stockMinimoInsumo = 0; // Valor por defecto
    }

    public Insumo(String nombreInsumo, String descripcionInsumo, String unidadMedidaInsumo, Integer stockMinimoInsumo) {
        this.nombreInsumo = nombreInsumo;
        this.descripcionInsumo = descripcionInsumo;
        this.unidadMedidaInsumo = unidadMedidaInsumo;
        this.stockMinimoInsumo = (stockMinimoInsumo != null) ? stockMinimoInsumo : 0;
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (this.stockMinimoInsumo == null) {
            this.stockMinimoInsumo = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getDescripcionInsumo() {
        return descripcionInsumo;
    }

    public void setDescripcionInsumo(String descripcionInsumo) {
        this.descripcionInsumo = descripcionInsumo;
    }

    public String getUnidadMedidaInsumo() {
        return unidadMedidaInsumo;
    }

    public void setUnidadMedidaInsumo(String unidadMedidaInsumo) {
        this.unidadMedidaInsumo = unidadMedidaInsumo;
    }

    public Integer getStockMinimoInsumo() {
        return stockMinimoInsumo;
    }

    public void setStockMinimoInsumo(Integer stockMinimoInsumo) {
        this.stockMinimoInsumo = stockMinimoInsumo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Set<InsumoPorProducto> getInsumosPorProducto() {
        return insumosPorProducto;
    }

    public void setInsumosPorProducto(Set<InsumoPorProducto> insumosPorProducto) {
        this.insumosPorProducto = insumosPorProducto;
    }

    public Set<DetalleOrdenCompra> getDetallesOrdenCompra() {
        return detallesOrdenCompra;
    }

    public void setDetallesOrdenCompra(Set<DetalleOrdenCompra> detallesOrdenCompra) {
        this.detallesOrdenCompra = detallesOrdenCompra;
    }

    public Set<InventarioInsumo> getInventariosInsumo() {
        return inventariosInsumo;
    }

    public void setInventariosInsumo(Set<InventarioInsumo> inventariosInsumo) {
        this.inventariosInsumo = inventariosInsumo;
    }

    // equals y hashCode (basado en nombreInsumo si está disponible, sino idInsumo)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Insumo insumo = (Insumo) o;
        if (nombreInsumo != null ? !nombreInsumo.equals(insumo.nombreInsumo) : insumo.nombreInsumo != null) {
            if (nombreInsumo == null || insumo.nombreInsumo == null) {
                return idInsumo != null && idInsumo.equals(insumo.idInsumo);
            }
            return false;
        }
        if (nombreInsumo == null && insumo.nombreInsumo == null) {
            return idInsumo != null && idInsumo.equals(insumo.idInsumo);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = nombreInsumo != null ? nombreInsumo.hashCode() : 0;
        if (nombreInsumo == null) {
            result = 31 * result + (idInsumo != null ? idInsumo.hashCode() : 0);
        }
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Insumo{" +
                "idInsumo=" + idInsumo +
                ", nombreInsumo='" + nombreInsumo + '\'' +
                ", unidadMedidaInsumo='" + unidadMedidaInsumo + '\'' +
                ", stockMinimoInsumo=" + stockMinimoInsumo +
                '}';
    }
}