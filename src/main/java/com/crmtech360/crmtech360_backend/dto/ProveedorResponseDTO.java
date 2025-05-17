package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de información detallada de un proveedor.")
public class ProveedorResponseDTO {

    @Schema(description = "Identificador único del proveedor.", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idProveedor;

    @Schema(description = "Nombre comercial o de fantasía del proveedor.", example = "Insumos Textiles Andinos S.A.", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreComercialProveedor;

    @Schema(description = "Razón social legal del proveedor, si es diferente del nombre comercial.", example = "Insumos Textiles Andinos Sociedad Anónima", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String razonSocialProveedor;

    @Schema(description = "Número de Identificación Tributaria (NIT) u otro identificador fiscal único del proveedor.", example = "900123456-1", accessMode = Schema.AccessMode.READ_ONLY)
    private String nitProveedor;

    @Schema(description = "Dirección física principal del proveedor.", example = "Carrera 7 # 70 - 20, Oficina 301, Bogotá D.C.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String direccionProveedor;

    @Schema(description = "Número de teléfono de contacto principal del proveedor.", example = "6013456789", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String telefonoProveedor;

    @Schema(description = "Correo electrónico de contacto principal del proveedor.", example = "ventas@insumosandinos.com", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String correoProveedor;

    @Schema(description = "Nombre de la persona de contacto principal en la empresa proveedora.", example = "Luisa Fernanda Rojas", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String contactoPrincipalProveedor;

    @Schema(description = "Fecha y hora de creación del registro del proveedor en el sistema.", example = "2022-11-01T15:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización del registro del proveedor.", example = "2024-02-10T09:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public ProveedorResponseDTO() {
    }

    public ProveedorResponseDTO(Integer idProveedor, String nombreComercialProveedor, String razonSocialProveedor, String nitProveedor, String direccionProveedor, String telefonoProveedor, String correoProveedor, String contactoPrincipalProveedor, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idProveedor = idProveedor;
        this.nombreComercialProveedor = nombreComercialProveedor;
        this.razonSocialProveedor = razonSocialProveedor;
        this.nitProveedor = nitProveedor;
        this.direccionProveedor = direccionProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.correoProveedor = correoProveedor;
        this.contactoPrincipalProveedor = contactoPrincipalProveedor;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters
    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}