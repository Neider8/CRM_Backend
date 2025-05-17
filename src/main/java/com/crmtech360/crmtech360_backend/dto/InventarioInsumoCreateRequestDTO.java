package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de creación de un nuevo registro de inventario para un insumo en una ubicación específica. " +
        "Esto usualmente establece el stock inicial o la primera entrada de un insumo en esa ubicación.")
public class InventarioInsumoCreateRequestDTO {

    @Schema(description = "Nombre o código de la ubicación física donde se almacenará el insumo (ej. 'Bodega Principal', 'Estante A-01').",
            example = "Bodega Principal",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La ubicación del inventario no puede estar vacía.")
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres.")
    private String ubicacionInventario;

    @Schema(description = "Identificador único del insumo para el cual se está creando el registro de inventario.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del insumo es obligatorio.")
    private Integer idInsumo;

    @Schema(description = "Cantidad inicial de stock para este insumo en la ubicación especificada. No puede ser negativa y puede tener hasta 3 decimales.",
            example = "150.000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad inicial de stock es obligatoria.")
    @DecimalMin(value = "0.0", message = "La cantidad de stock no puede ser negativa.")
    @Digits(integer=7, fraction=3, message="Formato inválido para cantidad (máx 7 enteros, 3 decimales)")
    private BigDecimal cantidadStock;

    // Constructores
    public InventarioInsumoCreateRequestDTO() {
    }

    public InventarioInsumoCreateRequestDTO(String ubicacionInventario, Integer idInsumo, BigDecimal cantidadStock) {
        this.ubicacionInventario = ubicacionInventario;
        this.idInsumo = idInsumo;
        this.cantidadStock = cantidadStock;
    }

    // Getters y Setters
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public Integer getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Integer idInsumo) { this.idInsumo = idInsumo; }
    public BigDecimal getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(BigDecimal cantidadStock) { this.cantidadStock = cantidadStock; }
}