package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size; // Importar Size
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de actualización de una Orden de Venta existente. " +
        "Permite modificar la fecha de entrega estimada, el estado y las observaciones. " +
        "Los detalles de la orden (ítems) se actualizan por separado.")
public class OrdenVentaUpdateRequestDTO {

    // Nota: El cliente de una orden de venta generalmente no se modifica después de su creación.

    @Schema(description = "Nueva fecha estimada en la que se espera entregar los productos al cliente. No puede ser una fecha pasada.",
            example = "2024-07-20", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @FutureOrPresent(message = "La fecha de entrega estimada no puede ser pasada si se especifica.")
    private LocalDate fechaEntregaEstimada;

    @Schema(description = "Nuevo estado de la orden de venta. Valores permitidos: 'Pendiente', 'Confirmada', 'En Producción', 'Entregada', 'Anulada'.",
            example = "En Producción",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Pattern(regexp = "Pendiente|Confirmada|En Producción|Entregada|Anulada", message = "Estado de orden inválido.")
    private String estadoOrden;

    @Schema(description = "Nuevas observaciones o notas adicionales para la orden de venta.",
            example = "Cliente solicitó adelantar entrega si es posible.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesOrden;

    // Nota: La actualización de los detalles (ítems) de la orden de venta
    // usualmente se maneja a través de endpoints específicos para los detalles
    // (ej. POST /ordenes-venta/{id}/detalles, PUT /ordenes-venta/{id}/detalles/{idDetalle}),
    // y no directamente a través de este DTO de actualización de cabecera.

    // Constructores
    public OrdenVentaUpdateRequestDTO() {
    }

    public OrdenVentaUpdateRequestDTO(LocalDate fechaEntregaEstimada, String estadoOrden, String observacionesOrden) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estadoOrden = estadoOrden;
        this.observacionesOrden = observacionesOrden;
    }

    // Getters y Setters
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(String estadoOrden) { this.estadoOrden = estadoOrden; }
    public String getObservacionesOrden() { return observacionesOrden; }
    public void setObservacionesOrden(String observacionesOrden) { this.observacionesOrden = observacionesOrden; }
}