package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDateTime;
// No se necesita importar LocalDate aquí si no se usa directamente

@Schema(description = "DTO para la respuesta de información detallada de un producto terminado.")
public class ProductoResponseDTO {

    @Schema(description = "Identificador único del producto.", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idProducto;

    @Schema(description = "Referencia única del producto (SKU).", example = "CAM-AZ-M-001", accessMode = Schema.AccessMode.READ_ONLY)
    private String referenciaProducto;

    @Schema(description = "Nombre descriptivo del producto.", example = "Camisa Algodón Clásica Azul Talla M", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreProducto;

    @Schema(description = "Descripción detallada adicional del producto.", example = "Camisa de algodón 100% peinado, manga corta, color azul rey.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String descripcionProducto;

    @Schema(description = "Talla del producto.", example = "M", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String tallaProducto;

    @Schema(description = "Color principal del producto.", example = "Azul Rey", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String colorProducto;

    @Schema(description = "Tipo o categoría general del producto.", example = "Camisa", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoProducto;

    @Schema(description = "Género al que está dirigido el producto.", example = "Hombre", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String generoProducto;

    @Schema(description = "Costo de producción de una unidad del producto.", example = "35000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal costoProduccion;

    @Schema(description = "Precio de venta al público (PVP) de una unidad del producto.", example = "79900.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precioVenta;

    @Schema(description = "Unidad de medida en la que se vende y gestiona el producto.", example = "Unidad", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String unidadMedidaProducto;

    @Schema(description = "Fecha y hora de creación del registro del producto en el sistema.", example = "2023-03-10T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de la última actualización del registro del producto.", example = "2024-01-15T14:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public ProductoResponseDTO() {
    }

    public ProductoResponseDTO(Integer idProducto, String referenciaProducto, String nombreProducto, String descripcionProducto, String tallaProducto, String colorProducto, String tipoProducto, String generoProducto, BigDecimal costoProduccion, BigDecimal precioVenta, String unidadMedidaProducto, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.idProducto = idProducto;
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
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
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