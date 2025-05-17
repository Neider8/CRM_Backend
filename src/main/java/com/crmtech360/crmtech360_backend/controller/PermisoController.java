package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de tener este DTO para los errores
import com.crmtech360.crmtech360_backend.dto.PermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;
import com.crmtech360.crmtech360_backend.service.PermisoService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/permisos")
@Tag(name = "Permisos", description = "API para la gestión de permisos del sistema (operaciones administrativas).")
@SecurityRequirement(name = "bearerAuth") // Todos los endpoints aquí requieren autenticación
public class PermisoController {

    private final PermisoService permisoService;

    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

    @Operation(summary = "Crear un nuevo permiso",
            description = "Crea un nuevo permiso en el sistema. El nombre del permiso debe ser único y seguir una convención (ej. PERMISO_ACCION_RECURSO). " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permiso creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. nombre vacío, formato incorrecto).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Administrador."),
            @ApiResponse(responseCode = "409", description = "Conflicto - El permiso con ese nombre ya existe.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermisoResponseDTO> createPermiso(@Valid @RequestBody PermisoRequestDTO permisoRequestDTO) {
        PermisoResponseDTO createdPermiso = permisoService.createPermiso(permisoRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPermiso.getIdPermiso())
                .toUri();
        return ResponseEntity.created(location).body(createdPermiso);
    }

    @Operation(summary = "Obtener todos los permisos",
            description = "Devuelve una lista de todos los permisos definidos en el sistema. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PermisoResponseDTO.class)))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Administrador.")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PermisoResponseDTO>> getAllPermisos() {
        List<PermisoResponseDTO> permisos = permisoService.findAllPermisos();
        return ResponseEntity.ok(permisos);
    }

    @Operation(summary = "Obtener un permiso por su ID",
            description = "Devuelve los detalles de un permiso específico. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermisoResponseDTO> getPermisoById(
            @Parameter(description = "ID del permiso a obtener.", required = true, example = "1") @PathVariable Integer id) {
        PermisoResponseDTO permiso = permisoService.findPermisoById(id);
        return ResponseEntity.ok(permiso);
    }

    @Operation(summary = "Obtener un permiso por su nombre",
            description = "Devuelve los detalles de un permiso específico basado en su nombre único. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermisoResponseDTO> getPermisoByNombre(
            @Parameter(description = "Nombre único del permiso (ej. PERMISO_CREAR_USUARIO).", required = true) @PathVariable String nombre) {
        PermisoResponseDTO permiso = permisoService.findByNombrePermiso(nombre);
        return ResponseEntity.ok(permiso);
    }


    @Operation(summary = "Actualizar un permiso existente",
            description = "Permite modificar el nombre o la descripción de un permiso. El nuevo nombre debe ser único. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermisoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto, el nuevo nombre de permiso ya existe.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PermisoResponseDTO> updatePermiso(
            @Parameter(description = "ID del permiso a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody PermisoRequestDTO permisoRequestDTO) {
        PermisoResponseDTO updatedPermiso = permisoService.updatePermiso(id, permisoRequestDTO);
        return ResponseEntity.ok(updatedPermiso);
    }

    @Operation(summary = "Eliminar un permiso por su ID",
            description = "Elimina un permiso del sistema. Esta acción puede fallar si el permiso está actualmente asignado a algún rol. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permiso eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - El permiso está en uso (asignado a roles) y no se puede eliminar.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deletePermiso(
            @Parameter(description = "ID del permiso a eliminar.", required = true) @PathVariable Integer id) {
        permisoService.deletePermiso(id);
        return ResponseEntity.noContent().build();
    }
}