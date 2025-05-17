package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO de referencia resumida para un registro de inventario de producto terminado específico. " +
        "Usualmente utilizado para identificar el inventario afectado en un movimiento o en listados concisos.")
public class InventarioProductoRefDTO {

    @Schema(description = "Identificador único del registro de inventario del producto.",
            example = "601",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventarioProducto;

    @Schema(description = "Nombre o código de la ubicación física donde se encuentra este inventario de producto.",
            example = "Almacén Principal",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String ubicacionInventario;

    @Schema(description = "Referencia del producto para fácil identificación y contexto.",
            example = "CAM-AZ-M-001",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String productoReferencia;

    // Constructores
    public InventarioProductoRefDTO() {}

    public InventarioProductoRefDTO(Integer idInventarioProducto, String ubicacionInventario, String productoReferencia) {
        this.idInventarioProducto = idInventarioProducto;
        this.ubicacionInventario = ubicacionInventario;
        this.productoReferencia = productoReferencia;
    }

    // Getters y Setters
    public Integer getIdInventarioProducto() { return idInventarioProducto; }
    public void setIdInventarioProducto(Integer idInventarioProducto) { this.idInventarioProducto = idInventarioProducto; }
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public String getProductoReferencia() { return productoReferencia; }
    public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }
}