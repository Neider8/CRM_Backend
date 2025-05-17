package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "DTO para la solicitud de actualización de una Tarea de Producción existente. " +
        "Todos los campos son opcionales; solo se actualizarán los campos proporcionados. " +
        "La orden de producción asociada no se puede cambiar por esta vía.")
public class TareaProduccionUpdateRequestDTO {

    // Nota: La orden de producción asociada (idOrdenProduccion) generalmente no se modifica
    // una vez que la tarea ha sido creada y vinculada.

    @Schema(description = "Nuevo identificador único del Empleado asignado a esta tarea. " +
            "Permite reasignar la tarea o asignarla si estaba sin empleado.",
            example = "15",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private Integer idEmpleado;

    @Schema(description = "Nuevo nombre o descripción breve de la tarea.",
            example = "Corte de Piezas Delanteras (Revisado)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 100, message = "El nombre de la tarea no puede exceder los 100 caracteres.")
    private String nombreTarea;

    @Schema(description = "Nueva fecha y hora real de inicio de la tarea.",
            example = "2024-06-03T08:15:00", // Formato ISO 8601
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de inicio no puede ser futura si se especifica.")
    private LocalDateTime fechaInicioTarea;

    @Schema(description = "Nueva fecha y hora real de finalización de la tarea.",
            example = "2024-06-03T10:45:00", // Formato ISO 8601
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de fin no puede ser futura si se especifica.")
    private LocalDateTime fechaFinTarea;

    @Schema(description = "Nueva duración estimada para completar la tarea (formato HH:MM:SS).",
            example = "02:00:00",
            type = "string", format = "time",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private LocalTime duracionEstimadaTarea;

    @Schema(description = "Nueva duración real que tomó completar la tarea (formato HH:MM:SS). Se actualiza al completar la tarea.",
            example = "02:15:30",
            type = "string", format = "time",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private LocalTime duracionRealTarea;

    @Schema(description = "Nuevo estado de la tarea. Valores permitidos: 'Pendiente', 'En Curso', 'Completada', 'Bloqueada'.",
            example = "Completada",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Pattern(regexp = "Pendiente|En Curso|Completada|Bloqueada", message = "Estado de tarea inválido.")
    private String estadoTarea;

    @Schema(description = "Nuevas observaciones o notas adicionales sobre la tarea.",
            example = "Tarea finalizada. Material sobrante devuelto a bodega.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres.") // Ajusta el max si es necesario
    private String observacionesTarea;

    // Constructores
    public TareaProduccionUpdateRequestDTO() {
    }

    public TareaProduccionUpdateRequestDTO(Integer idEmpleado, String nombreTarea, LocalDateTime fechaInicioTarea, LocalDateTime fechaFinTarea, LocalTime duracionEstimadaTarea, LocalTime duracionRealTarea, String estadoTarea, String observacionesTarea) {
        this.idEmpleado = idEmpleado;
        this.nombreTarea = nombreTarea;
        this.fechaInicioTarea = fechaInicioTarea;
        this.fechaFinTarea = fechaFinTarea;
        this.duracionEstimadaTarea = duracionEstimadaTarea;
        this.duracionRealTarea = duracionRealTarea;
        this.estadoTarea = estadoTarea;
        this.observacionesTarea = observacionesTarea;
    }

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
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