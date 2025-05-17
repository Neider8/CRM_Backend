package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de actualización de un empleado existente. " +
        "Todos los campos son opcionales; solo se actualizarán los campos proporcionados.")
public class EmpleadoUpdateRequestDTO {

    // tipoDocumento y numeroDocumento generalmente no se actualizan
    // o se manejan a través de un proceso más específico si es necesario.

    @Schema(description = "Nuevo nombre completo del empleado. Si se proporciona, reemplazará el nombre existente.",
            example = "Carlos Alberto Rodríguez Gómez",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El nombre del empleado no puede exceder los 255 caracteres.")
    private String nombreEmpleado;

    @Schema(description = "Nuevo cargo o posición del empleado en la empresa.",
            example = "Supervisor de Confección",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 100, message = "El cargo del empleado no puede exceder los 100 caracteres.")
    private String cargoEmpleado;

    @Schema(description = "Nueva área o departamento al que pertenece el empleado.",
            example = "Producción",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El área del empleado no puede exceder los 50 caracteres.")
    private String areaEmpleado;

    @Schema(description = "Nuevo salario base del empleado. No puede ser negativo y puede tener hasta 2 decimales.",
            example = "2200000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @DecimalMin(value = "0.0", message = "El salario no puede ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "El salario debe tener máximo 8 dígitos enteros y 2 decimales.")
    private BigDecimal salarioEmpleado;

    @Schema(description = "Nueva fecha en que el empleado fue contratado. No puede ser una fecha futura.",
            example = "2022-08-15", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de contratación no puede ser futura.")
    private LocalDate fechaContratacionEmpleado;

    // Constructores
    public EmpleadoUpdateRequestDTO() {
    }

    public EmpleadoUpdateRequestDTO(String nombreEmpleado, String cargoEmpleado, String areaEmpleado, BigDecimal salarioEmpleado, LocalDate fechaContratacionEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
        this.cargoEmpleado = cargoEmpleado;
        this.areaEmpleado = areaEmpleado;
        this.salarioEmpleado = salarioEmpleado;
        this.fechaContratacionEmpleado = fechaContratacionEmpleado;
    }

    // Getters y Setters
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
    public String getCargoEmpleado() { return cargoEmpleado; }
    public void setCargoEmpleado(String cargoEmpleado) { this.cargoEmpleado = cargoEmpleado; }
    public String getAreaEmpleado() { return areaEmpleado; }
    public void setAreaEmpleado(String areaEmpleado) { this.areaEmpleado = areaEmpleado; }
    public BigDecimal getSalarioEmpleado() { return salarioEmpleado; }
    public void setSalarioEmpleado(BigDecimal salarioEmpleado) { this.salarioEmpleado = salarioEmpleado; }
    public LocalDate getFechaContratacionEmpleado() { return fechaContratacionEmpleado; }
    public void setFechaContratacionEmpleado(LocalDate fechaContratacionEmpleado) { this.fechaContratacionEmpleado = fechaContratacionEmpleado; }
}