package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // Importar Size si es necesario
import java.time.LocalDate;
import java.util.List;

@Schema(description = "DTO para la solicitud de creación de una nueva Orden de Venta.")
public class OrdenVentaCreateRequestDTO {

    @Schema(description = "Identificador único del cliente que realiza la orden.",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del cliente es obligatorio.")
    private Integer idCliente;

    @Schema(description = "Fecha estimada en la que se espera entregar los productos al cliente. No puede ser una fecha pasada. Puede ser opcional al crear la orden.",
            example = "2024-07-15", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @FutureOrPresent(message = "La fecha de entrega estimada no puede ser pasada si se especifica.")
    private LocalDate fechaEntregaEstimada;

    @Schema(description = "Observaciones o notas adicionales para la orden de venta.",
            example = "Cliente requiere empaque especial. Confirmar dirección de envío.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 1000, message = "Las observaciones no pueden exceder los 1000 caracteres.") // Ajusta el max si es necesario
    private String observacionesOrden;

    // El estado inicial (ej. 'Pendiente', 'En Cotización') generalmente se asigna
    // automáticamente por la lógica de negocio en el servicio al crear la orden.
    // El total de la orden también se calcula en el servicio.

    @Schema(description = "Lista de detalles de la orden de venta, especificando cada producto y cantidad a vender. " +
            "Debe contener al menos un ítem.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "La orden debe tener al menos un detalle.")
    @Valid // Para asegurar que los DTOs anidados (DetalleOrdenVentaRequestDTO) también sean validados.
    private List<DetalleOrdenVentaRequestDTO> detalles; // Asegúrate que DetalleOrdenVentaRequestDTO esté anotado

    // Constructores
    public OrdenVentaCreateRequestDTO() {
    }

    public OrdenVentaCreateRequestDTO(Integer idCliente, LocalDate fechaEntregaEstimada, String observacionesOrden, List<DetalleOrdenVentaRequestDTO> detalles) {
        this.idCliente = idCliente;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.observacionesOrden = observacionesOrden;
        this.detalles = detalles;
    }

    // Getters y Setters
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getObservacionesOrden() { return observacionesOrden; }
    public void setObservacionesOrden(String observacionesOrden) { this.observacionesOrden = observacionesOrden; }
    public List<DetalleOrdenVentaRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenVentaRequestDTO> detalles) { this.detalles = detalles; }
}