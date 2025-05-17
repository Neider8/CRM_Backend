package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

@Schema(description = "DTO para la solicitud de creación de una nueva Tarea de Producción asociada a una Orden de Producción.")
public class TareaProduccionCreateRequestDTO {

    @Schema(description = "Identificador único de la Orden de Producción a la que pertenece esta tarea. Es mandatorio.",
            example = "801",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la Orden de Producción es obligatorio.")
    private Integer idOrdenProduccion;

    @Schema(description = "Identificador único del Empleado asignado a esta tarea. Puede ser opcional al crear la tarea y asignarse después.",
            example = "12",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private Integer idEmpleado;

    @Schema(description = "Nombre o descripción breve de la tarea a realizar (ej. 'Corte de Piezas Delanteras', 'Costura de Mangas', 'Control de Calidad Final').",
            example = "Corte de Piezas Delanteras",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de la tarea no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la tarea no puede exceder los 100 caracteres.")
    private String nombreTarea;

    @Schema(description = "Duración estimada para completar la tarea, en formato HH:MM o HH:MM:SS. (ej. '02:30' para 2 horas 30 minutos).",
            example = "02:30:00", // ISO-8601 time format
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true,
            type = "string", format = "time") // Indica a Swagger que es un tiempo
    private LocalTime duracionEstimadaTarea;

    // El estado inicial (ej. 'Pendiente') generalmente se asigna automáticamente por la lógica de negocio.

    @Schema(description = "Observaciones o instrucciones adicionales para la tarea de producción.",
            example = "Utilizar patrón de corte v2. Verificar calidad de tela antes de cortar.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres.") // Ajusta el max si es necesario
    private String observacionesTarea;

    // Constructores
    public TareaProduccionCreateRequestDTO() {
    }

    public TareaProduccionCreateRequestDTO(Integer idOrdenProduccion, Integer idEmpleado, String nombreTarea, LocalTime duracionEstimadaTarea, String observacionesTarea) {
        this.idOrdenProduccion = idOrdenProduccion;
        this.idEmpleado = idEmpleado;
        this.nombreTarea = nombreTarea;
        this.duracionEstimadaTarea = duracionEstimadaTarea;
        this.observacionesTarea = observacionesTarea;
    }

    // Getters y Setters
    public Integer getIdOrdenProduccion() { return idOrdenProduccion; }
    public void setIdOrdenProduccion(Integer idOrdenProduccion) { this.idOrdenProduccion = idOrdenProduccion; }
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }
    public LocalTime getDuracionEstimadaTarea() { return duracionEstimadaTarea; }
    public void setDuracionEstimadaTarea(LocalTime duracionEstimadaTarea) { this.duracionEstimadaTarea = duracionEstimadaTarea; }
    public String getObservacionesTarea() { return observacionesTarea; }
    public void setObservacionesTarea(String observacionesTarea) { this.observacionesTarea = observacionesTarea; }
}