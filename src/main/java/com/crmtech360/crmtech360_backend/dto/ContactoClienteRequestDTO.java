package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para la solicitud de creación o actualización de un contacto de cliente.")
public class ContactoClienteRequestDTO {

    @Schema(description = "Identificador único del cliente al que pertenece este contacto. Requerido para asociar el contacto.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del cliente es obligatorio.")
    private Integer idCliente;

    @Schema(description = "Nombre completo de la persona de contacto.",
            example = "Ana Pérez",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del contacto no puede estar vacío.")
    @Size(max = 255, message = "El nombre del contacto no puede exceder los 255 caracteres.")
    private String nombreContacto;

    @Schema(description = "Cargo o posición del contacto dentro de la empresa cliente.",
            example = "Gerente de Compras",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 100, message = "El cargo del contacto no puede exceder los 100 caracteres.")
    private String cargoContacto;

    @Schema(description = "Número de teléfono del contacto.",
            example = "3101234567",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Size(max = 20, message = "El teléfono del contacto no puede exceder los 20 caracteres.")
    private String telefonoContacto;

    @Schema(description = "Correo electrónico del contacto. Debe ser un formato de email válido.",
            example = "ana.perez@example.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional
            nullable = true)
    @Email(message = "El correo electrónico del contacto debe ser válido.")
    @Size(max = 100, message = "El correo del contacto no puede exceder los 100 caracteres.")
    private String correoContacto;

    // Constructores
    public ContactoClienteRequestDTO() {
    }

    public ContactoClienteRequestDTO(Integer idCliente, String nombreContacto, String cargoContacto, String telefonoContacto, String correoContacto) {
        this.idCliente = idCliente;
        this.nombreContacto = nombreContacto;
        this.cargoContacto = cargoContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
    }

    // Getters y Setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getCargoContacto() {
        return cargoContacto;
    }

    public void setCargoContacto(String cargoContacto) {
        this.cargoContacto = cargoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }
}