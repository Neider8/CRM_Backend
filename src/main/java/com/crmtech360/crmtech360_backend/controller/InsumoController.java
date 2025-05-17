package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO; // Asegúrate de importar este DTO
import com.crmtech360.crmtech360_backend.dto.InsumoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.service.InsumoService;
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
@RequestMapping("/api/v1/insumos")
@Tag(name = "Insumos", description = "API para la gestión de insumos (materias primas y componentes).")
@SecurityRequirement(name = "bearerAuth")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @Operation(summary = "Crear un nuevo insumo",
            description = "Registra un nuevo insumo en el sistema. El nombre del insumo debe ser único. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_CREAR_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Insumo creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsumoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida debido a datos incorrectos.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un insumo con el mismo nombre.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_CREAR_INSUMOS')")
    public ResponseEntity<InsumoResponseDTO> createInsumo(@Valid @RequestBody InsumoCreateRequestDTO insumoCreateRequestDTO) {
        InsumoResponseDTO createdInsumo = insumoService.createInsumo(insumoCreateRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdInsumo.getIdInsumo())
                .toUri();
        return ResponseEntity.created(location).body(createdInsumo);
    }

    @Operation(summary = "Obtener todos los insumos (paginado)",
            description = "Devuelve una lista paginada de todos los insumos. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de insumos obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<InsumoResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INSUMOS')")
    public ResponseEntity<Page<InsumoResponseDTO>> getAllInsumos(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=nombreInsumo,asc)")
            @PageableDefault(size = 10, sort = "nombreInsumo") Pageable pageable) {
        Page<InsumoResponseDTO> insumos = insumoService.findAllInsumos(pageable);
        return ResponseEntity.ok(insumos);
    }

    @Operation(summary = "Obtener un insumo por su ID",
            description = "Devuelve los detalles de un insumo específico. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insumo encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsumoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INSUMOS')")
    public ResponseEntity<InsumoResponseDTO> getInsumoById(
            @Parameter(description = "ID del insumo a obtener.", required = true, example = "1") @PathVariable Integer id) {
        InsumoResponseDTO insumo = insumoService.findInsumoById(id);
        return ResponseEntity.ok(insumo);
    }

    @Operation(summary = "Obtener un insumo por su nombre",
            description = "Devuelve los detalles de un insumo específico basado en su nombre. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, OPERARIO o permiso PERMISO_VER_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insumo encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsumoResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado.")
    })
    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'OPERARIO') or hasAuthority('PERMISO_VER_INSUMOS')")
    public ResponseEntity<InsumoResponseDTO> getInsumoByNombre(
            @Parameter(description = "Nombre del insumo a obtener.", required = true, example = "Tela Algodón Azul") @PathVariable String nombre) {
        InsumoResponseDTO insumo = insumoService.findInsumoByNombre(nombre);
        return ResponseEntity.ok(insumo);
    }

    @Operation(summary = "Actualizar un insumo existente",
            description = "Permite modificar los detalles de un insumo existente. " +
                    "Requiere rol ADMINISTRADOR, GERENTE o permiso PERMISO_EDITAR_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insumo actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = InsumoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado."),
            @ApiResponse(responseCode = "409", description = "Conflicto, el nuevo nombre de insumo ya existe.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE') or hasAuthority('PERMISO_EDITAR_INSUMOS')")
    public ResponseEntity<InsumoResponseDTO> updateInsumo(
            @Parameter(description = "ID del insumo a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody InsumoUpdateRequestDTO insumoUpdateRequestDTO) {
        InsumoResponseDTO updatedInsumo = insumoService.updateInsumo(id, insumoUpdateRequestDTO);
        return ResponseEntity.ok(updatedInsumo);
    }

    @Operation(summary = "Eliminar un insumo por su ID",
            description = "Elimina un insumo del sistema. Esta acción puede fallar si el insumo está en uso. " +
                    "Requiere rol ADMINISTRADOR o permiso PERMISO_ELIMINAR_INSUMOS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Insumo eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Insumo no encontrado."),
            @ApiResponse(responseCode = "409", description = "Conflicto, el insumo no se puede eliminar (ej. está en uso en BOM, órdenes de compra o inventario).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERMISO_ELIMINAR_INSUMOS')")
    public ResponseEntity<Void> deleteInsumo(
            @Parameter(description = "ID del insumo a eliminar.", required = true) @PathVariable Integer id) {
        insumoService.deleteInsumo(id);
        return ResponseEntity.noContent().build();
    }
}