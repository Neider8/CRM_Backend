package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO está aquí
import com.crmtech360.crmtech360_backend.service.OrdenProduccionService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-produccion")
@Tag(name = "Órdenes de Producción", description = "API para la gestión de órdenes de producción y sus tareas asociadas.")
@SecurityRequirement(name = "bearerAuth")
public class OrdenProduccionController {

    private final OrdenProduccionService ordenProduccionService;

    public OrdenProduccionController(OrdenProduccionService ordenProduccionService) {
        this.ordenProduccionService = ordenProduccionService;
    }

    @Operation(summary = "Crear una nueva orden de producción",
            description = "Registra una nueva orden de producción, usualmente vinculada a una orden de venta confirmada. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_CREAR_ORDEN_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden de producción creada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenProduccionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. orden de venta no válida o en estado incorrecto).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de Venta no encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_CREAR_ORDEN_PRODUCCION')")
    public ResponseEntity<OrdenProduccionResponseDTO> createOrdenProduccion(@Valid @RequestBody OrdenProduccionCreateRequestDTO createRequestDTO) {
        OrdenProduccionResponseDTO createdOrden = ordenProduccionService.createOrdenProduccion(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrden.getIdOrdenProduccion())
                .toUri();
        return ResponseEntity.created(location).body(createdOrden);
    }

    @Operation(summary = "Obtener todas las órdenes de producción (paginado)",
            description = "Devuelve una lista paginada de todas las órdenes de producción. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_ORDENES_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de producción obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<OrdenProduccionResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_ORDENES_PRODUCCION')")
    public ResponseEntity<Page<OrdenProduccionResponseDTO>> getAllOrdenesProduccion(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=fechaCreacion,desc)")
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<OrdenProduccionResponseDTO> ordenes = ordenProduccionService.findAllOrdenesProduccion(pageable);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener todas las órdenes de producción para una orden de venta específica",
            description = "Permite rastrear la producción asociada a una venta. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS, OPERARIO o permiso PERMISO_VER_ORDENES_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de producción para la orden de venta especificada."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta no encontrada.")
    })
    @GetMapping("/por-orden-venta/{idOrdenVenta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS', 'OPERARIO') or hasAuthority('PERMISO_VER_ORDENES_PRODUCCION')")
    public ResponseEntity<List<OrdenProduccionResponseDTO>> getOrdenesProduccionByOrdenVentaId(
            @Parameter(description = "ID de la Orden de Venta.", required = true) @PathVariable Integer idOrdenVenta) {
        List<OrdenProduccionResponseDTO> ordenes = ordenProduccionService.findOrdenesProduccionByOrdenVentaId(idOrdenVenta);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener una orden de producción por su ID",
            description = "Devuelve los detalles de una orden de producción específica, incluyendo sus tareas. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_ORDENES_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de producción encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenProduccionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de producción no encontrada.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_ORDENES_PRODUCCION')")
    public ResponseEntity<OrdenProduccionResponseDTO> getOrdenProduccionById(
            @Parameter(description = "ID de la orden de producción a obtener.", required = true) @PathVariable Integer id) {
        OrdenProduccionResponseDTO orden = ordenProduccionService.findOrdenProduccionById(id);
        return ResponseEntity.ok(orden);
    }

    @Operation(summary = "Actualizar la cabecera de una orden de producción existente",
            description = "Permite modificar fechas, estado u observaciones de una orden de producción. " +
                    "No se puede modificar si está ANULADA o TERMINADA. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_ORDEN_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de producción actualizada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenProduccionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o no se puede modificar la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de producción no encontrada.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_ORDEN_PRODUCCION')")
    public ResponseEntity<OrdenProduccionResponseDTO> updateOrdenProduccionHeader(
            @Parameter(description = "ID de la orden de producción a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody OrdenProduccionUpdateRequestDTO updateRequestDTO) {
        OrdenProduccionResponseDTO updatedOrden = ordenProduccionService.updateOrdenProduccionHeader(id, updateRequestDTO);
        return ResponseEntity.ok(updatedOrden);
    }

    @Operation(summary = "Anular una orden de producción",
            description = "Marca una orden de producción como anulada. Esto también puede afectar el estado de sus tareas asociadas. " +
                    "No se puede anular si ya está TERMINADA. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_ANULAR_ORDEN_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de producción anulada exitosamente."),
            @ApiResponse(responseCode = "400", description = "No se puede anular la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de producción no encontrada.")
    })
    @PostMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_ANULAR_ORDEN_PRODUCCION')")
    public ResponseEntity<Void> anularOrdenProduccion(
            @Parameter(description = "ID de la orden de producción a anular.", required = true) @PathVariable Integer id) {
        ordenProduccionService.anularOrdenProduccion(id);
        return ResponseEntity.ok().build();
    }

    // --- Endpoints para Tareas de Producción ---

    @Operation(summary = "Añadir una nueva tarea a una orden de producción",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_GESTIONAR_TAREAS_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea añadida exitosamente a la orden de producción.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TareaProduccionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. orden de producción no editable, empleado no existe)."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de producción o Empleado no encontrado.")
    })
    @PostMapping("/{idOrdenProduccion}/tareas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_GESTIONAR_TAREAS_PRODUCCION')")
    public ResponseEntity<TareaProduccionResponseDTO> addTareaToOrdenProduccion(
            @Parameter(description = "ID de la orden de producción a la que se añade la tarea.", required = true) @PathVariable Integer idOrdenProduccion,
            @Valid @RequestBody TareaProduccionCreateRequestDTO tareaRequestDTO) {
        TareaProduccionResponseDTO createdTarea = ordenProduccionService.addTareaToOrdenProduccion(idOrdenProduccion, tareaRequestDTO);
        // URI location podría apuntar a /api/v1/ordenes-produccion/{idOrdenProduccion}/tareas/{idTarea}
        // Por simplicidad, devolvemos el cuerpo con 201.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTarea);
    }

    @Operation(summary = "Actualizar una tarea específica en una orden de producción",
            description = "Permite modificar detalles de una tarea, como su estado o empleado asignado. " +
                    "OPERARIO puede actualizar tareas asignadas a él. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO (condicional) o permiso PERMISO_GESTIONAR_TAREAS_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido (ej. operario intentando modificar tarea ajena)."),
            @ApiResponse(responseCode = "404", description = "Orden de producción, tarea o empleado no encontrado.")
    })
    @PutMapping("/{idOrdenProduccion}/tareas/{idTarea}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_GESTIONAR_TAREAS_PRODUCCION')")
    public ResponseEntity<TareaProduccionResponseDTO> updateTareaInOrdenProduccion(
            @Parameter(description = "ID de la orden de producción.", required = true) @PathVariable Integer idOrdenProduccion,
            @Parameter(description = "ID de la tarea a actualizar.", required = true) @PathVariable Integer idTarea,
            @Valid @RequestBody TareaProduccionUpdateRequestDTO tareaRequestDTO) {
        // La lógica para que un OPERARIO solo modifique sus propias tareas debería estar en el servicio.
        TareaProduccionResponseDTO updatedTarea = ordenProduccionService.updateTareaInOrdenProduccion(idOrdenProduccion, idTarea, tareaRequestDTO);
        return ResponseEntity.ok(updatedTarea);
    }

    @Operation(summary = "Eliminar una tarea específica de una orden de producción",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_GESTIONAR_TAREAS_PRODUCCION.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de producción o tarea no encontrada.")
    })
    @DeleteMapping("/{idOrdenProduccion}/tareas/{idTarea}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_GESTIONAR_TAREAS_PRODUCCION')")
    public ResponseEntity<Void> removeTareaFromOrdenProduccion(
            @Parameter(description = "ID de la orden de producción.", required = true) @PathVariable Integer idOrdenProduccion,
            @Parameter(description = "ID de la tarea a eliminar.", required = true) @PathVariable Integer idTarea) {
        ordenProduccionService.removeTareaFromOrdenProduccion(idOrdenProduccion, idTarea);
        return ResponseEntity.noContent().build();
    }
}