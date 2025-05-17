package com.crmtech360.crmtech360_backend.controller;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO;
import com.crmtech360.crmtech360_backend.dto.JwtResponseDTO;
import com.crmtech360.crmtech360_backend.dto.LoginRequestDTO;
import com.crmtech360.crmtech360_backend.dto.UsuarioCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.UsuarioResponseDTO;
import com.crmtech360.crmtech360_backend.security.jwt.JwtUtil;
import com.crmtech360.crmtech360_backend.service.UsuarioService;
import com.crmtech360.crmtech360_backend.service.impl.UserDetailsServiceImpl; // Necesario para la inyección en el constructor

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "API para la autenticación de usuarios, registro y gestión de tokens JWT.")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    // UserDetailsServiceImpl es inyectado a través del constructor pero no necesita ser un campo
    // si no se usa directamente en los métodos de esta clase, más allá de la configuración de Spring Security.

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UsuarioService usuarioService,
                          UserDetailsServiceImpl userDetailsServiceImpl) { // Spring usa userDetailsServiceImpl para configurar el AuthenticationManager
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Autenticar usuario y obtener token JWT",
            description = "Proporciona nombre de usuario y contraseña para obtener un token de acceso. " +
                    "Este endpoint es público y no requiere autenticación previa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, token JWT generado.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud de login inválida (ej. campos vacíos).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas (no autorizado).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor durante la autenticación.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            log.info("Intentando autenticar al usuario: {}", loginRequestDTO.getNombreUsuario());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getNombreUsuario(),
                            loginRequestDTO.getContrasena()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateTokenFromUserDetails(userDetails);

            UsuarioResponseDTO usuarioInfo = usuarioService.findUsuarioByNombreUsuario(userDetails.getUsername());

            log.info("Usuario {} autenticado exitosamente. Token generado.", userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponseDTO(
                    jwt,
                    usuarioInfo.getIdUsuario(),
                    userDetails.getUsername(),
                    usuarioInfo.getRolUsuario()
            ));

        } catch (BadCredentialsException e) {
            log.warn("Credenciales inválidas para el usuario: {}", loginRequestDTO.getNombreUsuario());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiErrorResponseDTO(
                            HttpStatus.UNAUTHORIZED.value(),
                            "No Autorizado",
                            "Credenciales de inicio de sesión inválidas.",
                            "/api/v1/auth/login"
                    ));
        } catch (Exception e) {
            log.error("Error durante la autenticación del usuario: {}. Error: {}", loginRequestDTO.getNombreUsuario(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponseDTO(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error Interno del Servidor",
                            "Ocurrió un problema durante el proceso de autenticación.",
                            "/api/v1/auth/login"
                    ));
        }
    }

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario en el sistema. Este endpoint es público. " +
                    "El rol por defecto y la asociación con un empleado (si aplica) se manejan en el servicio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos (ej. validación fallida, empleado no encontrado si se proporciona ID).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto, el nombre de usuario ya existe o el empleado (si se proporciona ID) ya está asociado a otro usuario.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registerUser(@Valid @RequestBody UsuarioCreateRequestDTO signUpRequest) {
        log.info("Intentando registrar nuevo usuario: {}", signUpRequest.getNombreUsuario());
        UsuarioResponseDTO createdUsuario = usuarioService.createUsuario(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/usuarios/{id}")
                .buildAndExpand(createdUsuario.getIdUsuario()).toUri();
        log.info("Usuario {} registrado exitosamente con ID: {}", createdUsuario.getNombreUsuario(), createdUsuario.getIdUsuario());
        return ResponseEntity.created(location).body(createdUsuario);
    }
}