package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size; // Importar Size
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de actualización de una Orden de Compra existente. " +
        "Permite modificar fechas, estado y observaciones. Los detalles de la orden se actualizan por separado.")
public class OrdenCompraUpdateRequestDTO {

    // Nota: El proveedor de una orden de compra generalmente no se modifica después de su creación.
    // Si fuera necesario, requeriría una lógica de negocio y consideraciones de impacto más complejas.

    @Schema(description = "Nueva fecha estimada en la que se espera recibir los insumos de esta orden. No puede ser una fecha pasada.",
            example = "2024-07-01", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @FutureOrPresent(message = "La fecha de entrega estimada no puede ser pasada.")
    private LocalDate fechaEntregaEstimadaCompra;

    @Schema(description = "Fecha real en la que se recibieron completamente los insumos. Se actualiza al marcar la orden como 'Recibida Total'. No puede ser futura.",
            example = "2024-07-05", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de entrega real no puede ser futura.")
    private LocalDate fechaEntregaRealCompra;

    @Schema(description = "Nuevo estado de la orden de compra. Valores permitidos: 'Pendiente', 'Enviada', 'Recibida Parcial', 'Recibida Total', 'Anulada'.",
            example = "Enviada",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Pattern(regexp = "Pendiente|Enviada|Recibida Parcial|Recibida Total|Anulada", message = "Estado de compra inválido.")
    private String estadoCompra;

    @Schema(description = "Nuevas observaciones o notas adicionales para la orden de compra.",
            example = "Confirmada entrega con proveedor. Se espera para la fecha estimada.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesCompra;

    // Nota: La actualización de los detalles (ítems) de la orden de compra
    // usualmente se maneja a través de endpoints específicos para los detalles,
    // como POST /ordenes-compra/{id}/detalles, PUT /ordenes-compra/{id}/detalles/{idDetalle}, etc.,
    // y no directamente a través de este DTO de actualización de cabecera.

    // Constructores
    public OrdenCompraUpdateRequestDTO() {
    }

    public OrdenCompraUpdateRequestDTO(LocalDate fechaEntregaEstimadaCompra, LocalDate fechaEntregaRealCompra, String estadoCompra, String observacionesCompra) {
        this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra;
        this.fechaEntregaRealCompra = fechaEntregaRealCompra;
        this.estadoCompra = estadoCompra;
        this.observacionesCompra = observacionesCompra;
    }

    // Getters y Setters
    public LocalDate getFechaEntregaEstimadaCompra() { return fechaEntregaEstimadaCompra; }
    public void setFechaEntregaEstimadaCompra(LocalDate fechaEntregaEstimadaCompra) { this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra; }
    public LocalDate getFechaEntregaRealCompra() { return fechaEntregaRealCompra; }
    public void setFechaEntregaRealCompra(LocalDate fechaEntregaRealCompra) { this.fechaEntregaRealCompra = fechaEntregaRealCompra; }
    public String getEstadoCompra() { return estadoCompra; }
    public void setEstadoCompra(String estadoCompra) { this.estadoCompra = estadoCompra; }
    public String getObservacionesCompra() { return observacionesCompra; }
    public void setObservacionesCompra(String observacionesCompra) { this.observacionesCompra = observacionesCompra; }
}