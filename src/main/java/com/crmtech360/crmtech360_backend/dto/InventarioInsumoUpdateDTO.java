package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank; // Es buena práctica usar NotBlank si no debe ser solo espacios

@Schema(description = "DTO para la solicitud de actualización de la ubicación de un registro de inventario de insumo existente. " +
        "Solo se actualizará la ubicación si se proporciona un nuevo valor.")
public class InventarioInsumoUpdateDTO {

    @Schema(description = "Nueva ubicación física donde se almacenará el insumo (ej. 'Bodega Secundaria', 'Estante B-05'). " +
            "Debe ser único en combinación con el insumo si la lógica de negocio lo requiere.",
            example = "Bodega Secundaria",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Opcional, solo se actualiza si se envía
            nullable = true)
    @NotBlank(message = "La ubicación del inventario no puede estar vacía si se proporciona.") // Si se envía, no debe ser solo espacios
    @Size(max = 100, message = "La ubicación no puede exceder los 100 caracteres.")
    private String ubicacionInventario;

    // Constructores
    public InventarioInsumoUpdateDTO() {}

    public InventarioInsumoUpdateDTO(String ubicacionInventario) {
        this.ubicacionInventario = ubicacionInventario;
    }

    // Getters y Setters
    public String getUbicacionInventario() { return ubicacionInventario; }
    public void setUbicacionInventario(String ubicacionInventario) { this.ubicacionInventario = ubicacionInventario; }
}