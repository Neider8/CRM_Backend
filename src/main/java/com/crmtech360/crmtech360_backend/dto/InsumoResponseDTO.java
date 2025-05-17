package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de información detallada de un insumo (materia prima o componente).")
public class InsumoResponseDTO {

    @Schema(description = "Identificador único del insumo.", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInsumo;

    @Schema(description = "Nombre único y descriptivo del insumo.", example = "Tela Algodón Jersey Azul Rey", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreInsumo;

    @Schema(description = "Descripción detallada adicional del insumo.", example = "Algodón peinado, 180gr/m2, ideal para camisetas.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String descripcionInsumo;

    @Schema(description = "Unidad en la que se mide y gestiona el insumo.", example = "Metros", accessMode = Schema.AccessMode.READ_ONLY)
    private String unidadMedidaInsumo;

    @Schema(description = "Nivel de stock mínimo deseado para este insumo. Ayuda a generar alertas de reabastecimiento.", example = "50", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Integer stockMinimoInsumo;

    @Schema(description = "Fecha y hora de creación del registro del insumo en el sistema.", example = "2023-02-10T08:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización del registro del insumo.", example = "2024-04-05T16:15:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public InsumoResponseDTO() {
    }

    public InsumoResponseDTO(Integer idInsumo, String nombreInsumo, String descripcionInsumo, String unidadMedidaInsumo, Integer stockMinimoInsumo, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.descripcionInsumo = descripcionInsumo;
        this.unidadMedidaInsumo = unidadMedidaInsumo;
        this.stockMinimoInsumo = stockMinimoInsumo;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
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
}