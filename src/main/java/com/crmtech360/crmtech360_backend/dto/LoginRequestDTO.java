package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para la solicitud de inicio de sesión (autenticación) de un usuario.")
public class LoginRequestDTO {

    @Schema(description = "Nombre de usuario registrado en el sistema.",
            example = "usuarioAdmin",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String nombreUsuario;

    @Schema(description = "Contraseña asociada al nombre de usuario.",
            example = "P@$$wOrd123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password") // Indica que es un campo de contraseña, útil para UI de Swagger
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;

    // Constructores
    public LoginRequestDTO() {}

    public LoginRequestDTO(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}