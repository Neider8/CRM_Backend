package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.*; // Asegúrate que ApiErrorResponseDTO esté aquí
import com.crmtech360.crmtech360_backend.service.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de cuentas de usuario del sistema.")
@SecurityRequirement(name = "bearerAuth") // Todos los endpoints aquí requieren autenticación
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Crear un nuevo usuario (solo para Administradores)",
            description = "Registra una nueva cuenta de usuario en el sistema. Este endpoint es típicamente para uso administrativo. " +
                    "El registro público se maneja a través de '/api/v1/auth/register'. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. nombre de usuario duplicado, empleado no encontrado).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol de Administrador."),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado (si se intenta asociar).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto - Nombre de usuario ya existe o empleado ya asociado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@Valid @RequestBody UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
        UsuarioResponseDTO createdUsuario = usuarioService.createUsuario(usuarioCreateRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUsuario.getIdUsuario())
                .toUri();
        return ResponseEntity.created(location).body(createdUsuario);
    }

    @Operation(summary = "Obtener todos los usuarios (paginado)",
            description = "Devuelve una lista paginada de todos los usuarios del sistema. " +
                    "Requiere rol ADMINISTRADOR o GERENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))), // Page<UsuarioResponseDTO>
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido.")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsuarios(
            @Parameter(description = "Configuración de paginación (ej. page=0&size=10&sort=nombreUsuario,asc)")
            @PageableDefault(size = 10, sort = "nombreUsuario") Pageable pageable) {
        Page<UsuarioResponseDTO> usuarios = usuarioService.findAllUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Obtener un usuario por su ID",
            description = "Devuelve los detalles de una cuenta de usuario específica. " +
                    "Accesible por el propio usuario, o por usuarios con rol ADMINISTRADOR o GERENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("@userSecurity.hasUserId(authentication, #id) or hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(
            @Parameter(description = "ID del usuario a obtener.", required = true, example = "1") @PathVariable Integer id) {
        UsuarioResponseDTO usuario = usuarioService.findUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener un usuario por su nombre de usuario",
            description = "Devuelve los detalles de una cuenta de usuario específica por su nombre de usuario. " +
                    "Accesible por el propio usuario, o por usuarios con rol ADMINISTRADOR o GERENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @GetMapping("/username/{nombreUsuario}")
    @PreAuthorize("@userSecurity.isOwner(authentication, #nombreUsuario) or hasAnyRole('ADMINISTRADOR', 'GERENTE')")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByNombreUsuario(
            @Parameter(description = "Nombre de usuario único.", required = true, example = "juan.perez") @PathVariable String nombreUsuario) {
        UsuarioResponseDTO usuario = usuarioService.findUsuarioByNombreUsuario(nombreUsuario);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar información de un usuario",
            description = "Permite al propio usuario o a un ADMINISTRADOR actualizar detalles de la cuenta de usuario (ej. email, rol si es admin). " +
                    "La contraseña se cambia mediante un endpoint dedicado. " +
                    "El nombre de usuario no se puede cambiar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido (ej. intentando cambiar el rol sin ser admin)."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto (ej. empleado a asociar ya tiene usuario).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("@userSecurity.hasUserId(authentication, #id) or hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar.", required = true) @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        UsuarioResponseDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioUpdateRequestDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Operation(summary = "Cambiar la contraseña de un usuario",
            description = "Permite al propio usuario o a un ADMINISTRADOR cambiar la contraseña de una cuenta. " +
                    "Si el usuario cambia su propia contraseña, debe proporcionar la contraseña actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente.",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Contraseña cambiada exitosamente."))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej. contraseña actual incorrecta, nueva contraseña no cumple políticas).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/{id}/change-password")
    @PreAuthorize("@userSecurity.hasUserId(authentication, #id) or hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> changePassword(
            @Parameter(description = "ID del usuario cuya contraseña se cambiará.", required = true) @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        usuarioService.changePassword(id, changePasswordRequestDTO);
        return ResponseEntity.ok("Contraseña cambiada exitosamente.");
    }


    @Operation(summary = "Eliminar un usuario por su ID (solo Administradores)",
            description = "Elimina una cuenta de usuario del sistema. Esta es una acción destructiva. " +
                    "Requiere rol ADMINISTRADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No Autorizado."),
            @ApiResponse(responseCode = "403", description = "Prohibido."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar.", required = true) @PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}