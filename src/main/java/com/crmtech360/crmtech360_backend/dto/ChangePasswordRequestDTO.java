package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de cambio de contraseña de un usuario.")
public class ChangePasswordRequestDTO {

    @Schema(description = "La contraseña actual del usuario. Requerida si el usuario cambia su propia contraseña; puede ser omitida por un administrador que fuerza el cambio.",
            example = "P@$$wOrdActual123",
            requiredMode = Schema.RequiredMode.REQUIRED) // Generalmente requerida para el flujo normal del usuario
    @NotBlank(message = "La contraseña actual es obligatoria.")
    private String currentPassword;

    @Schema(description = "La nueva contraseña deseada para la cuenta. Debe cumplir con las políticas de seguridad (mínimo 8 caracteres).",
            example = "NuevaP@$$wOrdSegura456",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La nueva contraseña es obligatoria.")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres.")
    private String newPassword;

    // Constructores
    public ChangePasswordRequestDTO() {}

    public ChangePasswordRequestDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    // Getters y Setters
    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}