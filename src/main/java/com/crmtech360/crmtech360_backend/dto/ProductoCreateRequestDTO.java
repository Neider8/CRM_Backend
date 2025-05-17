package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
// import java.time.LocalDate; // No se usa LocalDate aquí directamente

@Schema(description = "DTO para la solicitud de creación de un nuevo producto terminado.")
public class ProductoCreateRequestDTO {

    @Schema(description = "Referencia única del producto, utilizada para identificación y SKU.",
            example = "CAM-AZ-M-001",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La referencia del producto no puede estar vacía.")
    @Size(max = 50, message = "La referencia del producto no puede exceder los 50 caracteres.")
    private String referenciaProducto;

    @Schema(description = "Nombre descriptivo del producto.",
            example = "Camisa Algodón Clásica Azul Talla M",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    @Size(max = 255, message = "El nombre del producto no puede exceder los 255 caracteres.")
    private String nombreProducto;

    @Schema(description = "Descripción detallada adicional del producto, características especiales, etc.",
            example = "Camisa de algodón 100% peinado, manga corta, color azul rey, ideal para uniforme o uso casual.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String descripcionProducto; // TEXT, opcional

    @Schema(description = "Talla del producto (ej. S, M, L, XL, Única, 38, 40).",
            example = "M",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Puede ser opcional o tener un valor por defecto
            nullable = true)
    @Size(max = 20, message = "La talla del producto no puede exceder los 20 caracteres.") // Ajustado max a 20
    private String tallaProducto;

    @Schema(description = "Color principal o descriptivo del producto.",
            example = "Azul Rey",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El color del producto no puede exceder los 50 caracteres.")
    private String colorProducto;

    @Schema(description = "Tipo o categoría general del producto (ej. Camisa, Pantalón, Chaqueta, Vestido).",
            example = "Camisa",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El tipo de producto no puede exceder los 50 caracteres.")
    private String tipoProducto;

    @Schema(description = "Género al que está dirigido el producto (ej. Hombre, Mujer, Unisex, Niño, Niña).",
            example = "Hombre",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "El género del producto no puede exceder los 20 caracteres.")
    private String generoProducto;

    @Schema(description = "Costo estimado o real de producción de una unidad del producto. Debe ser mayor que cero.",
            example = "35000.00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El costo de producción es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo de producción debe ser mayor que cero.")
    @Digits(integer = 10, fraction = 2, message = "El costo de producción debe tener máximo 10 dígitos enteros y 2 decimales.") // Ajustado integer a 10
    private BigDecimal costoProduccion;

    @Schema(description = "Precio de venta al público (PVP) de una unidad del producto. Debe ser mayor que cero.",
            example = "79900.00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio de venta es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor que cero.")
    @Digits(integer = 10, fraction = 2, message = "El precio de venta debe tener máximo 10 dígitos enteros y 2 decimales.") // Ajustado integer a 10
    private BigDecimal precioVenta;

    @Schema(description = "Unidad de medida en la que se vende y gestiona el producto. Por defecto 'Unidad'.",
            example = "Unidad",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 20, message = "La unidad de medida no puede exceder los 20 caracteres.")
    private String unidadMedidaProducto;

    // Constructores
    public ProductoCreateRequestDTO() {
    }

    public ProductoCreateRequestDTO(String referenciaProducto, String nombreProducto, String descripcionProducto, String tallaProducto, String colorProducto, String tipoProducto, String generoProducto, BigDecimal costoProduccion, BigDecimal precioVenta, String unidadMedidaProducto) {
        this.referenciaProducto = referenciaProducto;
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