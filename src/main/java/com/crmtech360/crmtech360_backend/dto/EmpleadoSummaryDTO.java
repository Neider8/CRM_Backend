package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de un empleado, " +
        "usualmente utilizado cuando se muestra un empleado asociado a otra entidad (ej. un Usuario).")
public class EmpleadoSummaryDTO {

    @Schema(description = "Identificador único del empleado.", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEmpleado;

    @Schema(description = "Número único del documento de identificación del empleado.", example = "1020304050", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroDocumento;

    @Schema(description = "Nombre completo del empleado.", example = "Carlos Alberto Rodriguez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreEmpleado;

    // Constructores
    public EmpleadoSummaryDTO() {
    }

    public EmpleadoSummaryDTO(Integer idEmpleado, String numeroDocumento, String nombreEmpleado) {
        this.idEmpleado = idEmpleado;
        this.numeroDocumento = numeroDocumento;
        this.nombreEmpleado = nombreEmpleado;
    }

    // Getters y Setters
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
}