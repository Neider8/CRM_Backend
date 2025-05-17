package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import com.crmtech360.crmtech360_backend.dto.EmpleadoSummaryDTO; // DESCOMENTA ESTA LÍNEA

@Schema(description = "DTO para la respuesta de información detallada de una cuenta de Usuario.")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador único del usuario.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuario;

    @Schema(description = "Nombre de usuario utilizado para el inicio de sesión.", example = "carlos.rodriguez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreUsuario;

    @Schema(description = "Rol principal asignado al usuario en el sistema.", example = "Operario", accessMode = Schema.AccessMode.READ_ONLY)
    private String rolUsuario;

    @Schema(description = "Fecha y hora de creación de la cuenta de usuario en el sistema.", example = "2023-06-01T09:05:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización de la información de la cuenta de usuario.", example = "2024-03-15T11:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Información resumida del empleado asociado a esta cuenta de usuario. Será nulo si el usuario no está vinculado a un empleado.",
            nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private EmpleadoSummaryDTO empleado;

    // Constructores
    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Integer idUsuario, String nombreUsuario, String rolUsuario, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, EmpleadoSummaryDTO empleado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.empleado = empleado;
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public EmpleadoSummaryDTO getEmpleado() { return empleado; }
    public void setEmpleado(EmpleadoSummaryDTO empleado) { this.empleado = empleado; }
}