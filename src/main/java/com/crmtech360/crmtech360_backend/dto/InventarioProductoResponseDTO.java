package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;
// Asumo que ProductoSummaryDTO ya está definido y anotado
// import com.crmtech360.crmtech360_backend.dto.ProductoSummaryDTO;

@Schema(description = "DTO para la respuesta de información detallada de un registro de inventario de producto terminado, " +
        "mostrando el stock de un producto en una ubicación específica.")
public class InventarioProductoResponseDTO {

    @Schema(description = "Identificador único del registro de inventario del producto.",
            example = "701",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventarioProducto;

    @Schema(description = "Nombre o código de la ubicación física donde se almacena este inventario de producto.",
            example = "Almacén Principal",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String ubicacionInventario;

    @Schema(description = "Información resumida del producto al que pertenece este registro de inventario.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private ProductoSummaryDTO producto; // Asegúrate que ProductoSummaryDTO esté anotado con @Schema

    @Schema(description = "Cantidad actual de stock disponible para este producto en esta ubicación (unidades enteras).",
            example = "150",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cantidadStock;

    @Schema(description = "Fecha y hora de la última actualización de este registro de inventario (ej. último movimiento).",
            example = "2024-05-12T18:45:00", // Formato ISO 8601
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime ultimaActualizacion;

    // Constructores
    public InventarioProductoResponseDTO() {
    }

    public InventarioProductoResponseDTO(Integer idInventarioProducto, String ubicacionInventario, ProductoSummaryDTO producto, Integer cantidadStock, LocalDateTime ultimaActualizacion) {
        this.idInventarioProducto = idInventarioProducto;
        this.ubicacionInventario = ubicacionInventario;
        this.producto = producto;
        this.cantidadStock = cantidadStock;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    // Getters y Setters
    public Integer getIdInventarioProducto() { return idInventarioProducto; }
    public void setIdInventarioProducto(Integer idInventarioProducto) { this.idInventarioProducto = idInventarioProducto; }
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public ProductoSummaryDTO getProducto() { return producto; }
    public void setProducto(ProductoSummaryDTO producto) { this.producto = producto; }
    public Integer getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(Integer cantidadStock) { this.cantidadStock = cantidadStock; }
    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}