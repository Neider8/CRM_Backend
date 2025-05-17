package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de creación o actualización de un detalle (ítem) dentro de una orden de compra.")
public class DetalleOrdenCompraRequestDTO {

    @Schema(description = "Identificador único del insumo que se está comprando.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del insumo es obligatorio.")
    private Integer idInsumo;

    @Schema(description = "Cantidad del insumo a comprar. Debe ser un número entero mayor o igual a 1.",
            example = "100",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad de compra es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidadCompra;

    @Schema(description = "Precio unitario al que se está comprando el insumo. Debe ser mayor que cero y puede tener hasta 2 decimales.",
            example = "25.50",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio unitario de compra es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que cero.")
    @Digits(integer = 8, fraction = 2, message = "El precio unitario debe tener máximo 8 dígitos enteros y 2 decimales.")
    private BigDecimal precioUnitarioCompra;

    // Nota: El subtotal (cantidadCompra * precioUnitarioCompra) generalmente se calcula
    // en la lógica de negocio del servicio y no se envía en la solicitud.

    // Constructores
    public DetalleOrdenCompraRequestDTO() {
    }

    public DetalleOrdenCompraRequestDTO(Integer idInsumo, Integer cantidadCompra, BigDecimal precioUnitarioCompra) {
        this.idInsumo = idInsumo;
        this.cantidadCompra = cantidadCompra;
        this.precioUnitarioCompra = precioUnitarioCompra;
    }

    // Getters y Setters
    public Integer getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Integer idInsumo) { this.idInsumo = idInsumo; }
    public Integer getCantidadCompra() { return cantidadCompra; }
    public void setCantidadCompra(Integer cantidadCompra) { this.cantidadCompra = cantidadCompra; }
    public BigDecimal getPrecioUnitarioCompra() { return precioUnitarioCompra; }
    public void setPrecioUnitarioCompra(BigDecimal precioUnitarioCompra) { this.precioUnitarioCompra = precioUnitarioCompra; }
}