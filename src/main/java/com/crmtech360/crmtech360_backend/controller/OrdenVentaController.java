package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO esté aquí
import com.crmtech360.crmtech360_backend.service.OrdenVentaService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-venta")
@Tag(name = "Órdenes de Venta", description = "API para la gestión de órdenes de venta de productos y sus detalles.")
@SecurityRequirement(name = "bearerAuth")
public class OrdenVentaController {

    private final OrdenVentaService ordenVentaService;

    public OrdenVentaController(OrdenVentaService ordenVentaService) {
        this.ordenVentaService = ordenVentaService;
    }

    @Operation(summary = "Crear una nueva orden de venta",
            description = "Registra una nueva orden de venta para un cliente, incluyendo los productos solicitados. " +
                    "El total de la orden se calcula automáticamente. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_CREAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden de venta creada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenVentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. cliente no existe, producto en detalle no existe, detalles vacíos).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Recurso relacionado no encontrado (Cliente o Producto).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_CREAR_ORDEN_VENTA')")
    public ResponseEntity<OrdenVentaResponseDTO> createOrdenVenta(@Valid @RequestBody OrdenVentaCreateRequestDTO createRequestDTO) {
        OrdenVentaResponseDTO createdOrdenVenta = ordenVentaService.createOrdenVenta(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdOrdenVenta.getIdOrdenVenta())
                .toUri();
        return ResponseEntity.created(location).body(createdOrdenVenta);
    }

    @Operation(summary = "Obtener todas las órdenes de venta (paginado)",
            description = "Devuelve una lista paginada de todas las órdenes de venta. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_ORDENES_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de venta obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<OrdenVentaResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_ORDENES_VENTA')")
    public ResponseEntity<Page<OrdenVentaResponseDTO>> getAllOrdenesVenta(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=fechaPedido,desc)")
            @PageableDefault(size = 10, sort = "fechaPedido", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrdenVentaResponseDTO> ordenes = ordenVentaService.findAllOrdenesVenta(pageable);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Obtener una orden de venta por su ID",
            description = "Devuelve los detalles completos de una orden de venta específica, incluyendo sus ítems. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_ORDENES_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de venta encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenVentaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta no encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_ORDENES_VENTA')")
    public ResponseEntity<OrdenVentaResponseDTO> getOrdenVentaById(
            @Parameter(description = "ID de la orden de venta a obtener.", required = true, example = "1") @PathVariable Integer id) {
        OrdenVentaResponseDTO ordenVenta = ordenVentaService.findOrdenVentaById(id);
        return ResponseEntity.ok(ordenVenta);
    }

    @Operation(summary = "Obtener todas las órdenes de venta de un cliente específico",
            description = "Devuelve una lista de todas las órdenes de venta asociadas a un ID de cliente. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_ORDENES_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de órdenes de venta para el cliente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @GetMapping("/cliente/{idCliente}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_ORDENES_VENTA')")
    public ResponseEntity<List<OrdenVentaResponseDTO>> getOrdenesVentaByClienteId(
            @Parameter(description = "ID del cliente para filtrar las órdenes de venta.", required = true) @PathVariable Integer idCliente) {
        List<OrdenVentaResponseDTO> ordenes = ordenVentaService.findOrdenesVentaByClienteId(idCliente);
        return ResponseEntity.ok(ordenes);
    }

    @Operation(summary = "Actualizar la cabecera de una orden de venta existente",
            description = "Permite modificar campos como fechas, estado y observaciones de una orden de venta. " +
                    "No se pueden modificar los detalles de la orden con este endpoint. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_EDITAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de venta actualizada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenVentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o no se puede modificar la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta no encontrada.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_ORDEN_VENTA')")
    public ResponseEntity<OrdenVentaResponseDTO> updateOrdenVentaHeader(
            @Parameter(description = "ID de la orden de venta a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody OrdenVentaUpdateRequestDTO updateRequestDTO) {
        OrdenVentaResponseDTO updatedOrdenVenta = ordenVentaService.updateOrdenVentaHeader(id, updateRequestDTO);
        return ResponseEntity.ok(updatedOrdenVenta);
    }

    @Operation(summary = "Anular una orden de venta",
            description = "Marca una orden de venta como ANULADA. No se puede anular si ya fue entregada o está en producción avanzada (lógica de servicio). " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_ANULAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orden de venta anulada exitosamente."),
            @ApiResponse(responseCode = "400", description = "No se puede anular la orden en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta no encontrada.")
    })
    @PostMapping("/{id}/anular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_ANULAR_ORDEN_VENTA')")
    public ResponseEntity<Void> anularOrdenVenta(
            @Parameter(description = "ID de la orden de venta a anular.", required = true) @PathVariable Integer id) {
        ordenVentaService.anularOrdenVenta(id);
        return ResponseEntity.ok().build();
    }

    // --- Endpoints para Detalles de Orden de Venta ---

    @Operation(summary = "Añadir un nuevo detalle a una orden de venta existente",
            description = "Permite agregar un producto a una orden de venta que no esté en un estado final (ej. no anulada o entregada). " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_EDITAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle añadido exitosamente a la orden.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetalleOrdenVentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. orden no editable, producto no existe)."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta o Producto no encontrado.")
    })
    @PostMapping("/{idOrdenVenta}/detalles")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_ORDEN_VENTA')")
    public ResponseEntity<DetalleOrdenVentaResponseDTO> addDetalleToOrdenVenta(
            @Parameter(description = "ID de la orden de venta a la que se añade el detalle.", required = true) @PathVariable Integer idOrdenVenta,
            @Valid @RequestBody DetalleOrdenVentaRequestDTO detalleRequestDTO) {
        DetalleOrdenVentaResponseDTO createdDetalle = ordenVentaService.addDetalleToOrdenVenta(idOrdenVenta, detalleRequestDTO);
        // Considerar construir URI para el detalle si se desea:
        // URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idDetalle}").buildAndExpand(createdDetalle.getIdDetalleOrden()).toUri();
        // return ResponseEntity.created(location).body(createdDetalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDetalle);
    }

    @Operation(summary = "Actualizar un detalle específico en una orden de venta",
            description = "Permite modificar la cantidad o precio de un ítem en una orden de venta no finalizada. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_EDITAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DetalleOrdenVentaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta, detalle o producto no encontrado.")
    })
    @PutMapping("/{idOrdenVenta}/detalles/{idDetalleVenta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_ORDEN_VENTA')")
    public ResponseEntity<DetalleOrdenVentaResponseDTO> updateDetalleInOrdenVenta(
            @Parameter(description = "ID de la orden de venta.", required = true) @PathVariable Integer idOrdenVenta,
            @Parameter(description = "ID del detalle de la orden a actualizar.", required = true) @PathVariable Integer idDetalleVenta,
            @Valid @RequestBody DetalleOrdenVentaRequestDTO detalleRequestDTO) {
        DetalleOrdenVentaResponseDTO updatedDetalle = ordenVentaService.updateDetalleInOrdenVenta(idOrdenVenta, idDetalleVenta, detalleRequestDTO);
        return ResponseEntity.ok(updatedDetalle);
    }

    @Operation(summary = "Eliminar un detalle específico de una orden de venta",
            description = "Elimina un ítem de una orden de venta no finalizada. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_EDITAR_ORDEN_VENTA.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente de la orden."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta o detalle no encontrado.")
    })
    @DeleteMapping("/{idOrdenVenta}/detalles/{idDetalleVenta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_ORDEN_VENTA')")
    public ResponseEntity<Void> removeDetalleFromOrdenVenta(
            @Parameter(description = "ID de la orden de venta.", required = true) @PathVariable Integer idOrdenVenta,
            @Parameter(description = "ID del detalle de la orden a eliminar.", required = true) @PathVariable Integer idDetalleVenta) {
        ordenVentaService.removeDetalleFromOrdenVenta(idOrdenVenta, idDetalleVenta);
        return ResponseEntity.noContent().build();
    }
}