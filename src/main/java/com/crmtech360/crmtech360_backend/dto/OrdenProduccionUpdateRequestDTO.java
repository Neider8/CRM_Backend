package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size; // Importar Size
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de actualización de una Orden de Producción existente. " +
        "Permite modificar fechas, estado y observaciones. La orden de venta asociada no se puede cambiar.")
public class OrdenProduccionUpdateRequestDTO {

    // Nota: La orden de venta asociada a una orden de producción generalmente no se modifica después de su creación.

    @Schema(description = "Nueva fecha planificada para el inicio de la producción.",
            example = "2024-06-05", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de inicio no puede ser futura si se especifica.") // O permitir futura si la lógica de negocio lo contempla
    private LocalDate fechaInicioProduccion;

    @Schema(description = "Nueva fecha estimada para la finalización de la producción. No puede ser una fecha pasada.",
            example = "2024-06-20", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @FutureOrPresent(message = "La fecha de fin estimada no puede ser pasada si se especifica.")
    private LocalDate fechaFinEstimadaProduccion;

    @Schema(description = "Fecha real en la que se completó la producción. Se actualiza al marcar la orden como 'Terminada'. No puede ser futura.",
            example = "2024-06-18", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de fin real no puede ser futura si se especifica.")
    private LocalDate fechaFinRealProduccion;

    @Schema(description = "Nuevo estado de la orden de producción. Valores permitidos: 'Pendiente', 'En Proceso', 'Terminada', 'Retrasada', 'Anulada'.",
            example = "En Proceso",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Pattern(regexp = "Pendiente|En Proceso|Terminada|Retrasada|Anulada", message = "Estado de producción inválido.")
    private String estadoProduccion;

    @Schema(description = "Nuevas observaciones o notas adicionales para la orden de producción.",
            example = "Prioridad ajustada. Se han añadido recursos adicionales.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesProduccion;

    // Constructores
    public OrdenProduccionUpdateRequestDTO() {
    }

    public OrdenProduccionUpdateRequestDTO(LocalDate fechaInicioProduccion, LocalDate fechaFinEstimadaProduccion, LocalDate fechaFinRealProduccion, String estadoProduccion, String observacionesProduccion) {
        this.fechaInicioProduccion = fechaInicioProduccion;
        this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion;
        this.fechaFinRealProduccion = fechaFinRealProduccion;
        this.estadoProduccion = estadoProduccion;
        this.observacionesProduccion = observacionesProduccion;
    }

    // Getters y Setters
    public LocalDate getFechaInicioProduccion() { return fechaInicioProduccion; }
    public void setFechaInicioProduccion(LocalDate fechaInicioProduccion) { this.fechaInicioProduccion = fechaInicioProduccion; }
    public LocalDate getFechaFinEstimadaProduccion() { return fechaFinEstimadaProduccion; }
    public void setFechaFinEstimadaProduccion(LocalDate fechaFinEstimadaProduccion) { this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion; }
    public LocalDate getFechaFinRealProduccion() { return fechaFinRealProduccion; }
    public void setFechaFinRealProduccion(LocalDate fechaFinRealProduccion) { this.fechaFinRealProduccion = fechaFinRealProduccion; }
    public String getEstadoProduccion() { return estadoProduccion; }
    public void setEstadoProduccion(String estadoProduccion) { this.estadoProduccion = estadoProduccion; }
    public String getObservacionesProduccion() { return observacionesProduccion; }
    public void setObservacionesProduccion(String observacionesProduccion) { this.observacionesProduccion = observacionesProduccion; }
}