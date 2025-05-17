package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de un insumo (materia prima o componente), " +
        "usualmente utilizado en listados o como parte de otros DTOs (ej. en Detalles de Orden de Compra o BOMs).")
public class InsumoSummaryDTO {

    @Schema(description = "Identificador único del insumo.", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInsumo;

    @Schema(description = "Nombre descriptivo y único del insumo.", example = "Tela Algodón Jersey Azul Rey", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreInsumo;

    @Schema(description = "Unidad en la que se mide y gestiona el insumo.", example = "Metros", accessMode = Schema.AccessMode.READ_ONLY)
    private String unidadMedidaInsumo;

    // Constructores
    public InsumoSummaryDTO() {
    }

    public InsumoSummaryDTO(Integer idInsumo, String nombreInsumo, String unidadMedidaInsumo) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.unidadMedidaInsumo = unidadMedidaInsumo;
    }

    // Getters y Setters
    public Integer getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Integer idInsumo) { this.idInsumo = idInsumo; }
    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
    public String getUnidadMedidaInsumo() { return unidadMedidaInsumo; }
    public void setUnidadMedidaInsumo(String unidadMedidaInsumo) { this.unidadMedidaInsumo = unidadMedidaInsumo; }
}