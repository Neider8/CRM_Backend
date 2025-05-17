package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank; // Es buena práctica usar NotBlank si no debe ser solo espacios

@Schema(description = "DTO para la solicitud de actualización de la ubicación de un registro de inventario de producto terminado existente. " +
        "Solo se actualizará la ubicación si se proporciona un nuevo valor.")
public class InventarioProductoUpdateDTO {

    // Comentario original:
    // Permitir cambiar la ubicación podría ser una operación compleja si hay stock.
    // Considerar si esta funcionalidad es realmente necesaria o si se maneja de otra forma.
    @Schema(description = "Nueva ubicación física donde se almacenará el producto (ej. 'Bodega B', 'Tienda Norte'). " +
            "Si se proporciona, la ubicación actual será reemplazada. Considerar implicaciones si hay stock.",
            example = "Bodega B",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, solo se actualiza si se envía
            nullable = true)
    @NotBlank(message = "La ubicación del inventario no puede estar vacía si se proporciona.") // Si se envía, no debe ser solo espacios
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres.")
    private String ubicacionInventario;

    // No se incluye cantidadStock aquí, ya que el stock se actualiza a través de movimientos de inventario.

    // Constructores
    public InventarioProductoUpdateDTO() {
    }

    public InventarioProductoUpdateDTO(String ubicacionInventario) {
        this.ubicacionInventario = ubicacionInventario;
    }

    // Getters y Setters
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
}