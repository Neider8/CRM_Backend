package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO esté aquí
import com.crmtech360.crmtech360_backend.service.InventarioProductoService;
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
@RequestMapping("/api/v1/inventario-productos")
@Tag(name = "Inventario de Productos", description = "API para la gestión de stock de productos terminados y sus movimientos.")
@SecurityRequirement(name = "bearerAuth")
public class InventarioProductoController {

    private final InventarioProductoService inventarioProductoService;

    public InventarioProductoController(InventarioProductoService inventarioProductoService) {
        this.inventarioProductoService = inventarioProductoService;
    }

    @Operation(summary = "Crear un nuevo registro de inventario para un producto en una ubicación.",
            description = "Establece el stock inicial de un producto en una ubicación. La combinación producto-ubicación debe ser única. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_GESTIONAR_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de inventario creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. producto no existe, datos faltantes).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un registro para este producto en esta ubicación.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_GESTIONAR_INVENTARIO')")
    public ResponseEntity<InventarioProductoResponseDTO> createInventarioProducto(@Valid @RequestBody InventarioProductoCreateRequestDTO createRequestDTO) {
        InventarioProductoResponseDTO createdInventario = inventarioProductoService.createInventarioProducto(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdInventario.getIdInventarioProducto())
                .toUri();
        return ResponseEntity.created(location).body(createdInventario);
    }

    @Operation(summary = "Obtener todos los registros de inventario de productos (paginado).",
            description = "Devuelve una lista paginada del stock de productos en todas las ubicaciones. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inventario de productos obtenida.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<InventarioProductoResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<Page<InventarioProductoResponseDTO>> getAllInventarioProductos(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=producto.nombreProducto,asc)")
            @PageableDefault(size = 10) Pageable pageable) {
        Page<InventarioProductoResponseDTO> inventarios = inventarioProductoService.findAllInventarioProductos(pageable);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Obtener un registro de inventario de producto por su ID.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioProductoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{idInventarioProducto}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<InventarioProductoResponseDTO> getInventarioProductoById(
            @Parameter(description = "ID del registro de inventario del producto.", required = true, example = "1") @PathVariable Integer idInventarioProducto) {
        InventarioProductoResponseDTO inventario = inventarioProductoService.findInventarioProductoById(idInventarioProducto);
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Obtener un registro de inventario por ID de producto y nombre de ubicación.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario encontrado."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado para el producto y ubicación especificados.")
    })
    @GetMapping("/producto/{idProducto}/ubicacion/{ubicacionInventario}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<InventarioProductoResponseDTO> getInventarioByProductoAndUbicacion(
            @Parameter(description = "ID del producto.", required = true, example = "101") @PathVariable Integer idProducto,
            @Parameter(description = "Nombre de la ubicación del inventario.", required = true, example = "Bodega Principal") @PathVariable String ubicacionInventario) {
        InventarioProductoResponseDTO inventario = inventarioProductoService.findByProductoAndUbicacion(idProducto, ubicacionInventario);
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Obtener todos los registros de inventario para un producto específico.",
            description = "Devuelve todas las ubicaciones y stocks para un producto dado. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de registros de inventario para el producto."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @GetMapping("/producto/{idProducto}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<List<InventarioProductoResponseDTO>> getInventariosByProductoId(
            @Parameter(description = "ID del producto para buscar sus inventarios.", required = true, example = "101") @PathVariable Integer idProducto) {
        List<InventarioProductoResponseDTO> inventarios = inventarioProductoService.findInventariosByProductoId(idProducto);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Registrar un movimiento de inventario para un producto (entrada/salida).",
            description = "Actualiza el stock del producto en la ubicación especificada por el idInventarioProducto. " +
                    "Se usa para entradas (ej. finalización de producción) o salidas (ej. venta). " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_REGISTRAR_MOVIMIENTO_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado y stock actualizado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoInventarioProductoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. stock insuficiente para salida, tipo de movimiento incorrecto).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario de producto no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/movimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_REGISTRAR_MOVIMIENTO_INVENTARIO')")
    public ResponseEntity<MovimientoInventarioProductoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoInventarioProductoCreateRequestDTO movimientoRequestDTO) {
        MovimientoInventarioProductoResponseDTO movimiento = inventarioProductoService.registrarMovimiento(movimientoRequestDTO);
        // Si los movimientos tuvieran su propio endpoint GET by ID:
        /*
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/inventario-productos/movimientos/{id}") // Asumiendo un endpoint hipotético
                .buildAndExpand(movimiento.getIdMovimientoProducto())
                .toUri();
        return ResponseEntity.created(location).body(movimiento);
        */
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @Operation(summary = "Obtener todos los movimientos de un registro de inventario de producto (paginado).",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<MovimientoInventarioProductoResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.")
    })
    @GetMapping("/{idInventarioProducto}/movimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<Page<MovimientoInventarioProductoResponseDTO>> getMovimientosByInventarioProductoId(
            @Parameter(description = "ID del registro de inventario de producto para obtener sus movimientos.", required = true) @PathVariable Integer idInventarioProducto,
            @Parameter(description = "Configuración de paginación.")
            @PageableDefault(size = 20, sort = "fechaMovimiento", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<MovimientoInventarioProductoResponseDTO> movimientos = inventarioProductoService.findMovimientosByInventarioProductoId(idInventarioProducto, pageable);
        return ResponseEntity.ok(movimientos);
    }

    @Operation(summary = "Obtener el stock actual de un registro de inventario de producto.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO, VENTAS o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actual obtenido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.")
    })
    @GetMapping("/{idInventarioProducto}/stock")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO', 'VENTAS') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<Integer> getStockActual(
            @Parameter(description = "ID del registro de inventario de producto.", required = true) @PathVariable Integer idInventarioProducto) {
        Integer stock = inventarioProductoService.getStockActual(idInventarioProducto);
        return ResponseEntity.ok(stock);
    }
}