package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;

@Schema(description = "DTO resumido para representar información básica de una Orden de Compra, " +
        "usualmente utilizado cuando se vincula a otras entidades (ej. un Pago).")
public class OrdenCompraSummaryDTO {

    @Schema(description = "Identificador único de la orden de compra.", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenCompra;

    @Schema(description = "Fecha y hora en que se registró la orden de compra.", example = "2024-05-10T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaPedidoCompra;

    @Schema(description = "Nombre comercial del proveedor asociado a la orden de compra (para referencia rápida).", example = "Insumos Textiles Andinos S.A.", accessMode = Schema.AccessMode.READ_ONLY)
    private String proveedorNombre;

    // Constructores
    public OrdenCompraSummaryDTO() {
    }

    public OrdenCompraSummaryDTO(Integer idOrdenCompra, LocalDateTime fechaPedidoCompra, String proveedorNombre) {
        this.idOrdenCompra = idOrdenCompra;
        this.fechaPedidoCompra = fechaPedidoCompra;
        this.proveedorNombre = proveedorNombre;
    }

    // Getters y Setters
    public Integer getIdOrdenCompra() { return idOrdenCompra; }
    public void setIdOrdenCompra(Integer idOrdenCompra) { this.idOrdenCompra = idOrdenCompra; }
    public LocalDateTime getFechaPedidoCompra() { return fechaPedidoCompra; }
    public void setFechaPedidoCompra(LocalDateTime fechaPedidoCompra) { this.fechaPedidoCompra = fechaPedidoCompra; }
    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }
}