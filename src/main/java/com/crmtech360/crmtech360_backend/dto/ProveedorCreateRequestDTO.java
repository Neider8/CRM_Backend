package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de creación de un nuevo proveedor.")
public class ProveedorCreateRequestDTO {

    @Schema(description = "Nombre comercial o de fantasía del proveedor.",
            example = "Insumos Textiles Andinos S.A.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre comercial del proveedor no puede estar vacío.")
    @Size(max = 255, message = "El nombre comercial no puede exceder los 255 caracteres.")
    private String nombreComercialProveedor;

    @Schema(description = "Razón social legal del proveedor, si es diferente del nombre comercial.",
            example = "Insumos Textiles Andinos Sociedad Anónima",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "La razón social no puede exceder los 255 caracteres.")
    private String razonSocialProveedor;

    @Schema(description = "Número de Identificación Tributaria (NIT) u otro identificador fiscal único del proveedor. Debe ser único en el sistema.",
            example = "900123456-1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El NIT del proveedor no puede estar vacío.")
    @Size(max = 20, message = "El NIT no puede exceder los 20 caracteres.")
    private String nitProveedor;

    @Schema(description = "Dirección física principal del proveedor.",
            example = "Carrera 7 # 70 - 20, Oficina 301, Bogotá D.C.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "La dirección no puede exceder los 255 caracteres.")
    private String direccionProveedor;

    @Schema(description = "Número de teléfono de contacto principal del proveedor.",
            example = "6013456789",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres.")
    private String telefonoProveedor;

    @Schema(description = "Correo electrónico de contacto principal del proveedor. Debe ser un formato de email válido.",
            example = "ventas@insumosandinos.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Email(message = "El correo electrónico debe ser válido.")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres.")
    private String correoProveedor;

    @Schema(description = "Nombre de la persona de contacto principal en la empresa proveedora.",
            example = "Luisa Fernanda Rojas",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El contacto principal no puede exceder los 255 caracteres.")
    private String contactoPrincipalProveedor;

    // Constructores
    public ProveedorCreateRequestDTO() {
    }

    public ProveedorCreateRequestDTO(String nombreComercialProveedor, String razonSocialProveedor, String nitProveedor, String direccionProveedor, String telefonoProveedor, String correoProveedor, String contactoPrincipalProveedor) {
        this.nombreComercialProveedor = nombreComercialProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.nitProveedor = nitProveedor;
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

    public String getNitProveedor() {
        return nitProveedor;
    }

    public void setNitProveedor(String nitProveedor) {
        this.nitProveedor = nitProveedor;
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