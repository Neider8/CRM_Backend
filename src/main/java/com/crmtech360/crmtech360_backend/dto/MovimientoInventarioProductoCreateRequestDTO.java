package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;

@Schema(description = "DTO para la solicitud de creación de un nuevo movimiento de inventario para un producto terminado (entrada o salida).")
public class MovimientoInventarioProductoCreateRequestDTO {

    @Schema(description = "Tipo de movimiento de inventario. Debe ser 'Entrada' o 'Salida'.",
            example = "Salida",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de movimiento no puede estar vacío.")
    @Pattern(regexp = "Entrada|Salida", message = "Tipo de movimiento inválido. Debe ser Entrada o Salida.")
    private String tipoMovimiento;

    @Schema(description = "Identificador único del registro de inventario de producto que se verá afectado por este movimiento (especifica el producto y su ubicación).",
            example = "701",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del registro de inventario afectado es obligatorio.")
    private Integer idInventarioProducto;

    @Schema(description = "Cantidad del producto que se mueve (entra o sale). Debe ser un número entero positivo.",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad del movimiento es obligatoria.")
    @Min(value = 1, message = "La cantidad del movimiento debe ser al menos 1.")
    private Integer cantidadMovimiento;

    @Schema(description = "Descripción o justificación del movimiento (ej. 'Salida por Venta OV-001', 'Entrada por Producción OP-005', 'Ajuste por conteo físico').",
            example = "Salida por Orden de Venta OV-001",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioProductoCreateRequestDTO() {
    }

    public MovimientoInventarioProductoCreateRequestDTO(String tipoMovimiento, Integer idInventarioProducto, Integer cantidadMovimiento, String descripcionMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        this.idInventarioProducto = idInventarioProducto;
        this.cantidadMovimiento = cantidadMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
    }

    // Getters y Setters
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public Integer getIdInventarioProducto() { return idInventarioProducto; }
    public void setIdInventarioProducto(Integer idInventarioProducto) { this.idInventarioProducto = idInventarioProducto; }
    public Integer getCantidadMovimiento() { return cantidadMovimiento; }
    public void setCantidadMovimiento(Integer cantidadMovimiento) { this.cantidadMovimiento = cantidadMovimiento; }
    public String getDescripcionMovimiento() { return descripcionMovimiento; }
    public void setDescripcionMovimiento(String descripcionMovimiento) { this.descripcionMovimiento = descripcionMovimiento; }
}