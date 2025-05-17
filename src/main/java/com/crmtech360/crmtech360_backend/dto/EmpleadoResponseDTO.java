package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de información detallada de un empleado.")
public class EmpleadoResponseDTO {

    @Schema(description = "Identificador único del empleado.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEmpleado;

    @Schema(description = "Tipo de documento de identificación del empleado (ej. 'Cédula', 'Otro').", example = "Cédula", accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoDocumento;

    @Schema(description = "Número único del documento de identificación del empleado.", example = "1020304050", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroDocumento;

    @Schema(description = "Nombre completo del empleado.", example = "Carlos Alberto Rodriguez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreEmpleado;

    @Schema(description = "Cargo o posición que ocupa el empleado en la empresa.", example = "Operario Máquina Plana", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String cargoEmpleado;

    @Schema(description = "Área o departamento al que pertenece el empleado.", example = "Confección", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String areaEmpleado;

    @Schema(description = "Salario base del empleado.", example = "1800000.00", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal salarioEmpleado;

    @Schema(description = "Fecha en que el empleado fue contratado (formato YYYY-MM-DD).", example = "2023-06-01", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaContratacionEmpleado;

    @Schema(description = "Fecha y hora de creación del registro del empleado en el sistema.", example = "2023-06-01T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización del registro del empleado.", example = "2024-01-20T11:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Información resumida de la cuenta de usuario asociada a este empleado, si existe.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private UsuarioSummaryDTO usuario; // Asegúrate que UsuarioSummaryDTO esté anotado con @Schema

    // Constructores
    public EmpleadoResponseDTO() {
    }

    public EmpleadoResponseDTO(Integer idEmpleado, String tipoDocumento, String numeroDocumento, String nombreEmpleado, String cargoEmpleado, String areaEmpleado, BigDecimal salarioEmpleado, LocalDate fechaContratacionEmpleado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, UsuarioSummaryDTO usuario) {
        this.idEmpleado = idEmpleado;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreEmpleado = nombreEmpleado;
        this.cargoEmpleado = cargoEmpleado;
        this.areaEmpleado = areaEmpleado;
        this.salarioEmpleado = salarioEmpleado;
        this.fechaContratacionEmpleado = fechaContratacionEmpleado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
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
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public UsuarioSummaryDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioSummaryDTO usuario) { this.usuario = usuario; }
}