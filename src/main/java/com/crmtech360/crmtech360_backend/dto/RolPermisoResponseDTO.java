package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
// Asumo que PermisoResponseDTO ya está definido y anotado
// import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;

@Schema(description = "DTO para la respuesta de una asignación de Permiso a un Rol.")
public class RolPermisoResponseDTO {

    @Schema(description = "Identificador único de la asignación rol-permiso.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRolPermiso;

    @Schema(description = "Nombre del rol al que se asignó el permiso.", example = "VENTAS", accessMode = Schema.AccessMode.READ_ONLY)
    private String rolNombre;

    @Schema(description = "Información detallada del permiso asignado al rol.", accessMode = Schema.AccessMode.READ_ONLY)
    private PermisoResponseDTO permiso; // Asegúrate que PermisoResponseDTO esté anotado

    // Constructores
    public RolPermisoResponseDTO() {
    }

    public RolPermisoResponseDTO(Integer idRolPermiso, String rolNombre, PermisoResponseDTO permiso) {
        this.idRolPermiso = idRolPermiso;
        this.rolNombre = rolNombre;
        this.permiso = permiso;
    }

    // Getters y Setters
    public Integer getIdRolPermiso() { return idRolPermiso; }
    public void setIdRolPermiso(Integer idRolPermiso) { this.idRolPermiso = idRolPermiso; }
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
    public PermisoResponseDTO getPermiso() { return permiso; }
    public void setPermiso(PermisoResponseDTO permiso) { this.permiso = permiso; }
}