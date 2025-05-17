package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema

@Schema(description = "DTO resumido para representar información básica de un Proveedor, " +
        "usualmente utilizado cuando se vincula a otras entidades (ej. en una Orden de Compra).")
public class ProveedorSummaryDTO {

    @Schema(description = "Identificador único del proveedor.", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idProveedor;

    @Schema(description = "Número de Identificación Tributaria (NIT) u otro identificador fiscal único del proveedor.", example = "900123456-1", accessMode = Schema.AccessMode.READ_ONLY)
    private String nitProveedor;

    @Schema(description = "Nombre comercial o de fantasía del proveedor.", example = "Insumos Textiles Andinos S.A.", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreComercialProveedor;

    // Constructores
    public ProveedorSummaryDTO() {
    }

    public ProveedorSummaryDTO(Integer idProveedor, String nitProveedor, String nombreComercialProveedor) {
        this.idProveedor = idProveedor;
        this.nitProveedor = nitProveedor;
        this.nombreComercialProveedor = nombreComercialProveedor;
    }

    // Getters y Setters
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public String getNitProveedor() { return nitProveedor; }
    public void setNitProveedor(String nitProveedor) { this.nitProveedor = nitProveedor; }
    public String getNombreComercialProveedor() { return nombreComercialProveedor; }
    public void setNombreComercialProveedor(String nombreComercialProveedor) { this.nombreComercialProveedor = nombreComercialProveedor; }
}