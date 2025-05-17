package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO esté aquí o importado explícitamente
import com.crmtech360.crmtech360_backend.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "API para la gestión de clientes y sus contactos.")
@SecurityRequirement(name = "bearerAuth") // Aplica a todos los endpoints en este controlador
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Crear un nuevo cliente",
            description = "Permite registrar un nuevo cliente en el sistema. El número de documento debe ser único. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_CREAR_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida debido a datos incorrectos (ej. validación fallida).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado - Se requiere token JWT o token inválido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido - El usuario no tiene los permisos necesarios.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Ya existe un cliente con el mismo número de documento.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_CREAR_CLIENTES')")
    public ResponseEntity<ClienteResponseDTO> createCliente(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO createdCliente = clienteService.createCliente(clienteRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCliente.getIdCliente())
                .toUri();
        return ResponseEntity.created(location).body(createdCliente);
    }

    @Operation(summary = "Obtener todos los clientes (paginado)",
            description = "Devuelve una lista paginada de todos los clientes. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), //Page<ClienteResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_CLIENTES')")
    public ResponseEntity<Page<ClienteResponseDTO>> getAllClientes(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=nombreCliente,asc)")
            @PageableDefault(size = 10, sort = "nombreCliente") Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.findAllClientes(pageable);
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Obtener un cliente por su ID",
            description = "Devuelve los detalles de un cliente específico. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Prohibido.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_CLIENTES')")
    public ResponseEntity<ClienteResponseDTO> getClienteById(
            @Parameter(description = "ID del cliente a obtener.", required = true, example = "1") @PathVariable Integer id) {
        ClienteResponseDTO cliente = clienteService.findClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Obtener un cliente por su número de documento",
            description = "Devuelve los detalles de un cliente específico basado en su número de documento. " +
                    "Requiere rol ADMINISTRADOR, GERENTE, VENTAS o permiso PERMISO_VER_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @GetMapping("/documento/{numeroDocumento}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_CLIENTES')")
    public ResponseEntity<ClienteResponseDTO> getClienteByNumeroDocumento(
            @Parameter(description = "Número de documento del cliente a obtener.", required = true, example = "123456789") @PathVariable String numeroDocumento) {
        ClienteResponseDTO cliente = clienteService.findClienteByNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Actualizar un cliente existente",
            description = "Permite modificar los detalles de un cliente existente. " +
                    "Requiere rol ADMINISTRADOR, VENTAS o permiso PERMISO_EDITAR_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado."),
            @ApiResponse(responseCode = "409", description = "Conflicto, el nuevo número de documento ya existe para otro cliente.")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_CLIENTES')")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @Parameter(description = "ID del cliente a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO updatedCliente = clienteService.updateCliente(id, clienteRequestDTO);
        return ResponseEntity.ok(updatedCliente);
    }

    @Operation(summary = "Eliminar un cliente por su ID",
            description = "Elimina un cliente del sistema. Esta acción no se puede deshacer. " +
                    "Requiere rol ADMINISTRADOR o permiso PERMISO_ELIMINAR_CLIENTES.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado."),
            @ApiResponse(responseCode = "409", description = "Conflicto, el cliente no se puede eliminar (ej. tiene órdenes asociadas).")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERMISO_ELIMINAR_CLIENTES')")
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "ID del cliente a eliminar.", required = true) @PathVariable Integer id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints para Contactos de Cliente ---

    @Operation(summary = "Añadir un contacto a un cliente existente",
            description = "Requiere los mismos permisos que para crear o editar un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contacto añadido exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactoClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @PostMapping("/{idCliente}/contactos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_CLIENTES') or hasAuthority('PERMISO_CREAR_CLIENTES')")
    public ResponseEntity<ContactoClienteResponseDTO> addContactoToCliente(
            @Parameter(description = "ID del cliente al que se añade el contacto.", required = true) @PathVariable Integer idCliente,
            @Valid @RequestBody ContactoClienteRequestDTO contactoRequestDTO) {
        ContactoClienteResponseDTO createdContacto = clienteService.addContactoToCliente(idCliente, contactoRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idContacto}")
                .buildAndExpand(createdContacto.getIdContacto())
                .toUri();
        return ResponseEntity.created(location).body(createdContacto);
    }

    @Operation(summary = "Obtener todos los contactos de un cliente específico",
            description = "Requiere los mismos permisos que para ver un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contactos obtenida exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado.")
    })
    @GetMapping("/{idCliente}/contactos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE', 'VENTAS') or hasAuthority('PERMISO_VER_CLIENTES')")
    public ResponseEntity<List<ContactoClienteResponseDTO>> getContactosByClienteId(
            @Parameter(description = "ID del cliente para obtener sus contactos.", required = true) @PathVariable Integer idCliente) {
        List<ContactoClienteResponseDTO> contactos = clienteService.findContactosByClienteId(idCliente);
        return ResponseEntity.ok(contactos);
    }

    @Operation(summary = "Actualizar un contacto específico de un cliente",
            description = "Requiere los mismos permisos que para editar un cliente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contacto actualizado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente o Contacto no encontrado.")
    })
    @PutMapping("/{idCliente}/contactos/{idContacto}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'VENTAS') or hasAuthority('PERMISO_EDITAR_CLIENTES')")
    public ResponseEntity<ContactoClienteResponseDTO> updateContactoCliente(
            @Parameter(description = "ID del cliente.", required = true) @PathVariable Integer idCliente,
            @Parameter(description = "ID del contacto a actualizar.", required = true) @PathVariable Integer idContacto,
            @Valid @RequestBody ContactoClienteRequestDTO contactoRequestDTO) {
        ContactoClienteResponseDTO updatedContacto = clienteService.updateContactoCliente(idCliente, idContacto, contactoRequestDTO);
        return ResponseEntity.ok(updatedContacto);
    }

    @Operation(summary = "Eliminar un contacto específico de un cliente",
            description = "Requiere los mismos permisos que para eliminar un cliente o un permiso más específico si se desea.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contacto eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Cliente o Contacto no encontrado.")
    })
    @DeleteMapping("/{idCliente}/contactos/{idContacto}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERMISO_ELIMINAR_CLIENTES')")
    public ResponseEntity<Void> deleteContactoCliente(
            @Parameter(description = "ID del cliente.", required = true) @PathVariable Integer idCliente,
            @Parameter(description = "ID del contacto a eliminar.", required = true) @PathVariable Integer idContacto) {
        clienteService.deleteContactoCliente(idCliente, idContacto);
        return ResponseEntity.noContent().build();
    }
}