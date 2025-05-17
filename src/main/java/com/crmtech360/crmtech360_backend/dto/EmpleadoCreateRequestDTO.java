package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de creación de un nuevo empleado.")
public class EmpleadoCreateRequestDTO {

    @Schema(description = "Tipo de documento de identificación del empleado. Valores permitidos: 'Cédula' u 'Otro'.",
            example = "Cédula",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de documento no puede estar vacío.")
    @Pattern(regexp = "Cédula|Otro", message = "El tipo de documento debe ser Cédula u Otro.")
    private String tipoDocumento;

    @Schema(description = "Número único del documento de identificación del empleado. Debe ser único en el sistema.",
            example = "1020304050",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El número de documento no puede estar vacío.")
    @Size(max = 20, message = "El número de documento no puede exceder los 20 caracteres.")
    private String numeroDocumento;

    @Schema(description = "Nombre completo del empleado.",
            example = "Carlos Alberto Rodriguez",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del empleado no puede estar vacío.")
    @Size(max = 255, message = "El nombre del empleado no puede exceder los 255 caracteres.")
    private String nombreEmpleado;

    @Schema(description = "Cargo o posición que ocupa el empleado en la empresa.",
            example = "Operario Máquina Plana",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 100, message = "El cargo del empleado no puede exceder los 100 caracteres.")
    private String cargoEmpleado;

    @Schema(description = "Área o departamento al que pertenece el empleado.",
            example = "Confección",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 50, message = "El área del empleado no puede exceder los 50 caracteres.")
    private String areaEmpleado;

    @Schema(description = "Salario base del empleado. No puede ser negativo y puede tener hasta 2 decimales.",
            example = "1800000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional en la creación, puede tener un default o ser parte de otro proceso
            nullable = true)
    @DecimalMin(value = "0.0", message = "El salario no puede ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "El salario debe tener máximo 8 dígitos enteros y 2 decimales.")
    private BigDecimal salarioEmpleado;

    @Schema(description = "Fecha en que el empleado fue contratado. No puede ser una fecha futura.",
            example = "2023-06-01", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @PastOrPresent(message = "La fecha de contratación no puede ser futura.")
    private LocalDate fechaContratacionEmpleado;

    // Constructores
    public EmpleadoCreateRequestDTO() {
    }

    public EmpleadoCreateRequestDTO(String tipoDocumento, String numeroDocumento, String nombreEmpleado, String cargoEmpleado, String areaEmpleado, BigDecimal salarioEmpleado, LocalDate fechaContratacionEmpleado) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreEmpleado = nombreEmpleado;
        this.cargoEmpleado = cargoEmpleado;
        this.areaEmpleado = areaEmpleado;
        this.salarioEmpleado = salarioEmpleado;
        this.fechaContratacionEmpleado = fechaContratacionEmpleado;
    }

    // Getters y Setters
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
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