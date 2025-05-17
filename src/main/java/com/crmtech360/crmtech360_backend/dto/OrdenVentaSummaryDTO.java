package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;

@Schema(description = "DTO resumido para representar información básica de una Orden de Venta, " +
        "usualmente utilizado cuando se vincula a otras entidades (ej. una Orden de Producción o un Pago/Cobro).")
public class OrdenVentaSummaryDTO {

    @Schema(description = "Identificador único de la orden de venta.", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenVenta;

    @Schema(description = "Fecha y hora en que se registró la orden de venta.", example = "2024-05-01T11:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaPedido;

    @Schema(description = "Nombre del cliente asociado a la orden de venta (para referencia rápida).", example = "Telas del Pacífico S.A.S.", accessMode = Schema.AccessMode.READ_ONLY)
    private String clienteNombre;

    // Constructores
    public OrdenVentaSummaryDTO() {
    }

    public OrdenVentaSummaryDTO(Integer idOrdenVenta, LocalDateTime fechaPedido, String clienteNombre) {
        this.idOrdenVenta = idOrdenVenta;
        this.fechaPedido = fechaPedido;
        this.clienteNombre = clienteNombre;
    }

    // Getters y Setters
    public Integer getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Integer idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
}