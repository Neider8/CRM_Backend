package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO para la solicitud de creación o actualización de un cliente.")
public class ClienteRequestDTO {

    @Schema(description = "Tipo de documento de identificación del cliente. Valores permitidos: 'NIT' o 'Cédula'.",
            example = "NIT",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de documento no puede estar vacío.")
    @Pattern(regexp = "NIT|Cédula", message = "El tipo de documento debe ser NIT o Cédula.")
    private String tipoDocumento;

    @Schema(description = "Número único del documento de identificación del cliente. Debe ser único en el sistema.",
            example = "900123456-7",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El número de documento no puede estar vacío.")
    @Size(max = 20, message = "El número de documento no puede exceder los 20 caracteres.")
    private String numeroDocumento;

    @Schema(description = "Nombre completo o razón social del cliente.",
            example = "Telas del Pacífico S.A.S.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    @Size(max = 255, message = "El nombre del cliente no puede exceder los 255 caracteres.")
    private String nombreCliente;

    @Schema(description = "Dirección física principal del cliente.",
            example = "Calle 100 # 20 - 30, Bogotá D.C.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 255, message = "La dirección del cliente no puede exceder los 255 caracteres.")
    private String direccionCliente;

    @Schema(description = "Número de teléfono de contacto principal del cliente.",
            example = "3001234567",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 20, message = "El teléfono del cliente no puede exceder los 20 caracteres.")
    private String telefonoCliente;

    @Schema(description = "Correo electrónico de contacto principal del cliente. Debe ser un formato de email válido.",
            example = "contacto@telasdelpacifico.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional pero recomendado
            nullable = true)
    @Email(message = "El correo electrónico debe ser válido.")
    @Size(max = 100, message = "El correo del cliente no puede exceder los 100 caracteres.")
    private String correoCliente;

    // Constructores
    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String tipoDocumento, String numeroDocumento, String nombreCliente, String direccionCliente, String telefonoCliente, String correoCliente) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
    }

    // Getters y Setters
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }
}