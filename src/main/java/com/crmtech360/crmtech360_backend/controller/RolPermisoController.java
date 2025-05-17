package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de tener este DTO para los errores
import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.RolPermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.RolPermisoResponseDTO;
import com.crmtech360.crmtech360_backend.service.RolPermisoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importante para la seguridad
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles-permisos")
@Tag(name = "Roles y Permisos", description = "API para gestionar la asignación de permisos a los roles del sistema (operaciones administrativas).")
@SecurityRequirement(name = "bearerAuth") // Todos los endpoints aquí requieren autenticación
public class RolPermisoController {

    private final RolPermisoService rolPermisoService;

    public RolPermisoController(RolPermisoService rolPermisoService) {
        this.rolPermisoService = rolPermisoService;
    }

    @Operation(summary = "Asignar un permiso a un rol",
            description = "Establece una relación entre un rol y un permiso. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permiso asignado al rol exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RolPermisoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. rol o permiso no existen, o la asignación ya existe).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Administrador."),
            @ApiResponse(responseCode = "404", description = "Rol o Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - La asignación de este permiso a este rol ya existe.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<RolPermisoResponseDTO> assignPermisoToRol(@Valid @RequestBody RolPermisoRequestDTO rolPermisoRequestDTO) {
        RolPermisoResponseDTO assignedRolPermiso = rolPermisoService.assignPermisoToRol(rolPermisoRequestDTO);
        // Para este tipo de asignación, devolver el objeto creado con 201 es suficiente.
        // No se suele necesitar una URI de 'Location' para una entrada en una tabla de unión.
        return ResponseEntity.status(HttpStatus.CREATED).body(assignedRolPermiso);
    }

    @Operation(summary = "Obtener todos los permisos asignados a un rol específico",
            description = "Devuelve una lista de todos los permisos que un rol particular posee. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos para el rol obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PermisoResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{rolNombre}/permisos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PermisoResponseDTO>> getPermisosForRol(
            @Parameter(description = "Nombre del rol (ej. VENTAS, GERENTE).", required = true, example = "VENTAS") @PathVariable String rolNombre) {
        List<PermisoResponseDTO> permisos = rolPermisoService.getPermisosForRol(rolNombre);
        return ResponseEntity.ok(permisos);
    }

    @Operation(summary = "Verificar si un rol tiene un permiso específico",
            description = "Comprueba si un rol dado tiene un permiso específico por ID. Devuelve true o false. " +
                    "Requiere rol ADMINISTRADOR (o podría ser `isAuthenticated()` si es una verificación común).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación realizada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Rol o Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{rolNombre}/permisos/{idPermiso}/check")
    @PreAuthorize("hasRole('ADMINISTRADOR')") // O isAuthenticated() si se necesita más flexibilidad
    public ResponseEntity<Boolean> checkIfRolHasPermiso(
            @Parameter(description = "Nombre del rol.", required = true, example = "VENTAS") @PathVariable String rolNombre,
            @Parameter(description = "ID del permiso a verificar.", required = true, example = "10") @PathVariable Integer idPermiso) {
        boolean hasPermiso = rolPermisoService.checkIfRolHasPermiso(rolNombre, idPermiso);
        return ResponseEntity.ok(hasPermiso);
    }

    @Operation(summary = "Remover un permiso de un rol",
            description = "Elimina la asignación de un permiso específico a un rol. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso removido del rol exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "La asignación rol-permiso no fue encontrada o el rol/permiso no existe.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @DeleteMapping("/{rolNombre}/permisos/{idPermiso}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> removePermisoFromRol(
            @Parameter(description = "Nombre del rol.", required = true, example = "VENTAS") @PathVariable String rolNombre,
            @Parameter(description = "ID del permiso a remover.", required = true, example = "10") @PathVariable Integer idPermiso) {
        rolPermisoService.removePermisoFromRol(rolNombre, idPermiso);
        return ResponseEntity.noContent().build();
    }
}