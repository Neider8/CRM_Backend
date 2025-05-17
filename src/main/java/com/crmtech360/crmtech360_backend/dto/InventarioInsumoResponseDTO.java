package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de información detallada de un registro de inventario de insumo, " +
        "mostrando el stock de un insumo en una ubicación específica.")
public class InventarioInsumoResponseDTO {

    @Schema(description = "Identificador único del registro de inventario del insumo.",
            example = "501",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventarioInsumo;

    @Schema(description = "Nombre o código de la ubicación física donde se almacena este inventario de insumo.",
            example = "Bodega Principal",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String ubicacionInventario;

    @Schema(description = "Información resumida del insumo al que pertenece este registro de inventario.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private InsumoSummaryDTO insumo; // Asegúrate que InsumoSummaryDTO esté anotado con @Schema

    @Schema(description = "Cantidad actual de stock disponible para este insumo en esta ubicación. Puede tener decimales.",
            example = "125.750",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal cantidadStock;

    @Schema(description = "Fecha y hora de la última actualización de este registro de inventario (ej. último movimiento).",
            example = "2024-05-10T11:20:30", // Formato ISO 8601
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime ultimaActualizacion;

    // Constructores
    public InventarioInsumoResponseDTO() {
    }

    public InventarioInsumoResponseDTO(Integer idInventarioInsumo, String ubicacionInventario, InsumoSummaryDTO insumo, BigDecimal cantidadStock, LocalDateTime ultimaActualizacion) {
        this.idInventarioInsumo = idInventarioInsumo;
        this.ubicacionInventario = ubicacionInventario;
        this.insumo = insumo;
        this.cantidadStock = cantidadStock;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    // Getters y Setters
    public Integer getIdInventarioInsumo() { return idInventarioInsumo; }
    public void setIdInventarioInsumo(Integer idInventarioInsumo) { this.idInventarioInsumo = idInventarioInsumo; }
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public InsumoSummaryDTO getInsumo() { return insumo; }
    public void setInsumo(InsumoSummaryDTO insumo) { this.insumo = insumo; }
    public BigDecimal getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(BigDecimal cantidadStock) { this.cantidadStock = cantidadStock; }
    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}