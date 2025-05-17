package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "DTO para la solicitud de actualización de un producto terminado existente. " +
        "Todos los campos son opcionales; solo se actualizarán los campos proporcionados. " +
        "La referencia del producto (referenciaProducto) generalmente no se actualiza por esta vía.")
public class ProductoUpdateRequestDTO {

    // Nota: La referencia del producto (referenciaProducto) usualmente no se actualiza
    // o requiere un proceso especial para evitar conflictos de SKU.

    @Schema(description = "Nuevo nombre descriptivo del producto.",
            example = "Camisa Algodón Premium Azul Rey Talla M",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 255, message = "El nombre del producto no puede exceder los 255 caracteres.")
    private String nombreProducto;

    @Schema(description = "Nueva descripción detallada adicional del producto.",
            example = "Camisa de algodón 100% Pima, manga corta, color azul rey intenso, ideal para ocasiones formales o casuales.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String descripcionProducto;

    @Schema(description = "Nueva talla del producto (ej. S, M, L, XL, Única, 38, 40).",
            example = "L",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "La talla del producto no puede exceder los 20 caracteres.") // Ajustado max a 20
    private String tallaProducto;

    @Schema(description = "Nuevo color principal o descriptivo del producto.",
            example = "Azul Marino",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El color del producto no puede exceder los 50 caracteres.")
    private String colorProducto;

    @Schema(description = "Nuevo tipo o categoría general del producto (ej. Camisa, Pantalón, Chaqueta, Vestido).",
            example = "Polo",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El tipo de producto no puede exceder los 50 caracteres.")
    private String tipoProducto;

    @Schema(description = "Nuevo género al que está dirigido el producto (ej. Hombre, Mujer, Unisex, Niño, Niña).",
            example = "Unisex",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "El género del producto no puede exceder los 20 caracteres.")
    private String generoProducto;

    @Schema(description = "Nuevo costo estimado o real de producción de una unidad del producto. Debe ser mayor que cero.",
            example = "38000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo de producción debe ser mayor que cero si se especifica.")
    @Digits(integer = 10, fraction = 2, message = "El costo de producción debe tener máximo 10 dígitos enteros y 2 decimales si se especifica.") // Ajustado integer
    private BigDecimal costoProduccion;

    @Schema(description = "Nuevo precio de venta al público (PVP) de una unidad del producto. Debe ser mayor que cero.",
            example = "85000.00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor que cero si se especifica.")
    @Digits(integer = 10, fraction = 2, message = "El precio de venta debe tener máximo 10 dígitos enteros y 2 decimales si se especifica.") // Ajustado integer
    private BigDecimal precioVenta;

    @Schema(description = "Nueva unidad de medida en la que se vende y gestiona el producto.",
            example = "Unidad",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "La unidad de medida no puede exceder los 20 caracteres.")
    private String unidadMedidaProducto;


    // Constructores
    public ProductoUpdateRequestDTO() {
    }

    public ProductoUpdateRequestDTO(String nombreProducto, String descripcionProducto, String tallaProducto, String colorProducto, String tipoProducto, String generoProducto, BigDecimal costoProduccion, BigDecimal precioVenta, String unidadMedidaProducto) {
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.tallaProducto = tallaProducto;
        this.colorProducto = colorProducto;
        this.tipoProducto = tipoProducto;
        this.generoProducto = generoProducto;
        this.costoProduccion = costoProduccion;
        this.precioVenta = precioVenta;
        this.unidadMedidaProducto = unidadMedidaProducto;
    }

    // Getters y Setters
    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getTallaProducto() {
        return tallaProducto;
    }

    public void setTallaProducto(String tallaProducto) {
        this.tallaProducto = tallaProducto;
    }

    public String getColorProducto() {
        return colorProducto;
    }

    public void setColorProducto(String colorProducto) {
        this.colorProducto = colorProducto;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getGeneroProducto() {
        return generoProducto;
    }

    public void setGeneroProducto(String generoProducto) {
        this.generoProducto = generoProducto;
    }

    public BigDecimal getCostoProduccion() {
        return costoProduccion;
    }

    public void setCostoProduccion(BigDecimal costoProduccion) {
        this.costoProduccion = costoProduccion;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getUnidadMedidaProducto() {
        return unidadMedidaProducto;
    }

    public void setUnidadMedidaProducto(String unidadMedidaProducto) {
        this.unidadMedidaProducto = unidadMedidaProducto;
    }
}