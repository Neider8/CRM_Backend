package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;

@Schema(description = "DTO para la respuesta de información detallada de un ítem (producto) dentro de una orden de venta.")
public class DetalleOrdenVentaResponseDTO {

    @Schema(description = "Identificador único del detalle de la orden de venta.", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idDetalleOrden;

    @Schema(description = "Información resumida del producto vendido.", accessMode = Schema.AccessMode.READ_ONLY)
    private ProductoSummaryDTO producto; // Asegúrate que ProductoSummaryDTO esté anotado con @Schema

    @Schema(description = "Cantidad del producto vendida en este detalle.", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cantidadProducto;

    @Schema(description = "Precio unitario al que se vendió el producto en este detalle.", example = "150.75", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precioUnitarioVenta;

    @Schema(description = "Subtotal para este detalle de la orden de venta (cantidadProducto * precioUnitarioVenta).", example = "753.75", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal subtotalDetalle;

    // Constructores
    public DetalleOrdenVentaResponseDTO() {
    }

    public DetalleOrdenVentaResponseDTO(Integer idDetalleOrden, ProductoSummaryDTO producto, Integer cantidadProducto, BigDecimal precioUnitarioVenta, BigDecimal subtotalDetalle) {
        this.idDetalleOrden = idDetalleOrden;
        this.producto = producto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitarioVenta = precioUnitarioVenta;
        this.subtotalDetalle = subtotalDetalle;
    }

    // Getters y Setters
    public Integer getIdDetalleOrden() { return idDetalleOrden; }
    public void setIdDetalleOrden(Integer idDetalleOrden) { this.idDetalleOrden = idDetalleOrden; }
    public ProductoSummaryDTO getProducto() { return producto; }
    public void setProducto(ProductoSummaryDTO producto) { this.producto = producto; }
    public Integer getCantidadProducto() { return cantidadProducto; }
    public void setCantidadProducto(Integer cantidadProducto) { this.cantidadProducto = cantidadProducto; }
    public BigDecimal getPrecioUnitarioVenta() { return precioUnitarioVenta; }
    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) { this.precioUnitarioVenta = precioUnitarioVenta; }
    public BigDecimal getSubtotalDetalle() { return subtotalDetalle; }
    public void setSubtotalDetalle(BigDecimal subtotalDetalle) { this.subtotalDetalle = subtotalDetalle; }
}