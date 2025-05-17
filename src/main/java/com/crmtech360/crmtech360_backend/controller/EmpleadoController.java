package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de tener este DTO para los errores
import com.crmtech360.crmtech360_backend.dto.EmpleadoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/empleados")
@Tag(name = "Empleados", description = "API para la gestión de empleados del sistema.")
@SecurityRequirement(name = "bearerAuth") // Requiere autenticación JWT para todos los endpoints aquí
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Crear un nuevo empleado",
            description = "Registra un nuevo empleado en el sistema. El número de documento debe ser único. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_CREAR_EMPLEADOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida debido a datos incorrectos (ej. validación fallida).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado - Se requiere token JWT o token inválido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - El usuario no tiene los permisos necesarios.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un empleado con el mismo número de documento.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_CREAR_EMPLEADOS')")
    public ResponseEntity<EmpleadoResponseDTO> createEmpleado(@Valid @RequestBody EmpleadoCreateRequestDTO empleadoCreateRequestDTO) {
        EmpleadoResponseDTO createdEmpleado = empleadoService.createEmpleado(empleadoCreateRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEmpleado.getIdEmpleado())
                .toUri();
        return ResponseEntity.created(location).body(createdEmpleado);
    }

    @Operation(summary = "Obtener todos los empleados (paginado)",
            description = "Devuelve una lista paginada de todos los empleados. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_EMPLEADOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), //Page<EmpleadoResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_EMPLEADOS')")
    public ResponseEntity<Page<EmpleadoResponseDTO>> getAllEmpleados(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=nombreEmpleado,asc)")
            @PageableDefault(size = 10, sort = "nombreEmpleado") Pageable pageable) {
        Page<EmpleadoResponseDTO> empleados = empleadoService.findAllEmpleados(pageable);
        return ResponseEntity.ok(empleados);
    }

    @Operation(summary = "Obtener un empleado por su ID",
            description = "Devuelve los detalles de un empleado específico. " +
                    "Puede ser accedido por ADMINISTRADOR, GERENTE, alguien con PERMISO_VER_EMPLEADOS, o el propio usuario si el empleado está vinculado a su cuenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_EMPLEADOS') or @userSecurity.isEmployeeSelf(authentication, #id)")
    public ResponseEntity<EmpleadoResponseDTO> getEmpleadoById(
            @Parameter(description = "ID del empleado a obtener.", required = true, example = "1") @PathVariable Integer id) {
        EmpleadoResponseDTO empleado = empleadoService.findEmpleadoById(id);
        return ResponseEntity.ok(empleado);
    }

    @Operation(summary = "Obtener un empleado por su número de documento",
            description = "Devuelve los detalles de un empleado específico basado en su número de documento. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_EMPLEADOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado.")
    })
    @GetMapping("/documento/{numeroDocumento}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_EMPLEADOS')")
    public ResponseEntity<EmpleadoResponseDTO> getEmpleadoByNumeroDocumento(
            @Parameter(description = "Número de documento del empleado.", required = true, example = "10203040") @PathVariable String numeroDocumento) {
        EmpleadoResponseDTO empleado = empleadoService.findEmpleadoByNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(empleado);
    }

    @Operation(summary = "Actualizar un empleado existente",
            description = "Permite modificar los detalles de un empleado existente. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_EMPLEADOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmpleadoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_EMPLEADOS')")
    public ResponseEntity<EmpleadoResponseDTO> updateEmpleado(
            @Parameter(description = "ID del empleado a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody EmpleadoUpdateRequestDTO empleadoUpdateRequestDTO) {
        EmpleadoResponseDTO updatedEmpleado = empleadoService.updateEmpleado(id, empleadoUpdateRequestDTO);
        return ResponseEntity.ok(updatedEmpleado);
    }

    @Operation(summary = "Eliminar un empleado por su ID",
            description = "Elimina un empleado del sistema. Esta acción puede tener implicaciones si el empleado está asociado a usuarios o tareas. " +
                    "Requiere rol ADMINISTRADOR o permiso PERMISO_ELIMINAR_EMPLEADOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado."),
            @ApiResponse(responseCode = "409", description = "Conflicto, el empleado no se puede eliminar (ej. está vinculado a un usuario activo o tareas).")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERMISO_ELIMINAR_EMPLEADOS')")
    public ResponseEntity<Void> deleteEmpleado(
            @Parameter(description = "ID del empleado a eliminar.", required = true) @PathVariable Integer id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}