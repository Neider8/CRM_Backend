package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;

@Schema(description = "DTO para la respuesta de información detallada de un ítem (detalle) dentro de una orden de compra.")
public class DetalleOrdenCompraResponseDTO {

    @Schema(description = "Identificador único del detalle de la orden de compra.", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idDetalleCompra;

    @Schema(description = "Información resumida del insumo comprado.", accessMode = Schema.AccessMode.READ_ONLY)
    private InsumoSummaryDTO insumo; // Asegúrate que InsumoSummaryDTO esté anotado con @Schema

    @Schema(description = "Cantidad del insumo comprada en este detalle.", example = "100", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer cantidadCompra;

    @Schema(description = "Precio unitario al que se compró el insumo en este detalle.", example = "25.50", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precioUnitarioCompra;

    @Schema(description = "Subtotal para este detalle de la orden de compra (cantidadCompra * precioUnitarioCompra).", example = "2550.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal subtotalCompra;

    // Constructores
    public DetalleOrdenCompraResponseDTO() {
    }

    public DetalleOrdenCompraResponseDTO(Integer idDetalleCompra, InsumoSummaryDTO insumo, Integer cantidadCompra, BigDecimal precioUnitarioCompra, BigDecimal subtotalCompra) {
        this.idDetalleCompra = idDetalleCompra;
        this.insumo = insumo;
        this.cantidadCompra = cantidadCompra;
        this.precioUnitarioCompra = precioUnitarioCompra;
        this.subtotalCompra = subtotalCompra;
    }

    // Getters y Setters
    public Integer getIdDetalleCompra() { return idDetalleCompra; }
    public void setIdDetalleCompra(Integer idDetalleCompra) { this.idDetalleCompra = idDetalleCompra; }
    public InsumoSummaryDTO getInsumo() { return insumo; }
    public void setInsumo(InsumoSummaryDTO insumo) { this.insumo = insumo; }
    public Integer getCantidadCompra() { return cantidadCompra; }
    public void setCantidadCompra(Integer cantidadCompra) { this.cantidadCompra = cantidadCompra; }
    public BigDecimal getPrecioUnitarioCompra() { return precioUnitarioCompra; }
    public void setPrecioUnitarioCompra(BigDecimal precioUnitarioCompra) { this.precioUnitarioCompra = precioUnitarioCompra; }
    public BigDecimal getSubtotalCompra() { return subtotalCompra; }
    public void setSubtotalCompra(BigDecimal subtotalCompra) { this.subtotalCompra = subtotalCompra; }
}