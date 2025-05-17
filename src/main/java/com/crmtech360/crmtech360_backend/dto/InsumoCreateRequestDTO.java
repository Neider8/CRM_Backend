package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull; // No se usa NotNull directamente aquí
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de creación de un nuevo insumo (materia prima o componente).")
public class InsumoCreateRequestDTO {

    @Schema(description = "Nombre único y descriptivo del insumo.",
            example = "Tela Algodón Jersey Azul Rey",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del insumo no puede estar vacío.")
    @Size(max = 255, message = "El nombre del insumo no puede exceder los 255 caracteres.")
    private String nombreInsumo;

    @Schema(description = "Descripción detallada adicional del insumo, si es necesaria.",
            example = "Algodón peinado, 180gr/m2, ideal para camisetas.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String descripcionInsumo; // TEXT, opcional

    @Schema(description = "Unidad en la que se mide y gestiona el insumo (ej. Metros, Kilos, Unidades, Rollos).",
            example = "Metros",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La unidad de medida del insumo no puede estar vacía.")
    @Size(max = 50, message = "La unidad de medida del insumo no puede exceder los 50 caracteres.")
    private String unidadMedidaInsumo;

    @Schema(description = "Nivel de stock mínimo deseado para este insumo. Si el stock actual cae por debajo de este valor, se podrían generar alertas. No puede ser negativo. Si se omite, podría tomar un valor por defecto (ej. 0).",
            example = "50",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, puede tener un default
            nullable = true)
    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    private Integer stockMinimoInsumo; // Por defecto 0 en la entidad

    // Constructores
    public InsumoCreateRequestDTO() {
    }

    public InsumoCreateRequestDTO(String nombreInsumo, String descripcionInsumo, String unidadMedidaInsumo, Integer stockMinimoInsumo) {
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