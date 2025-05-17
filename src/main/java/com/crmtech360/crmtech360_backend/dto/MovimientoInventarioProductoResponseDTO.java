package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;
// Asumo que InventarioProductoRefDTO ya está definido y anotado
// import com.crmtech360.crmtech360_backend.dto.InventarioProductoRefDTO;

@Schema(description = "DTO para la respuesta de un movimiento de inventario de producto terminado registrado.")
public class MovimientoInventarioProductoResponseDTO {

    @Schema(description = "Identificador único del movimiento de inventario del producto.",
            example = "801",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idMovimientoProducto;

    @Schema(description = "Tipo de movimiento realizado ('Entrada' o 'Salida').",
            example = "Salida",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoMovimiento;

    @Schema(description = "Referencia resumida al registro de inventario de producto que fue afectado por este movimiento.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private InventarioProductoRefDTO inventarioProductoRef; // Asegúrate que InventarioProductoRefDTO esté anotado

    @Schema(description = "Cantidad del producto que se movió (entró o salió). Siempre es un valor positivo; el tipo de movimiento indica la dirección.",
            example = "10",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cantidadMovimiento;

    @Schema(description = "Fecha y hora en que se registró el movimiento de inventario.",
            example = "2024-05-14T11:30:00", // Formato ISO 8601
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaMovimiento;

    @Schema(description = "Descripción o justificación del movimiento (ej. 'Salida por Venta OV-001', 'Entrada de Producción OP-005').",
            example = "Salida por Orden de Venta OV-001",
            nullable = true,
            accessMode = Schema.AccessMode.READ_ONLY)
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioProductoResponseDTO() {
    }

    public MovimientoInventarioProductoResponseDTO(Integer idMovimientoProducto, String tipoMovimiento, InventarioProductoRefDTO inventarioProductoRef, Integer cantidadMovimiento, LocalDateTime fechaMovimiento, String descripcionMovimiento) {
        this.idMovimientoProducto = idMovimientoProducto;
        this.tipoMovimiento = tipoMovimiento;
        this.inventarioProductoRef = inventarioProductoRef;
        this.cantidadMovimiento = cantidadMovimiento;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
    }

    // Getters y Setters
    public Integer getIdMovimientoProducto() { return idMovimientoProducto; }
    public void setIdMovimientoProducto(Integer idMovimientoProducto) { this.idMovimientoProducto = idMovimientoProducto; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public InventarioProductoRefDTO getInventarioProductoRef() { return inventarioProductoRef; }
    public void setInventarioProductoRef(InventarioProductoRefDTO inventarioProductoRef) { this.inventarioProductoRef = inventarioProductoRef; }
    public Integer getCantidadMovimiento() { return cantidadMovimiento; }
    public void setCantidadMovimiento(Integer cantidadMovimiento) { this.cantidadMovimiento = cantidadMovimiento; }
    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getDescripcionMovimiento() { return descripcionMovimiento; }
    public void setDescripcionMovimiento(String descripcionMovimiento) { this.descripcionMovimiento = descripcionMovimiento; }
}