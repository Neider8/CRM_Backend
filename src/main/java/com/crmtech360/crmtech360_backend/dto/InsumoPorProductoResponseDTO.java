package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;

@Schema(description = "DTO para la respuesta de un ítem en la Lista de Materiales (BOM) de un producto, " +
        "detallando un insumo y la cantidad requerida.")
public class InsumoPorProductoResponseDTO {

    @Schema(description = "Identificador único del producto al que pertenece este ítem del BOM.",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idProducto;

    @Schema(description = "Referencia única del producto (para contexto).",
            example = "CAM-AZ-M-001",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String referenciaProducto;

    @Schema(description = "Nombre del producto (para contexto).",
            example = "Camisa Algodón Azul Talla M",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreProducto;

    @Schema(description = "Identificador único del insumo que forma parte del BOM.",
            example = "15",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInsumo;

    @Schema(description = "Nombre del insumo (para contexto).",
            example = "Tela Algodón Jersey Azul Rey",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreInsumo;

    @Schema(description = "Unidad de medida en la que se gestiona el insumo (para contexto).",
            example = "Metros",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String unidadMedidaInsumo;

    @Schema(description = "Cantidad del insumo requerida para producir una unidad del producto.",
            example = "2.750",
            accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal cantidadRequerida;

    // Constructores
    public InsumoPorProductoResponseDTO() {
    }

    public InsumoPorProductoResponseDTO(Integer idProducto, String referenciaProducto, String nombreProducto, Integer idInsumo, String nombreInsumo, String unidadMedidaInsumo, BigDecimal cantidadRequerida) {
        this.idProducto = idProducto;
        this.referenciaProducto = referenciaProducto;
        this.nombreProducto = nombreProducto;
        this.idInsumo = idInsumo;
        this.nombreInsumo = nombreInsumo;
        this.unidadMedidaInsumo = unidadMedidaInsumo;
        this.cantidadRequerida = cantidadRequerida;
    }

    // Getters y Setters
    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getReferenciaProducto() {
        return referenciaProducto;
    }

    public void setReferenciaProducto(String referenciaProducto) {
        this.referenciaProducto = referenciaProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getUnidadMedidaInsumo() {
        return unidadMedidaInsumo;
    }

    public void setUnidadMedidaInsumo(String unidadMedidaInsumo) {
        this.unidadMedidaInsumo = unidadMedidaInsumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
}