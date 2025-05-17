package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de importar este DTO
import com.crmtech360.crmtech360_backend.dto.PagoCobroCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.PagoCobroResponseDTO;
import com.crmtech360.crmtech360_backend.dto.PagoCobroUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.service.PagoCobroService;
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
@RequestMapping("/api/v1/pagos-cobros")
@Tag(name = "Pagos y Cobros", description = "API para la gestión de transacciones financieras (pagos a proveedores y cobros a clientes).")
@SecurityRequirement(name = "bearerAuth")
public class PagoCobroController {

    private final PagoCobroService pagoCobroService;

    public PagoCobroController(PagoCobroService pagoCobroService) {
        this.pagoCobroService = pagoCobroService;
    }

    @Operation(summary = "Registrar un nuevo pago o cobro",
            description = "Crea una nueva transacción financiera, que puede ser un pago (asociado a una orden de compra) o un cobro (asociado a una orden de venta). " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS (para cobros) o permiso PERMISO_REGISTRAR_PAGO_COBRO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transacción registrada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoCobroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. orden de venta/compra no existe, tipo de transacción inválido).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de Venta u Orden de Compra no encontrada (si se especifica).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_REGISTRAR_PAGO_COBRO')")
    public ResponseEntity<PagoCobroResponseDTO> createPagoCobro(@Valid @RequestBody PagoCobroCreateRequestDTO createRequestDTO) {
        PagoCobroResponseDTO createdTransaccion = pagoCobroService.createPagoCobro(createRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTransaccion.getIdPagoCobro())
                .toUri();
        return ResponseEntity.created(location).body(createdTransaccion);
    }

    @Operation(summary = "Obtener todas las transacciones de pagos y cobros (paginado)",
            description = "Devuelve una lista paginada de todas las transacciones financieras. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_PAGOS_COBROS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<PagoCobroResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_PAGOS_COBROS')")
    public ResponseEntity<Page<PagoCobroResponseDTO>> getAllPagosCobros(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=fechaRegistroTransaccion,desc)")
            @PageableDefault(size = 10, sort = "fechaRegistroTransaccion", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PagoCobroResponseDTO> transacciones = pagoCobroService.findAllPagosCobros(pageable);
        return ResponseEntity.ok(transacciones);
    }

    @Operation(summary = "Obtener una transacción de pago/cobro por su ID",
            description = "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_PAGOS_COBROS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoCobroResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{idPagoCobro}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_PAGOS_COBROS')")
    public ResponseEntity<PagoCobroResponseDTO> getPagoCobroById(
            @Parameter(description = "ID de la transacción de pago/cobro.", required = true) @PathVariable Integer idPagoCobro) {
        PagoCobroResponseDTO transaccion = pagoCobroService.findPagoCobroById(idPagoCobro);
        return ResponseEntity.ok(transaccion);
    }

    @Operation(summary = "Obtener transacciones por tipo (PAGO o COBRO)",
            description = "Filtra las transacciones por PAGO o COBRO. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_PAGOS_COBROS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones filtrada por tipo."),
            @ApiResponse(responseCode = "400", description = "Tipo de transacción inválido."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping("/tipo/{tipoTransaccion}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_PAGOS_COBROS')")
    public ResponseEntity<List<PagoCobroResponseDTO>> getPagosCobrosByTipo(
            @Parameter(description = "Tipo de transacción ('PAGO' o 'COBRO').", required = true, example = "COBRO") @PathVariable String tipoTransaccion,
            @Parameter(description = "Configuración de paginación.")
            @PageableDefault(size = 10, sort = "fechaRegistroTransaccion", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PagoCobroResponseDTO> transacciones = pagoCobroService.findPagosCobrosByTipoTransaccion(tipoTransaccion, pageable);
        return ResponseEntity.ok(transacciones);
    }

    @Operation(summary = "Obtener todos los cobros asociados a una orden de venta",
            description = "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_PAGOS_COBROS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cobros para la orden de venta."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de venta no encontrada.")
    })
    @GetMapping("/orden-venta/{idOrdenVenta}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_PAGOS_COBROS')")
    public ResponseEntity<List<PagoCobroResponseDTO>> getCobrosByOrdenVentaId(
            @Parameter(description = "ID de la orden de venta.", required = true) @PathVariable Integer idOrdenVenta,
            @Parameter(description = "Configuración de paginación.")
            @PageableDefault(size = 10, sort = "fechaRegistroTransaccion", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PagoCobroResponseDTO> cobros = pagoCobroService.findCobrosByOrdenVentaId(idOrdenVenta, pageable);
        return ResponseEntity.ok(cobros);
    }

    @Operation(summary = "Obtener todos los pagos asociados a una orden de compra",
            description = "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_PAGOS_COBROS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pagos para la orden de compra."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Orden de compra no encontrada.")
    })
    @GetMapping("/orden-compra/{idOrdenCompra}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_PAGOS_COBROS')")
    public ResponseEntity<List<PagoCobroResponseDTO>> getPagosByOrdenCompraId(
            @Parameter(description = "ID de la orden de compra.", required = true) @PathVariable Integer idOrdenCompra,
            @Parameter(description = "Configuración de paginación.")
            @PageableDefault(size = 10, sort = "fechaRegistroTransaccion", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PagoCobroResponseDTO> pagos = pagoCobroService.findPagosByOrdenCompraId(idOrdenCompra, pageable);
        return ResponseEntity.ok(pagos);
    }

    @Operation(summary = "Actualizar una transacción de pago/cobro existente",
            description = "Permite modificar el monto, método de pago o referencias de una transacción. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_PAGO_COBRO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagoCobroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o transacción no editable."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada.")
    })
    @PutMapping("/{idPagoCobro}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_PAGO_COBRO')")
    public ResponseEntity<PagoCobroResponseDTO> updatePagoCobro(
            @Parameter(description = "ID de la transacción a actualizar.", required = true) @PathVariable Integer idPagoCobro,
            @Valid @RequestBody PagoCobroUpdateRequestDTO updateRequestDTO) {
        PagoCobroResponseDTO updatedTransaccion = pagoCobroService.updatePagoCobro(idPagoCobro, updateRequestDTO);
        return ResponseEntity.ok(updatedTransaccion);
    }

    @Operation(summary = "Anular una transacción de pago/cobro",
            description = "Marca una transacción como anulada. Esto puede revertir saldos o requerir ajustes manuales. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_ANULAR_PAGO_COBRO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacción anulada exitosamente."),
            @ApiResponse(responseCode = "400", description = "No se puede anular la transacción en su estado actual."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada.")
    })
    @PostMapping("/{idPagoCobro}/anular")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_ANULAR_PAGO_COBRO')")
    public ResponseEntity<Void> anularPagoCobro(
            @Parameter(description = "ID de la transacción a anular.", required = true) @PathVariable Integer idPagoCobro) {
        pagoCobroService.anularPagoCobro(idPagoCobro);
        return ResponseEntity.ok().build();
    }
}