package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de creación o actualización de un Permiso del sistema.")
public class PermisoRequestDTO {

    @Schema(description = "Nombre único del permiso. Debe seguir una convención, por ejemplo: VER_CLIENTES, CREAR_PRODUCTO, PERMISO_GESTIONAR_USUARIOS.",
            example = "PERMISO_VER_CLIENTES",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del permiso no puede estar vacío.")
    @Size(min = 3, max = 100, message = "El nombre del permiso debe tener entre 3 y 100 caracteres.")
    @Pattern(regexp = "^[A-Z_]+$", message = "El nombre del permiso solo puede contener letras mayúsculas y guiones bajos (ej. NOMBRE_PERMISO).")
    private String nombrePermiso;

    // El DTO que me proporcionaste solo tiene nombrePermiso. Si tuvieras descripción:
    /*
    @Schema(description = "Descripción detallada de lo que este permiso autoriza.",
            example = "Permite al usuario ver la lista de clientes y sus detalles.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "La descripción del permiso no puede exceder los 500 caracteres.")
    private String descripcionPermiso;
    */

    // Constructor vacío
    public PermisoRequestDTO() {
    }

    // Constructor con argumentos
    public PermisoRequestDTO(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
        // Si tuvieras descripción:
        // this.descripcionPermiso = descripcionPermiso;
    }

    // Getters y Setters
    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    /*
    // Si tuvieras descripción:
    public String getDescripcionPermiso() {
        return descripcionPermiso;
    }

    public void setDescripcionPermiso(String descripcionPermiso) {
        this.descripcionPermiso = descripcionPermiso;
    }
    */
}