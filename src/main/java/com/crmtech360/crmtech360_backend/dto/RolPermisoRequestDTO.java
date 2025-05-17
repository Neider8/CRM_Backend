package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size; // Añadir si se quiere validar la longitud del rolNombre

@Schema(description = "DTO para la solicitud de asignación de un permiso a un rol específico.")
public class RolPermisoRequestDTO {

    @Schema(description = "Nombre del rol al que se asignará el permiso. Debe ser uno de los roles definidos en el sistema (ej. Administrador, Gerente, Operario, Ventas).",
            example = "VENTAS",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del rol no puede estar vacío.")
    // El Pattern actual es específico y podría ser demasiado restrictivo si los nombres de rol son diferentes.
    // Se recomienda que los nombres de rol en la BD coincidan con estos o que el Pattern sea más flexible.
    // Ejemplo de pattern más general si los roles pueden tener otros nombres: @Pattern(regexp = "^[A-Za-z_]+$", message = "El nombre del rol solo puede contener letras y guion bajo.")
    @Pattern(regexp = "Administrador|Gerente|Operario|Ventas", message = "Rol inválido. Valores esperados: Administrador, Gerente, Operario, Ventas.")
    @Size(max = 50, message = "El nombre del rol no debe exceder los 50 caracteres.") // Ejemplo de Size
    private String rolNombre;

    @Schema(description = "Identificador único del permiso que se asignará al rol.",
            example = "15", // Suponiendo que 15 es el ID de PERMISO_VER_CLIENTES
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del permiso es obligatorio.")
    private Integer idPermiso;

    // Constructores
    public RolPermisoRequestDTO() {
    }

    public RolPermisoRequestDTO(String rolNombre, Integer idPermiso) {
        this.rolNombre = rolNombre;
        this.idPermiso = idPermiso;
    }

    // Getters y Setters
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }
}