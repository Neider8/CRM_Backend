package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO para la respuesta de información de un Permiso del sistema.")
public class PermisoResponseDTO {

    @Schema(description = "Identificador único del permiso.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPermiso;

    @Schema(description = "Nombre único del permiso (ej. PERMISO_VER_CLIENTES).", example = "PERMISO_VER_CLIENTES", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombrePermiso;

    // Si tuvieras un campo 'descripcionPermiso' en la entidad y lo incluyeras en la respuesta:
    /*
    @Schema(description = "Descripción detallada de lo que este permiso autoriza.",
            example = "Permite al usuario ver la lista de clientes y sus detalles.",
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = true)
    private String descripcionPermiso;
    */

    // Constructor vacío
    public PermisoResponseDTO() {
    }

    // Constructor con argumentos
    public PermisoResponseDTO(Integer idPermiso, String nombrePermiso) {
        this.idPermiso = idPermiso;
        this.nombrePermiso = nombrePermiso;
        // Si tuvieras descripción:
        // this.descripcionPermiso = descripcionPermiso;
    }

    // Getters y Setters
    public Integer getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Integer idPermiso) {
        this.idPermiso = idPermiso;
    }

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