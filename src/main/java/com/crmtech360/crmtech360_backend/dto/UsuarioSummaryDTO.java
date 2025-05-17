package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de una cuenta de Usuario, " +
        "usualmente utilizado cuando se vincula a otras entidades (ej. un Empleado).")
public class UsuarioSummaryDTO {

    @Schema(description = "Identificador único del usuario.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuario;

    @Schema(description = "Nombre de usuario utilizado para el inicio de sesión.", example = "carlos.rodriguez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreUsuario;

    @Schema(description = "Rol principal asignado al usuario en el sistema.", example = "Operario", accessMode = Schema.AccessMode.READ_ONLY)
    private String rolUsuario;

    // Constructores
    public UsuarioSummaryDTO() {
    }

    public UsuarioSummaryDTO(Integer idUsuario, String nombreUsuario, String rolUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
}