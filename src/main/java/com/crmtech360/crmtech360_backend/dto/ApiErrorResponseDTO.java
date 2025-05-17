package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "DTO estándar para respuestas de error de la API. Proporciona detalles sobre errores HTTP.")
public class ApiErrorResponseDTO {

    @Schema(description = "La marca de tiempo exacta en la que ocurrió el error, en formato ISO.",
            example = "2024-05-15T10:30:45.123Z", // Ejemplo de formato ISO para LocalDateTime
            accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime timestamp;

    @Schema(description = "El código de estado HTTP numérico (ej. 400, 401, 403, 404, 500).",
            example = "404",
            accessMode = Schema.AccessMode.READ_ONLY)
    private int status;

    @Schema(description = "Una breve descripción textual del tipo de error HTTP (ej. 'Not Found', 'Bad Request', 'Unauthorized').",
            example = "Not Found",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String error;

    @Schema(description = "Un mensaje legible por humanos que describe la causa específica del error.",
            example = "El recurso solicitado con ID 999 no fue encontrado.",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String message;

    @Schema(description = "La ruta (endpoint) de la API que originó el error.",
            example = "/api/v1/clientes/999",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String path;

    @Schema(description = "Un mapa que contiene errores de validación específicos por campo, donde la clave es el nombre del campo y el valor es el mensaje de error de validación. Este campo es opcional y solo aparece si hay errores de validación.",
            nullable = true,
            accessMode = Schema.AccessMode.READ_ONLY)
    private Map<String, String> validationErrors;

    @Schema(description = "Una lista de cadenas que proporcionan detalles adicionales sobre el error o múltiples errores. Este campo es opcional.",
            nullable = true,
            accessMode = Schema.AccessMode.READ_ONLY)
    private List<String> details;

    // Constructores
    public ApiErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponseDTO(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ApiErrorResponseDTO(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this(status, error, message, path);
        this.validationErrors = validationErrors;
    }

    public ApiErrorResponseDTO(int status, String error, String message, String path, List<String> details) {
        this(status, error, message, path);
        this.details = details;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}