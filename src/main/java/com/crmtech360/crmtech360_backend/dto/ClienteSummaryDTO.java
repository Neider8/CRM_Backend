package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de un cliente, " +
        "usualmente utilizado en listados o como parte de otros DTOs (ej. en Órdenes de Venta).")
public class ClienteSummaryDTO {

    @Schema(description = "Identificador único del cliente.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCliente;

    @Schema(description = "Número único del documento de identificación del cliente.", example = "900123456-7", accessMode = Schema.AccessMode.READ_ONLY)
    private String numeroDocumento;

    @Schema(description = "Nombre completo o razón social del cliente.", example = "Telas del Pacífico S.A.S.", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreCliente;

    // Constructores
    public ClienteSummaryDTO() {
    }

    public ClienteSummaryDTO(Integer idCliente, String numeroDocumento, String nombreCliente) {
        this.idCliente = idCliente;
        this.numeroDocumento = numeroDocumento;
        this.nombreCliente = nombreCliente;
    }

    // Getters y Setters
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
}