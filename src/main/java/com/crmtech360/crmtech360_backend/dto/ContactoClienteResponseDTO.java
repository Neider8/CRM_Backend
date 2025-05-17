package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO para la respuesta de información detallada de un contacto de cliente.")
public class ContactoClienteResponseDTO {

    @Schema(description = "Identificador único del contacto del cliente.", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idContacto;

    // Comentario original:
    // No incluimos el idCliente aquí si el DTO siempre se presenta dentro de un ClienteResponseDTO
    // Si se puede obtener ContactoClienteResponseDTO de forma independiente, sí se incluiría idCliente.
    // @Schema(description = "ID del cliente al que este contacto pertenece.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    // private Integer idCliente;

    @Schema(description = "Nombre completo de la persona de contacto.", example = "Ana Pérez", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreContacto;

    @Schema(description = "Cargo o posición del contacto dentro de la empresa cliente.", example = "Gerente de Compras", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String cargoContacto;

    @Schema(description = "Número de teléfono del contacto.", example = "3101234567", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String telefonoContacto;

    @Schema(description = "Correo electrónico del contacto.", example = "ana.perez@example.com", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String correoContacto;

    // Constructores
    public ContactoClienteResponseDTO() {
    }

    public ContactoClienteResponseDTO(Integer idContacto, String nombreContacto, String cargoContacto, String telefonoContacto, String correoContacto) {
        this.idContacto = idContacto;
        this.nombreContacto = nombreContacto;
        this.cargoContacto = cargoContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
    }

    // Getters y Setters
    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
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