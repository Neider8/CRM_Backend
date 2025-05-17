package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de actualización de un proveedor existente. " +
        "Todos los campos son opcionales; solo se actualizarán los campos proporcionados. " +
        "El NIT del proveedor generalmente no se actualiza por esta vía.")
public class ProveedorUpdateRequestDTO {

    @Schema(description = "Nuevo nombre comercial o de fantasía del proveedor.",
            example = "Insumos Textiles Andinos Actualizado S.A.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El nombre comercial no puede exceder los 255 caracteres.")
    private String nombreComercialProveedor;

    @Schema(description = "Nueva razón social legal del proveedor, si es diferente del nombre comercial.",
            example = "Insumos Textiles Andinos Sociedad Anónima Renovada",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "La razón social no puede exceder los 255 caracteres.")
    private String razonSocialProveedor;

    // Nota: El NIT del proveedor (nitProveedor) usualmente no se actualiza
    // o requiere un proceso especial para evitar conflictos de identificación fiscal.

    @Schema(description = "Nueva dirección física principal del proveedor.",
            example = "Avenida Siempre Viva 123, Oficina 502, Bogotá D.C.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccionProveedor;

    @Schema(description = "Nuevo número de teléfono de contacto principal del proveedor.",
            example = "6019876543",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefonoProveedor;

    @Schema(description = "Nuevo correo electrónico de contacto principal del proveedor. Debe ser un formato de email válido.",
            example = "info@insumosandinosactualizado.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Email(message = "El correo electrónico debe ser válido.")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.")
    private String correoProveedor;

    @Schema(description = "Nuevo nombre de la persona de contacto principal en la empresa proveedora.",
            example = "Carlos Mendoza",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El contacto principal no puede exceder los 255 caracteres.")
    private String contactoPrincipalProveedor;

    // Constructores
    public ProveedorUpdateRequestDTO() {
    }

    public ProveedorUpdateRequestDTO(String nombreComercialProveedor, String razonSocialProveedor, String direccionProveedor, String telefonoProveedor, String correoProveedor, String contactoPrincipalProveedor) {
        this.nombreComercialProveedor = nombreComercialProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.direccionProveedor = direccionProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.correoProveedor = correoProveedor;
        this.contactoPrincipalProveedor = contactoPrincipalProveedor;
    }

    // Getters y Setters
    public String getNombreComercialProveedor() {
        return nombreComercialProveedor;
    }

    public void setNombreComercialProveedor(String nombreComercialProveedor) {
        this.nombreComercialProveedor = nombreComercialProveedor;
    }

    public String getRazonSocialProveedor() {
        return razonSocialProveedor;
    }

    public void setRazonSocialProveedor(String razonSocialProveedor) {
        this.razonSocialProveedor = razonSocialProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public String getContactoPrincipalProveedor() {
        return contactoPrincipalProveedor;
    }

    public void setContactoPrincipalProveedor(String contactoPrincipalProveedor) {
        this.contactoPrincipalProveedor = contactoPrincipalProveedor;
    }
}