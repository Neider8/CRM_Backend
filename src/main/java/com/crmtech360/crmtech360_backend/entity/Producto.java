package com.crmtech360.crmtech360_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "referencia_producto", nullable = false, unique = true, length = 50)
    private String referenciaProducto;

    @Column(name = "nombre_producto", nullable = false, length = 255)
    private String nombreProducto;

    @Column(name = "descripcion_producto", columnDefinition = "TEXT")
    private String descripcionProducto;

    @Column(name = "talla_producto", length = 10)
    private String tallaProducto;

    @Column(name = "color_producto", length = 50)
    private String colorProducto;

    @Column(name = "tipo_producto", length = 50)
    private String tipoProducto;

    @Column(name = "genero_producto", length = 20)
    private String generoProducto;

    @Column(name = "costo_produccion", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoProduccion;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(name = "unidad_medida_producto", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'Unidad'")
    private String unidadMedidaProducto;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DetalleOrdenVenta> detallesOrdenVenta;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InsumoPorProducto> insumosPorProducto;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<InventarioProducto> inventariosProducto;

    // Constructores
    public Producto() {
        this.unidadMedidaProducto = "Unidad"; // Valor por defecto
    }

    public Producto(String referenciaProducto, String nombreProducto, String descripcionProducto, String tallaProducto, String colorProducto, String tipoProducto, String generoProducto, BigDecimal costoProduccion, BigDecimal precioVenta, String unidadMedidaProducto) {
        this.referenciaProducto = referenciaProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.tallaProducto = tallaProducto;
        this.colorProducto = colorProducto;
        this.tipoProducto = tipoProducto;
        this.generoProducto = generoProducto;
        this.costoProduccion = costoProduccion;
        this.precioVenta = precioVenta;
        this.unidadMedidaProducto = unidadMedidaProducto != null ? unidadMedidaProducto : "Unidad";
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (this.unidadMedidaProducto == null) {
            this.unidadMedidaProducto = "Unidad";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getReferenciaProducto() {
        return referenciaProducto;
    }

    public void setReferenciaProducto(String referenciaProducto) {
        this.referenciaProducto = referenciaProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getTallaProducto() {
        return tallaProducto;
    }

    public void setTallaProducto(String tallaProducto) {
        this.tallaProducto = tallaProducto;
    }

    public String getColorProducto() {
        return colorProducto;
    }

    public void setColorProducto(String colorProducto) {
        this.colorProducto = colorProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getGeneroProducto() {
        return generoProducto;
    }

    public void setGeneroProducto(String generoProducto) {
        this.generoProducto = generoProducto;
    }

    public BigDecimal getCostoProduccion() {
        return costoProduccion;
    }

    public void setCostoProduccion(BigDecimal costoProduccion) {
        this.costoProduccion = costoProduccion;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getUnidadMedidaProducto() {
        return unidadMedidaProducto;
    }

    public void setUnidadMedidaProducto(String unidadMedidaProducto) {
        this.unidadMedidaProducto = unidadMedidaProducto;
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

    public Set<DetalleOrdenVenta> getDetallesOrdenVenta() {
        return detallesOrdenVenta;
    }

    public void setDetallesOrdenVenta(Set<DetalleOrdenVenta> detallesOrdenVenta) {
        this.detallesOrdenVenta = detallesOrdenVenta;
    }

    public Set<InsumoPorProducto> getInsumosPorProducto() {
        return insumosPorProducto;
    }

    public void setInsumosPorProducto(Set<InsumoPorProducto> insumosPorProducto) {
        this.insumosPorProducto = insumosPorProducto;
    }

    public Set<InventarioProducto> getInventariosProducto() {
        return inventariosProducto;
    }

    public void setInventariosProducto(Set<InventarioProducto> inventariosProducto) {
        this.inventariosProducto = inventariosProducto;
    }

    // equals y hashCode (basado en referenciaProducto si está disponible, sino idProducto)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        if (referenciaProducto != null ? !referenciaProducto.equals(producto.referenciaProducto) : producto.referenciaProducto != null) {
            if (referenciaProducto == null || producto.referenciaProducto == null) {
                return idProducto != null && idProducto.equals(producto.idProducto);
            }
            return false;
        }
        if (referenciaProducto == null && producto.referenciaProducto == null) {
            return idProducto != null && idProducto.equals(producto.idProducto);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = referenciaProducto != null ? referenciaProducto.hashCode() : 0;
        if (referenciaProducto == null) {
            result = 31 * result + (idProducto != null ? idProducto.hashCode() : 0);
        }
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "Producto{" +
                "idProducto=" + idProducto +
                ", referenciaProducto='" + referenciaProducto + '\'' +
                ", nombreProducto='" + nombreProducto + '\'' +
                ", tallaProducto='" + tallaProducto + '\'' +
                ", colorProducto='" + colorProducto + '\'' +
                ", precioVenta=" + precioVenta +
                '}';
    }
}