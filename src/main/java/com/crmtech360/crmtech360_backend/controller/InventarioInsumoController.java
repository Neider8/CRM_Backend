package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO está aquí
import com.crmtech360.crmtech360_backend.service.InventarioInsumoService;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario-insumos")
@Tag(name = "Inventario de Insumos", description = "API para la gestión de stock de insumos (materias primas) y sus movimientos.")
@SecurityRequirement(name = "bearerAuth")
public class InventarioInsumoController {

    private final InventarioInsumoService inventarioInsumoService;

    public InventarioInsumoController(InventarioInsumoService inventarioInsumoService) {
        this.inventarioInsumoService = inventarioInsumoService;
    }

    @Operation(summary = "Crear un nuevo registro de inventario para un insumo en una ubicación específica.",
            description = "Permite establecer el stock inicial de un insumo en una ubicación. La combinación de insumo y ubicación debe ser única. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_GESTIONAR_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro de inventario de insumo creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioInsumoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. datos faltantes, insumo no existe).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un registro de inventario para este insumo en esta ubicación.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_GESTIONAR_INVENTARIO')")
    public ResponseEntity<InventarioInsumoResponseDTO> createInventarioInsumo(@Valid @RequestBody InventarioInsumoCreateRequestDTO createRequestDTO) {
        InventarioInsumoResponseDTO createdInventario = inventarioInsumoService.createInventarioInsumo(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdInventario.getIdInventarioInsumo())
                .toUri();
        return ResponseEntity.created(location).body(createdInventario);
    }

    @Operation(summary = "Obtener todos los registros de inventario de insumos (paginado).",
            description = "Devuelve una lista paginada de todos los registros de inventario de insumos. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inventario de insumos obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<InventarioInsumoResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<Page<InventarioInsumoResponseDTO>> getAllInventarioInsumos(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=insumo.nombreInsumo,asc)")
            @PageableDefault(size = 10) Pageable pageable) {
        Page<InventarioInsumoResponseDTO> inventarios = inventarioInsumoService.findAllInventarioInsumos(pageable);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Obtener un registro de inventario de insumo por su ID.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario de insumo encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioInsumoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{idInventarioInsumo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<InventarioInsumoResponseDTO> getInventarioInsumoById(
            @Parameter(description = "ID del registro de inventario de insumo.", required = true, example = "1") @PathVariable Integer idInventarioInsumo) {
        InventarioInsumoResponseDTO inventario = inventarioInsumoService.findInventarioInsumoById(idInventarioInsumo);
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Obtener un registro de inventario por insumo y ubicación.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro de inventario encontrado."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado para el insumo y ubicación especificados.")
    })
    @GetMapping("/insumo/{idInsumo}/ubicacion/{ubicacionInventario}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<InventarioInsumoResponseDTO> getInventarioByInsumoAndUbicacion(
            @Parameter(description = "ID del insumo.", required = true, example = "10") @PathVariable Integer idInsumo,
            @Parameter(description = "Nombre de la ubicación del inventario.", required = true, example = "Bodega Central") @PathVariable String ubicacionInventario) {
        InventarioInsumoResponseDTO inventario = inventarioInsumoService.findByInsumoAndUbicacion(idInsumo, ubicacionInventario);
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Obtener todos los registros de inventario para un insumo específico.",
            description = "Devuelve todas las ubicaciones y stocks para un insumo dado. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de registros de inventario para el insumo."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado.")
    })
    @GetMapping("/insumo/{idInsumo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<List<InventarioInsumoResponseDTO>> getInventariosByInsumoId(
            @Parameter(description = "ID del insumo para buscar sus inventarios.", required = true, example = "10") @PathVariable Integer idInsumo) {
        List<InventarioInsumoResponseDTO> inventarios = inventarioInsumoService.findInventariosByInsumoId(idInsumo);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Registrar un movimiento de inventario para un insumo.",
            description = "Actualiza el stock del insumo en la ubicación especificada (identificada por idInventarioInsumo). " +
                    "Se usa para entradas (ej. recepción de compra) o salidas (ej. consumo para producción). " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_REGISTRAR_MOVIMIENTO_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado y stock actualizado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoInventarioInsumoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. stock insuficiente para salida, tipo de movimiento incorrecto).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario de insumo no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/movimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_REGISTRAR_MOVIMIENTO_INVENTARIO')")
    public ResponseEntity<MovimientoInventarioInsumoResponseDTO> registrarMovimiento(
            @Valid @RequestBody MovimientoInventarioInsumoCreateRequestDTO movimientoRequestDTO) {
        MovimientoInventarioInsumoResponseDTO movimiento = inventarioInsumoService.registrarMovimiento(movimientoRequestDTO);
        // La URI para un movimiento podría ser /api/v1/inventario-insumos/movimientos/{idMovimientoInsumo}
        // o anidada bajo el inventario específico. Por simplicidad, devolvemos el objeto.
        // Si se quisiera una URI específica para el movimiento:
        /*
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/inventario-insumos/movimientos/{id}") // Asumiendo un endpoint para ver un movimiento
                .buildAndExpand(movimiento.getIdMovimientoInsumo())
                .toUri();
        return ResponseEntity.created(location).body(movimiento);
        */
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @Operation(summary = "Obtener todos los movimientos de un registro de inventario de insumo (paginado).",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.")
    })
    @GetMapping("/{idInventarioInsumo}/movimientos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<Page<MovimientoInventarioInsumoResponseDTO>> getMovimientosByInventarioInsumoId(
            @Parameter(description = "ID del registro de inventario de insumo para obtener sus movimientos.", required = true) @PathVariable Integer idInventarioInsumo,
            @Parameter(description = "Configuración de paginación.")
            @PageableDefault(size = 20, sort = "fechaMovimiento", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        Page<MovimientoInventarioInsumoResponseDTO> movimientos = inventarioInsumoService.findMovimientosByInventarioInsumoId(idInventarioInsumo, pageable);
        return ResponseEntity.ok(movimientos);
    }

    @Operation(summary = "Obtener el stock actual de un registro de inventario de insumo.",
            description = "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INVENTARIO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock actual obtenido."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado.")
    })
    @GetMapping("/{idInventarioInsumo}/stock")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INVENTARIO')")
    public ResponseEntity<BigDecimal> getStockActual(
            @Parameter(description = "ID del registro de inventario de insumo.", required = true) @PathVariable Integer idInventarioInsumo) {
        BigDecimal stock = inventarioInsumoService.getStockActual(idInventarioInsumo);
        return ResponseEntity.ok(stock);
    }
}