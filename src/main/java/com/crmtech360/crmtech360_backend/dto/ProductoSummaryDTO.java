package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de un Producto, " +
        "usualmente utilizado en listados o como parte de otros DTOs (ej. en Detalles de Orden de Venta).")
public class ProductoSummaryDTO {

    @Schema(description = "Identificador único del producto.", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idProducto;

    @Schema(description = "Referencia única del producto (SKU) para identificación rápida.", example = "CAM-AZ-M-001", accessMode = Schema.AccessMode.READ_ONLY)
    private String referenciaProducto;

    @Schema(description = "Nombre descriptivo del producto.", example = "Camisa Algodón Clásica Azul Talla M", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreProducto;

    @Schema(description = "Talla del producto, útil para contexto en confección y ventas.", example = "M", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String tallaProducto;

    @Schema(description = "Color principal del producto, útil para contexto en confección y ventas.", example = "Azul Rey", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String colorProducto;

    // Constructores
    public ProductoSummaryDTO() {
    }

    public ProductoSummaryDTO(Integer idProducto, String referenciaProducto, String nombreProducto, String tallaProducto, String colorProducto) {
        this.idProducto = idProducto;
        this.referenciaProducto = referenciaProducto;
        this.nombreProducto = nombreProducto;
        this.tallaProducto = tallaProducto;
        this.colorProducto = colorProducto;
    }

    // Getters y Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getReferenciaProducto() { return referenciaProducto; }
    public void setReferenciaProducto(String referenciaProducto) { this.referenciaProducto = referenciaProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getTallaProducto() { return tallaProducto; }
    public void setTallaProducto(String tallaProducto) { this.tallaProducto = tallaProducto; }
    public String getColorProducto() { return colorProducto; }
    public void setColorProducto(String colorProducto) { this.colorProducto = colorProducto; }
}