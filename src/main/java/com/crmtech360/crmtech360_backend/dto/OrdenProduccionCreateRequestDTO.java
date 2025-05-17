package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Pattern; // No se usa Pattern aquí directamente
import jakarta.validation.constraints.Size; // Importar Size si es necesario para observacionesProduccion
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de creación de una nueva Orden de Producción.")
public class OrdenProduccionCreateRequestDTO {

    @Schema(description = "Identificador único de la Orden de Venta que origina esta Orden de Producción. Es mandatorio.",
            example = "305",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la Orden de Venta asociada es obligatorio.")
    private Integer idOrdenVenta;

    @Schema(description = "Fecha planificada para el inicio de la producción. Puede ser opcional al crear la orden.",
            example = "2024-06-01", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    // No se requiere @PastOrPresent o @FutureOrPresent aquí necesariamente, depende de la lógica de negocio.
    private LocalDate fechaInicioProduccion;

    @Schema(description = "Fecha estimada para la finalización de la producción. No puede ser una fecha pasada. Puede ser opcional al crear la orden.",
            example = "2024-06-15", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @FutureOrPresent(message = "La fecha de fin estimada no puede ser pasada si se especifica.")
    private LocalDate fechaFinEstimadaProduccion;

    // El estado inicial (ej. 'Pendiente', 'Planificada') generalmente se asigna
    // automáticamente por la lógica de negocio en el servicio al crear la orden.

    @Schema(description = "Observaciones o notas adicionales para la orden de producción.",
            example = "Producir con urgencia. Lote de tela especial reservado.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesProduccion;

    // Constructores
    public OrdenProduccionCreateRequestDTO() {
    }

    public OrdenProduccionCreateRequestDTO(Integer idOrdenVenta, LocalDate fechaInicioProduccion, LocalDate fechaFinEstimadaProduccion, String observacionesProduccion) {
        this.idOrdenVenta = idOrdenVenta;
        this.fechaInicioProduccion = fechaInicioProduccion;
        this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion;
        this.observacionesProduccion = observacionesProduccion;
    }

    // Getters y Setters
    public Integer getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Integer idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public LocalDate getFechaInicioProduccion() { return fechaInicioProduccion; }
    public void setFechaInicioProduccion(LocalDate fechaInicioProduccion) { this.fechaInicioProduccion = fechaInicioProduccion; }
    public LocalDate getFechaFinEstimadaProduccion() { return fechaFinEstimadaProduccion; }
    public void setFechaFinEstimadaProduccion(LocalDate fechaFinEstimadaProduccion) { this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion; }
    public String getObservacionesProduccion() { return observacionesProduccion; }
    public void setObservacionesProduccion(String observacionesProduccion) { this.observacionesProduccion = observacionesProduccion; }
}