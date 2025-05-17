package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO está aquí
import com.crmtech360.crmtech360_backend.service.OrdenCompraService;
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
@RequestMapping("/api/v1/ordenes-compra")
@Tag(name = "Órdenes de Compra", description = "API para la gestión de órdenes de compra de insumos a proveedores.")
@SecurityRequirement(name = "bearerAuth")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    @Operation(summary = "Crear una nueva orden de compra",
            description = "Registra una nueva orden de compra para adquirir insumos de un proveedor. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_CREAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden de compra creada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenCompraResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. proveedor no existe, detalles vacíos, insumo en detalle no existe).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado no encontrado (ej. Proveedor o Insumo).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_CREAR_ORDEN_COMPRA')")
    public ResponseEntity<OrdenCompraResponseDTO> createOrdenCompra(@Valid @RequestBody OrdenCompraCreateRequestDTO createRequestDTO) {
        OrdenCompraResponseDTO createdOrden = ordenCompraService.createOrdenCompra(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrden.getIdOrdenCompra())
                .toUri();
        return ResponseEntity.created(location).body(createdOrden);
    }

    @Operation(summary = "Obtener todas las órdenes de compra (paginado)",
            description = "Devuelve una lista paginada de todas las órdenes de compra. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_ORDENES_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de compra obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<OrdenCompraResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_ORDENES_COMPRA')")
    public ResponseEntity<Page<OrdenCompraResponseDTO>> getAllOrdenesCompra(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=fechaPedidoCompra,desc)")
            @PageableDefault(size = 10, sort = "fechaPedidoCompra", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<OrdenCompraResponseDTO> ordenes = ordenCompraService.findAllOrdenesCompra(pageable);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener todas las órdenes de compra de un proveedor específico",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_ORDENES_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de compra para el proveedor."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.")
    })
    @GetMapping("/proveedor/{idProveedor}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_ORDENES_COMPRA')")
    public ResponseEntity<List<OrdenCompraResponseDTO>> getOrdenesCompraByProveedorId(
            @Parameter(description = "ID del proveedor para filtrar las órdenes de compra.", required = true) @PathVariable Integer idProveedor) {
        List<OrdenCompraResponseDTO> ordenes = ordenCompraService.findOrdenesCompraByProveedorId(idProveedor);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener una orden de compra por su ID",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_ORDENES_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de compra encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenCompraResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra no encontrada.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_ORDENES_COMPRA')")
    public ResponseEntity<OrdenCompraResponseDTO> getOrdenCompraById(
            @Parameter(description = "ID de la orden de compra a obtener.", required = true) @PathVariable Integer id) {
        OrdenCompraResponseDTO orden = ordenCompraService.findOrdenCompraById(id);
        return ResponseEntity.ok(orden);
    }

    @Operation(summary = "Actualizar la cabecera de una orden de compra existente",
            description = "Permite modificar campos como fechas y estado de una orden de compra. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de compra actualizada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenCompraResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o no se puede modificar la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra no encontrada.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_ORDEN_COMPRA')")
    public ResponseEntity<OrdenCompraResponseDTO> updateOrdenCompraHeader(
            @Parameter(description = "ID de la orden de compra a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody OrdenCompraUpdateRequestDTO updateRequestDTO) {
        OrdenCompraResponseDTO updatedOrden = ordenCompraService.updateOrdenCompraHeader(id, updateRequestDTO);
        return ResponseEntity.ok(updatedOrden);
    }

    @Operation(summary = "Anular una orden de compra",
            description = "Marca una orden de compra como anulada. No se puede anular si ya fue recibida. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_ANULAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de compra anulada exitosamente."), // Podría ser 204 No Content también
            @ApiResponse(responseCode = "400", description = "No se puede anular la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra no encontrada.")
    })
    @PostMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_ANULAR_ORDEN_COMPRA')")
    public ResponseEntity<Void> anularOrdenCompra(
            @Parameter(description = "ID de la orden de compra a anular.", required = true) @PathVariable Integer id) {
        ordenCompraService.anularOrdenCompra(id);
        return ResponseEntity.ok().build();
    }

    // --- Endpoints para Detalles de Orden de Compra ---

    @Operation(summary = "Añadir un nuevo detalle a una orden de compra existente",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle añadido exitosamente a la orden.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetalleOrdenCompraResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. orden no editable, insumo no existe)."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra o Insumo no encontrado.")
    })
    @PostMapping("/{idOrdenCompra}/detalles")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_ORDEN_COMPRA')")
    public ResponseEntity<DetalleOrdenCompraResponseDTO> addDetalleToOrdenCompra(
            @Parameter(description = "ID de la orden de compra.", required = true) @PathVariable Integer idOrdenCompra,
            @Valid @RequestBody DetalleOrdenCompraRequestDTO detalleRequestDTO) {
        DetalleOrdenCompraResponseDTO createdDetalle = ordenCompraService.addDetalleToOrdenCompra(idOrdenCompra, detalleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDetalle);
    }

    @Operation(summary = "Actualizar un detalle específico en una orden de compra",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra, detalle o insumo no encontrado.")
    })
    @PutMapping("/{idOrdenCompra}/detalles/{idDetalleCompra}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_ORDEN_COMPRA')")
    public ResponseEntity<DetalleOrdenCompraResponseDTO> updateDetalleInOrdenCompra(
            @Parameter(description = "ID de la orden de compra.", required = true) @PathVariable Integer idOrdenCompra,
            @Parameter(description = "ID del detalle de compra a actualizar.", required = true) @PathVariable Integer idDetalleCompra,
            @Valid @RequestBody DetalleOrdenCompraRequestDTO detalleRequestDTO) {
        DetalleOrdenCompraResponseDTO updatedDetalle = ordenCompraService.updateDetalleInOrdenCompra(idOrdenCompra, idDetalleCompra, detalleRequestDTO);
        return ResponseEntity.ok(updatedDetalle);
    }

    @Operation(summary = "Eliminar un detalle específico de una orden de compra",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_ORDEN_COMPRA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente de la orden."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra o detalle no encontrado.")
    })
    @DeleteMapping("/{idOrdenCompra}/detalles/{idDetalleCompra}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_ORDEN_COMPRA')")
    public ResponseEntity<Void> removeDetalleFromOrdenCompra(
            @Parameter(description = "ID de la orden de compra.", required = true) @PathVariable Integer idOrdenCompra,
            @Parameter(description = "ID del detalle de compra a eliminar.", required = true) @PathVariable Integer idDetalleCompra) {
        ordenCompraService.removeDetalleFromOrdenCompra(idOrdenCompra, idDetalleCompra);
        return ResponseEntity.noContent().build();
    }
}