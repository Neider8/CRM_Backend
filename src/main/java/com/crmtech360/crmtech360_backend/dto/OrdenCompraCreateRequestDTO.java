package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // Importar Size si es necesario para observacionesCompra
import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO para la solicitud de creación de una nueva orden de compra.")
public class OrdenCompraCreateRequestDTO {

    @Schema(description = "Identificador único del proveedor al que se le realiza la orden de compra.",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del proveedor es obligatorio.")
    private Integer idProveedor;

    @Schema(description = "Fecha estimada en la que se espera recibir los insumos de esta orden. No puede ser una fecha pasada.",
            example = "2024-06-15", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Puede ser opcional
            nullable = true)
    @FutureOrPresent(message = "La fecha de entrega estimada no puede ser pasada.")
    private LocalDate fechaEntregaEstimadaCompra;

    @Schema(description = "Observaciones o notas adicionales para la orden de compra.",
            example = "Pedido urgente. Contactar a Juan Pérez para coordinación de entrega.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesCompra;

    @Schema(description = "Lista de detalles de la orden de compra, especificando cada insumo y cantidad a comprar. " +
            "Debe contener al menos un ítem.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "La orden de compra debe tener al menos un detalle.")
    @Valid // Para asegurar que los DTOs anidados (DetalleOrdenCompraRequestDTO) también sean validados.
    private List<DetalleOrdenCompraRequestDTO> detalles; // Asegúrate que DetalleOrdenCompraRequestDTO esté anotado

    // Constructores
    public OrdenCompraCreateRequestDTO() {
    }

    public OrdenCompraCreateRequestDTO(Integer idProveedor, LocalDate fechaEntregaEstimadaCompra, String observacionesCompra, List<DetalleOrdenCompraRequestDTO> detalles) {
        this.idProveedor = idProveedor;
        this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra;
        this.observacionesCompra = observacionesCompra;
        this.detalles = detalles;
    }

    // Getters y Setters
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public LocalDate getFechaEntregaEstimadaCompra() { return fechaEntregaEstimadaCompra; }
    public void setFechaEntregaEstimadaCompra(LocalDate fechaEntregaEstimadaCompra) { this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra; }
    public String getObservacionesCompra() { return observacionesCompra; }
    public void setObservacionesCompra(String observacionesCompra) { this.observacionesCompra = observacionesCompra; }
    public List<DetalleOrdenCompraRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenCompraRequestDTO> detalles) { this.detalles = detalles; }
}