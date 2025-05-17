package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de creación de un nuevo movimiento de inventario para un insumo (entrada o salida).")
public class MovimientoInventarioInsumoCreateRequestDTO {

    @Schema(description = "Tipo de movimiento de inventario. Debe ser 'Entrada' o 'Salida'.",
            example = "Entrada",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de movimiento no puede estar vacío.")
    @Pattern(regexp = "Entrada|Salida", message = "Tipo de movimiento inválido. Debe ser Entrada o Salida.")
    private String tipoMovimiento;

    @Schema(description = "Identificador único del registro de inventario de insumo que se verá afectado por este movimiento.",
            example = "501",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del registro de inventario de insumo afectado es obligatorio.")
    private Integer idInventarioInsumo;

    @Schema(description = "Cantidad del insumo que se mueve (entra o sale). Debe ser un valor positivo y puede tener hasta 3 decimales.",
            example = "25.500",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad del movimiento es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad del movimiento debe ser positiva.")
    @Digits(integer=7, fraction=3, message="Formato inválido para cantidad (máx 7 enteros, 3 decimales)")
    private BigDecimal cantidadMovimiento;

    @Schema(description = "Descripción o justificación del movimiento (ej. 'Entrada por OC-123', 'Salida para Producción OP-789', 'Ajuste de inventario por conteo').",
            example = "Entrada por Orden de Compra OC-123",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcionMovimiento;

    // Constructores
    public MovimientoInventarioInsumoCreateRequestDTO() {
    }

    public MovimientoInventarioInsumoCreateRequestDTO(String tipoMovimiento, Integer idInventarioInsumo, BigDecimal cantidadMovimiento, String descripcionMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        this.idInventarioInsumo = idInventarioInsumo;
        this.cantidadMovimiento = cantidadMovimiento;
        this.descripcionMovimiento = descripcionMovimiento;
    }

    // Getters y Setters
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public Integer getIdInventarioInsumo() { return idInventarioInsumo; }
    public void setIdInventarioInsumo(Integer idInventarioInsumo) { this.idInventarioInsumo = idInventarioInsumo; }
    public BigDecimal getCantidadMovimiento() { return cantidadMovimiento; }
    public void setCantidadMovimiento(BigDecimal cantidadMovimiento) { this.cantidadMovimiento = cantidadMovimiento; }
    public String getDescripcionMovimiento() { return descripcionMovimiento; }
    public void setDescripcionMovimiento(String descripcionMovimiento) { this.descripcionMovimiento = descripcionMovimiento; }
}