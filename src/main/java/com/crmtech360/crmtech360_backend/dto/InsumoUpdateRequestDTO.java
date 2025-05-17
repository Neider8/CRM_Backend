package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de actualización de un insumo existente. " +
        "Todos los campos son opcionales; solo se actualizarán los campos proporcionados.")
public class InsumoUpdateRequestDTO {

    @Schema(description = "Nuevo nombre único y descriptivo del insumo. Si se proporciona, reemplazará el nombre existente.",
            example = "Tela Algodón Jersey Rojo Escarlata",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El nombre del insumo no puede exceder los 255 caracteres.")
    private String nombreInsumo;

    @Schema(description = "Nueva descripción detallada adicional del insumo.",
            example = "Algodón peinado, 180gr/m2, color rojo escarlata, ideal para camisetas y sudaderas.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String descripcionInsumo;

    @Schema(description = "Nueva unidad en la que se mide y gestiona el insumo (ej. Metros, Kilos, Unidades, Rollos).",
            example = "Kilos",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "La unidad de medida del insumo no puede exceder los 50 caracteres.")
    private String unidadMedidaInsumo;

    @Schema(description = "Nuevo nivel de stock mínimo deseado para este insumo. No puede ser negativo.",
            example = "25",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    private Integer stockMinimoInsumo;

    // Constructores
    public InsumoUpdateRequestDTO() {
    }

    public InsumoUpdateRequestDTO(String nombreInsumo, String descripcionInsumo, String unidadMedidaInsumo, Integer stockMinimoInsumo) {
        this.nombreInsumo = nombreInsumo;
        this.descripcionInsumo = descripcionInsumo;
        this.unidadMedidaInsumo = unidadMedidaInsumo;
        this.stockMinimoInsumo = stockMinimoInsumo;
    }

    // Getters y Setters
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
}