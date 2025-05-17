package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de un movimiento de inventario de insumo registrado.")
public class MovimientoInventarioInsumoResponseDTO {

    @Schema(description = "Identificador único del movimiento de inventario de insumo.",
            example = "301",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idMovimientoInsumo;

    @Schema(description = "Tipo de movimiento realizado ('Entrada' o 'Salida').",
            example = "Entrada",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoMovimiento;

    @Schema(description = "Referencia resumida al registro de inventario de insumo que fue afectado por este movimiento.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private InventarioInsumoRefDTO inventarioInsumoRef; // Asegúrate que InventarioInsumoRefDTO esté anotado

    @Schema(description = "Cantidad del insumo que se movió (entró o salió). Siempre es un valor positivo; el tipo de movimiento indica la dirección.",
            example = "25.500",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal cantidadMovimiento;

    @Schema(description = "Fecha y hora en que se registró el movimiento de inventario.",
            example = "2024-05-13T10:00:00", // Formato ISO 8601
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaMovimiento;

    @Schema(description = "Descripción o justificación del movimiento (ej. 'Entrada por OC-123', 'Salida para Producción OP-789').",
            example = "Entrada por Orden de Compra OC-123",
            nullable = true,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioInsumoResponseDTO() {
    }

    public MovimientoInventarioInsumoResponseDTO(Integer idMovimientoInsumo, String tipoMovimiento, InventarioInsumoRefDTO inventarioInsumoRef, BigDecimal cantidadMovimiento, LocalDateTime fechaMovimiento, String descripcionMovimiento) {
        this.idMovimientoInsumo = idMovimientoInsumo;
        this.tipoMovimiento = tipoMovimiento;
        this.inventarioInsumoRef = inventarioInsumoRef;
        this.cantidadMovimiento = cantidadMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
    }

    // Getters y Setters
    public Integer getIdMovimientoInsumo() { return idMovimientoInsumo; }
    public void setIdMovimientoInsumo(Integer idMovimientoInsumo) { this.idMovimientoInsumo = idMovimientoInsumo; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public InventarioInsumoRefDTO getInventarioInsumoRef() { return inventarioInsumoRef; }
    public void setInventarioInsumoRef(InventarioInsumoRefDTO inventarioInsumoRef) { this.inventarioInsumoRef = inventarioInsumoRef; }
    public BigDecimal getCantidadMovimiento() { return cantidadMovimiento; }
    public void setCantidadMovimiento(BigDecimal cantidadMovimiento) { this.cantidadMovimiento = cantidadMovimiento; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getDescripcionMovimiento() { return descripcionMovimiento; }
    public void setDescripcionMovimiento(String descripcionMovimiento) { this.descripcionMovimiento = descripcionMovimiento; }
}