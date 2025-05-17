package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema; // Para listas o sets
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "DTO para la respuesta de información detallada de un cliente, incluyendo sus contactos.")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único del cliente.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCliente;

    @Schema(description = "Tipo de documento de identificación del cliente (ej. 'NIT', 'Cédula').", example = "NIT", accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoDocumento;

    @Schema(description = "Número único del documento de identificación del cliente.", example = "900123456-7", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroDocumento;

    @Schema(description = "Nombre completo o razón social del cliente.", example = "Telas del Pacífico S.A.S.", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreCliente;

    @Schema(description = "Dirección física principal del cliente.", example = "Calle 100 # 20 - 30, Bogotá D.C.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String direccionCliente;

    @Schema(description = "Número de teléfono de contacto principal del cliente.", example = "3001234567", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String telefonoCliente;

    @Schema(description = "Correo electrónico de contacto principal del cliente.", example = "contacto@telasdelpacifico.com", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String correoCliente;

    @Schema(description = "Fecha y hora de creación del registro del cliente en el sistema.", example = "2023-01-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización del registro del cliente.", example = "2024-03-10T15:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Conjunto de contactos asociados a este cliente.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    // Swagger inferirá el tipo de ContactoClienteResponseDTO si está anotado correctamente.
    // Si fuera una lista, usarías @ArraySchema(schema = @Schema(implementation = ContactoClienteResponseDTO.class))
    // Para un Set, esto es generalmente suficiente si ContactoClienteResponseDTO está bien definido.
    private Set<ContactoClienteResponseDTO> contactosCliente;

    // Constructores
    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Integer idCliente, String tipoDocumento, String numeroDocumento, String nombreCliente, String direccionCliente, String telefonoCliente, String correoCliente, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, Set<ContactoClienteResponseDTO> contactosCliente) {
        this.idCliente = idCliente;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreCliente = nombreCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.contactosCliente = contactosCliente;
    }

    // Getters y Setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

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

    public Set<ContactoClienteResponseDTO> getContactosCliente() {
        return contactosCliente;
    }

    public void setContactosCliente(Set<ContactoClienteResponseDTO> contactosCliente) {
        this.contactosCliente = contactosCliente;
    }
}