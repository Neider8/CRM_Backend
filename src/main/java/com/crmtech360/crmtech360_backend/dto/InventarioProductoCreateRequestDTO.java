package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la solicitud de creación de un nuevo registro de inventario para un producto terminado en una ubicación específica. " +
        "Esto usualmente establece el stock inicial o la primera entrada de un producto en esa ubicación.")
public class InventarioProductoCreateRequestDTO {

    @Schema(description = "Nombre o código de la ubicación física donde se almacenará el producto (ej. 'Bodega Principal', 'Almacén A', 'Tienda Centro').",
            example = "Almacén Principal",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La ubicación del inventario no puede estar vacía.")
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres.")
    private String ubicacionInventario;

    @Schema(description = "Identificador único del producto para el cual se está creando el registro de inventario.",
            example = "101",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del producto es obligatorio.")
    private Integer idProducto;

    @Schema(description = "Cantidad inicial de stock para este producto en la ubicación especificada. Debe ser un número entero no negativo.",
            example = "200",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad inicial de stock es obligatoria.")
    @Min(value = 0, message = "La cantidad de stock no puede ser negativa.")
    private Integer cantidadStock; // Cantidad inicial al crear la ubicación para este producto

    // Constructores
    public InventarioProductoCreateRequestDTO() {
    }

    public InventarioProductoCreateRequestDTO(String ubicacionInventario, Integer idProducto, Integer cantidadStock) {
        this.ubicacionInventario = ubicacionInventario;
        this.idProducto = idProducto;
        this.cantidadStock = cantidadStock;
    }

    // Getters y Setters
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidadStock() { return cantidadStock; }
    public void setCantidadStock(Integer cantidadStock) { this.cantidadStock = cantidadStock; }
}