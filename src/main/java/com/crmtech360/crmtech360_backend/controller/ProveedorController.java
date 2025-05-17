package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de importar este DTO
import com.crmtech360.crmtech360_backend.dto.ProveedorCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.service.ProveedorService;
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

@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedores", description = "API para la gestión de proveedores de insumos y servicios.")
@SecurityRequirement(name = "bearerAuth")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @Operation(summary = "Crear un nuevo proveedor",
            description = "Registra un nuevo proveedor en el sistema. El NIT del proveedor debe ser único. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_CREAR_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProveedorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. NIT duplicado, datos faltantes).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un proveedor con el mismo NIT.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_CREAR_PROVEEDORES')")
    public ResponseEntity<ProveedorResponseDTO> createProveedor(@Valid @RequestBody ProveedorCreateRequestDTO proveedorCreateRequestDTO) {
        ProveedorResponseDTO createdProveedor = proveedorService.createProveedor(proveedorCreateRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProveedor.getIdProveedor())
                .toUri();
        return ResponseEntity.created(location).body(createdProveedor);
    }

    @Operation(summary = "Obtener todos los proveedores (paginado)",
            description = "Devuelve una lista paginada de todos los proveedores. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<ProveedorResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_PROVEEDORES')")
    public ResponseEntity<Page<ProveedorResponseDTO>> getAllProveedores(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=nombreComercialProveedor,asc)")
            @PageableDefault(size = 10, sort = "nombreComercialProveedor") Pageable pageable) {
        Page<ProveedorResponseDTO> proveedores = proveedorService.findAllProveedores(pageable);
        return ResponseEntity.ok(proveedores);
    }

    @Operation(summary = "Obtener un proveedor por su ID",
            description = "Devuelve los detalles de un proveedor específico. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProveedorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_PROVEEDORES')")
    public ResponseEntity<ProveedorResponseDTO> getProveedorById(
            @Parameter(description = "ID del proveedor a obtener.", required = true, example = "1") @PathVariable Integer id) {
        ProveedorResponseDTO proveedor = proveedorService.findProveedorById(id);
        return ResponseEntity.ok(proveedor);
    }

    @Operation(summary = "Obtener un proveedor por su NIT",
            description = "Devuelve los detalles de un proveedor específico basado en su NIT. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_VER_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProveedorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/nit/{nit}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_VER_PROVEEDORES')")
    public ResponseEntity<ProveedorResponseDTO> getProveedorByNit(
            @Parameter(description = "NIT del proveedor a obtener.", required = true, example = "900123456-1") @PathVariable String nit) {
        ProveedorResponseDTO proveedor = proveedorService.findProveedorByNit(nit);
        return ResponseEntity.ok(proveedor);
    }

    @Operation(summary = "Actualizar un proveedor existente",
            description = "Permite modificar los detalles de un proveedor. El NIT no se puede cambiar; si es necesario, se debe crear un nuevo proveedor. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProveedorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_PROVEEDORES')")
    public ResponseEntity<ProveedorResponseDTO> updateProveedor(
            @Parameter(description = "ID del proveedor a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody ProveedorUpdateRequestDTO proveedorUpdateRequestDTO) {
        ProveedorResponseDTO updatedProveedor = proveedorService.updateProveedor(id, proveedorUpdateRequestDTO);
        return ResponseEntity.ok(updatedProveedor);
    }

    @Operation(summary = "Eliminar un proveedor por su ID",
            description = "Elimina un proveedor del sistema. Puede fallar si el proveedor tiene órdenes de compra asociadas. " +
                    "Requiere rol ADMINISTRADOR o permiso PERMISO_ELIMINAR_PROVEEDORES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - El proveedor no se puede eliminar (ej. tiene órdenes de compra asociadas).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERMISO_ELIMINAR_PROVEEDORES')")
    public ResponseEntity<Void> deleteProveedor(
            @Parameter(description = "ID del proveedor a eliminar.", required = true) @PathVariable Integer id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.noContent().build();
    }
}