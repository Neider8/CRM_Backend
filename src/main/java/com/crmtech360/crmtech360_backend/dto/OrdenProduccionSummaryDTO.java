package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
// No se necesitan imports de java.time aquí si no hay campos de fecha

@Schema(description = "DTO resumido para representar información básica de una Orden de Producción, " +
        "usualmente utilizado cuando se vincula a otras entidades (ej. una Tarea de Producción).")
public class OrdenProduccionSummaryDTO {

    @Schema(description = "Identificador único de la orden de producción.", example = "801", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenProduccion;

    @Schema(description = "Estado actual de la orden de producción (ej. 'Pendiente', 'En Progreso', 'Terminada').", example = "En Progreso", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoProduccion;

    @Schema(description = "Identificador único de la Orden de Venta que originó esta Orden de Producción (para referencia rápida).", example = "305", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenVenta;

    // Constructores
    public OrdenProduccionSummaryDTO() {
    }

    public OrdenProduccionSummaryDTO(Integer idOrdenProduccion, String estadoProduccion, Integer idOrdenVenta) {
        this.idOrdenProduccion = idOrdenProduccion;
        this.estadoProduccion = estadoProduccion;
        this.idOrdenVenta = idOrdenVenta;
    }

    // Getters y Setters
    public Integer getIdOrdenProduccion() { return idOrdenProduccion; }
    public void setIdOrdenProduccion(Integer idOrdenProduccion) { this.idOrdenProduccion = idOrdenProduccion; }
    public String getEstadoProduccion() { return estadoProduccion; }
    public void setEstadoProduccion(String estadoProduccion) { this.estadoProduccion = estadoProduccion; }
    public Integer getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Integer idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
}