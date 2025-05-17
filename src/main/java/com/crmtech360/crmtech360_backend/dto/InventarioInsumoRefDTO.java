package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO de referencia resumida para un registro de inventario de insumo específico. " +
        "Usualmente utilizado para identificar el inventario afectado en un movimiento o en listados concisos.")
public class InventarioInsumoRefDTO {

    @Schema(description = "Identificador único del registro de inventario del insumo.",
            example = "501",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventarioInsumo;

    @Schema(description = "Nombre o código de la ubicación física donde se encuentra este inventario de insumo.",
            example = "Bodega Principal",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String ubicacionInventario;

    @Schema(description = "Nombre del insumo para fácil identificación y contexto.",
            example = "Tela Algodón Jersey Azul Rey",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String insumoNombre; // Nombre del insumo para fácil identificación

    // Constructores
    public InventarioInsumoRefDTO() {}

    public InventarioInsumoRefDTO(Integer idInventarioInsumo, String ubicacionInventario, String insumoNombre) {
        this.idInventarioInsumo = idInventarioInsumo;
        this.ubicacionInventario = ubicacionInventario;
        this.insumoNombre = insumoNombre;
    }

    // Getters y Setters
    public Integer getIdInventarioInsumo() { return idInventarioInsumo; }
    public void setIdInventarioInsumo(Integer idInventarioInsumo) { this.idInventarioInsumo = idInventarioInsumo; }
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public String getInsumoNombre() { return insumoNombre; }
    public void setInsumoNombre(String insumoNombre) { this.insumoNombre = insumoNombre; }
}