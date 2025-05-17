package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de añadir o actualizar un insumo dentro de la Lista de Materiales (BOM) de un producto.")
public class InsumoPorProductoRequestDTO {

    @Schema(description = "Identificador único del producto al que pertenece este ítem del BOM. " +
            "Este campo es usualmente implícito por el endpoint (ej. /productos/{idProducto}/bom) y podría no ser necesario en el cuerpo del DTO si se maneja así.",
            example = "101",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Puede ser redundante si ya está en el path
            nullable = true) // Opcional en el cuerpo si ya está en la ruta
    @NotNull(message = "El ID del producto es obligatorio.") // Mantener validación si se envía en el cuerpo
    private Integer idProducto; // Si este DTO se usa en un endpoint como POST /productos/{idProducto}/bom, este campo podría ser redundante en el body.

    @Schema(description = "Identificador único del insumo que forma parte del BOM.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del insumo es obligatorio.")
    private Integer idInsumo;

    @Schema(description = "Cantidad del insumo requerida para producir una unidad del producto. " +
            "Debe ser mayor que cero y puede tener hasta 3 decimales para precisión.",
            example = "2.750",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad requerida es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad requerida debe ser mayor que cero.")
    @Digits(integer = 7, fraction = 3, message = "La cantidad requerida debe tener máximo 7 dígitos enteros y 3 decimales.")
    private BigDecimal cantidadRequerida;

    // Constructores
    public InsumoPorProductoRequestDTO() {
    }

    public InsumoPorProductoRequestDTO(Integer idProducto, Integer idInsumo, BigDecimal cantidadRequerida) {
        this.idProducto = idProducto;
        this.idInsumo = idInsumo;
        this.cantidadRequerida = cantidadRequerida;
    }

    // Getters y Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
}