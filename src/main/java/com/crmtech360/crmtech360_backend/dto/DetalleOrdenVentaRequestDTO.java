package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de creación o actualización de un detalle (ítem de producto) dentro de una orden de venta.")
public class DetalleOrdenVentaRequestDTO {

    @Schema(description = "Identificador único del producto que se está vendiendo.",
            example = "25",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del producto es obligatorio.")
    private Integer idProducto;

    @Schema(description = "Cantidad del producto a vender. Debe ser un número entero mayor o igual a 1.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad del producto es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidadProducto;

    @Schema(description = "Precio unitario al que se vende el producto en esta orden. " +
            "Si se omite o es nulo, se podría usar el precio base del producto (lógica de servicio). " +
            "Debe ser mayor que cero y puede tener hasta 2 decimales si se especifica.",
            example = "150.75",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, permite usar precio base del producto
            nullable = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que cero si se especifica.")
    @Digits(integer = 8, fraction = 2, message = "El precio unitario debe tener máximo 8 dígitos enteros y 2 decimales si se especifica.")
    private BigDecimal precioUnitarioVenta; // Precio al momento de la venta

    // Nota: El subtotal (cantidadProducto * precioUnitarioVenta) generalmente se calcula
    // en la lógica de negocio del servicio y no se envía en la solicitud.

    // Constructores
    public DetalleOrdenVentaRequestDTO() {
    }

    public DetalleOrdenVentaRequestDTO(Integer idProducto, Integer cantidadProducto, BigDecimal precioUnitarioVenta) {
        this.idProducto = idProducto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    // Getters y Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidadProducto() { return cantidadProducto; }
    public void setCantidadProducto(Integer cantidadProducto) { this.cantidadProducto = cantidadProducto; }
    public BigDecimal getPrecioUnitarioVenta() { return precioUnitarioVenta; }
    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) { this.precioUnitarioVenta = precioUnitarioVenta; }
}