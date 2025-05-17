package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;
import java.time.LocalTime;
// Asumo que EmpleadoSummaryDTO ya está definido y anotado
// import com.crmtech360.crmtech360_backend.dto.EmpleadoSummaryDTO;

@Schema(description = "DTO para la respuesta de información detallada de una Tarea de Producción.")
public class TareaProduccionResponseDTO {

    @Schema(description = "Identificador único de la tarea de producción.", example = "901", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idTareaProduccion;

    @Schema(description = "Identificador único de la Orden de Producción a la que pertenece esta tarea.", example = "801", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenProduccion;

    @Schema(description = "Información resumida del empleado asignado a esta tarea. Puede ser nulo si la tarea no está asignada.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private EmpleadoSummaryDTO empleado; // Asegúrate que EmpleadoSummaryDTO esté anotado

    @Schema(description = "Nombre o descripción breve de la tarea.", example = "Corte de Piezas Delanteras", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreTarea;

    @Schema(description = "Fecha y hora real de inicio de la tarea.", example = "2024-06-03T08:00:00", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaInicioTarea;

    @Schema(description = "Fecha y hora real de finalización de la tarea.", example = "2024-06-03T10:30:00", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaFinTarea;

    @Schema(description = "Duración estimada para completar la tarea (formato HH:MM:SS).", example = "02:30:00", type = "string", format = "time", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalTime duracionEstimadaTarea;

    @Schema(description = "Duración real que tomó completar la tarea (formato HH:MM:SS). Se calcula al finalizar la tarea.", example = "02:25:00", type = "string", format = "time", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalTime duracionRealTarea;

    @Schema(description = "Estado actual de la tarea (ej. 'Pendiente', 'En Progreso', 'Completada', 'Bloqueada').", example = "En Progreso", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoTarea;

    @Schema(description = "Observaciones o notas adicionales sobre la tarea.", example = "Realizado con patrón v2. Sin inconvenientes.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String observacionesTarea;

    // Constructores
    public TareaProduccionResponseDTO() {
    }

    public TareaProduccionResponseDTO(Integer idTareaProduccion, Integer idOrdenProduccion, EmpleadoSummaryDTO empleado, String nombreTarea, LocalDateTime fechaInicioTarea, LocalDateTime fechaFinTarea, LocalTime duracionEstimadaTarea, LocalTime duracionRealTarea, String estadoTarea, String observacionesTarea) {
        this.idTareaProduccion = idTareaProduccion;
        this.idOrdenProduccion = idOrdenProduccion;
        this.empleado = empleado;
        this.nombreTarea = nombreTarea;
        this.fechaInicioTarea = fechaInicioTarea;
        this.fechaFinTarea = fechaFinTarea;
        this.duracionEstimadaTarea = duracionEstimadaTarea;
        this.duracionRealTarea = duracionRealTarea;
        this.estadoTarea = estadoTarea;
        this.observacionesTarea = observacionesTarea;
    }

    // Getters y Setters
    public Integer getIdTareaProduccion() { return idTareaProduccion; }
    public void setIdTareaProduccion(Integer idTareaProduccion) { this.idTareaProduccion = idTareaProduccion; }
    public Integer getIdOrdenProduccion() { return idOrdenProduccion; }
    public void setIdOrdenProduccion(Integer idOrdenProduccion) { this.idOrdenProduccion = idOrdenProduccion; }
    public EmpleadoSummaryDTO getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoSummaryDTO empleado) { this.empleado = empleado; }
    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }
    public LocalDateTime getFechaInicioTarea() { return fechaInicioTarea; }
    public void setFechaInicioTarea(LocalDateTime fechaInicioTarea) { this.fechaInicioTarea = fechaInicioTarea; }
    public LocalDateTime getFechaFinTarea() { return fechaFinTarea; }
    public void setFechaFinTarea(LocalDateTime fechaFinTarea) { this.fechaFinTarea = fechaFinTarea; }
    public LocalTime getDuracionEstimadaTarea() { return duracionEstimadaTarea; }
    public void setDuracionEstimadaTarea(LocalTime duracionEstimadaTarea) { this.duracionEstimadaTarea = duracionEstimadaTarea; }
    public LocalTime getDuracionRealTarea() { return duracionRealTarea; }
    public void setDuracionRealTarea(LocalTime duracionRealTarea) { this.duracionRealTarea = duracionRealTarea; }
    public String getEstadoTarea() { return estadoTarea; }
    public void setEstadoTarea(String estadoTarea) { this.estadoTarea = estadoTarea; }
    public String getObservacionesTarea() { return observacionesTarea; }
    public void setObservacionesTarea(String observacionesTarea) { this.observacionesTarea = observacionesTarea; }
}