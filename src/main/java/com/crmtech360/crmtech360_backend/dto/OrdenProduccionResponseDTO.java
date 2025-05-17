package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO para la respuesta de información detallada de una Orden de Producción, " +
        "incluyendo la orden de venta asociada y la lista de tareas de producción.")
public class OrdenProduccionResponseDTO {

    @Schema(description = "Identificador único de la orden de producción.", example = "801", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenProduccion;

    @Schema(description = "Información resumida de la orden de venta que originó esta orden de producción.", accessMode = Schema.AccessMode.READ_ONLY)
    private OrdenVentaSummaryDTO ordenVenta; // Asegúrate que OrdenVentaSummaryDTO esté anotado

    @Schema(description = "Fecha y hora en que se registró la orden de producción en el sistema.", example = "2024-05-15T14:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha planificada para el inicio de la producción.", example = "2024-06-01", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaInicioProduccion;

    @Schema(description = "Fecha estimada para la finalización de la producción.", example = "2024-06-15", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaFinEstimadaProduccion;

    @Schema(description = "Fecha real en la que se completó la producción. Será nula si la orden no ha finalizado.", example = "2024-06-14", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaFinRealProduccion;

    @Schema(description = "Estado actual de la orden de producción (ej. 'Pendiente', 'En Progreso', 'Terminada', 'Anulada').", example = "En Progreso", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoProduccion;

    @Schema(description = "Observaciones o notas adicionales para la orden de producción.", example = "Producir con urgencia. Lote de tela especial reservado.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String observacionesProduccion;

    @Schema(description = "Fecha y hora de la última actualización de este registro de orden de producción.", example = "2024-05-16T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Lista de tareas de producción asociadas a esta orden.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    @ArraySchema(schema = @Schema(implementation = TareaProduccionResponseDTO.class))
    private List<TareaProduccionResponseDTO> tareas; // Asegúrate que TareaProduccionResponseDTO esté anotado

    // Constructores
    public OrdenProduccionResponseDTO() {
    }

    public OrdenProduccionResponseDTO(Integer idOrdenProduccion, OrdenVentaSummaryDTO ordenVenta, LocalDateTime fechaCreacion, LocalDate fechaInicioProduccion, LocalDate fechaFinEstimadaProduccion, LocalDate fechaFinRealProduccion, String estadoProduccion, String observacionesProduccion, LocalDateTime fechaActualizacion, List<TareaProduccionResponseDTO> tareas) {
        this.idOrdenProduccion = idOrdenProduccion;
        this.ordenVenta = ordenVenta;
        this.fechaCreacion = fechaCreacion;
        this.fechaInicioProduccion = fechaInicioProduccion;
        this.fechaFinEstimadaProduccion = fechaFinEstimadaProduccion;
        this.fechaFinRealProduccion = fechaFinRealProduccion;
        this.estadoProduccion = estadoProduccion;
        this.observacionesProduccion = observacionesProduccion;
        this.fechaActualizacion = fechaActualizacion;
        this.tareas = tareas;
    }

    // Getters y Setters
    public Integer getIdOrdenProduccion() { return idOrdenProduccion; }
    public void setIdOrdenProduccion(Integer idOrdenProduccion) { this.idOrdenProduccion = idOrdenProduccion; }
    public OrdenVentaSummaryDTO getOrdenVenta() { return ordenVenta; }
    public void setOrdenVenta(OrdenVentaSummaryDTO ordenVenta) { this.ordenVenta = ordenVenta; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
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
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public List<TareaProduccionResponseDTO> getTareas() { return tareas; }
    public void setTareas(List<TareaProduccionResponseDTO> tareas) { this.tareas = tareas; }
}