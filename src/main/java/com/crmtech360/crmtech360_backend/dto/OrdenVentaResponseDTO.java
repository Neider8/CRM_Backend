package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO para la respuesta de información detallada de una Orden de Venta, incluyendo sus detalles e información del cliente.")
public class OrdenVentaResponseDTO {

    @Schema(description = "Identificador único de la orden de venta.", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenVenta;

    @Schema(description = "Información resumida del cliente asociado a esta orden de venta.", accessMode = Schema.AccessMode.READ_ONLY)
    private ClienteSummaryDTO cliente; // Asegúrate que ClienteSummaryDTO esté anotado

    @Schema(description = "Fecha y hora en que se registró la orden de venta en el sistema.", example = "2024-05-01T11:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaPedido;

    @Schema(description = "Fecha estimada en la que se espera entregar los productos al cliente.", example = "2024-07-15", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaEntregaEstimada;

    @Schema(description = "Estado actual de la orden de venta (ej. 'Pendiente', 'Confirmada', 'En Producción', 'Entregada', 'Anulada').", example = "Confirmada", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoOrden;

    @Schema(description = "Valor total calculado de la orden de venta.", example = "12500.50", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalOrden;

    @Schema(description = "Observaciones o notas adicionales para la orden de venta.", example = "Cliente requiere empaque especial.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String observacionesOrden;

    @Schema(description = "Fecha y hora de la última actualización de este registro de orden de venta.", example = "2024-05-02T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Lista de detalles (ítems) que componen esta orden de venta.", accessMode = Schema.AccessMode.READ_ONLY)
    @ArraySchema(schema = @Schema(implementation = DetalleOrdenVentaResponseDTO.class))
    private List<DetalleOrdenVentaResponseDTO> detalles; // Asegúrate que DetalleOrdenVentaResponseDTO esté anotado

    // Constructores
    public OrdenVentaResponseDTO() {
    }

    public OrdenVentaResponseDTO(Integer idOrdenVenta, ClienteSummaryDTO cliente, LocalDateTime fechaPedido, LocalDate fechaEntregaEstimada, String estadoOrden, BigDecimal totalOrden, String observacionesOrden, LocalDateTime fechaActualizacion, List<DetalleOrdenVentaResponseDTO> detalles) {
        this.idOrdenVenta = idOrdenVenta;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estadoOrden = estadoOrden;
        this.totalOrden = totalOrden;
        this.observacionesOrden = observacionesOrden;
        this.fechaActualizacion = fechaActualizacion;
        this.detalles = detalles;
    }

    // Getters y Setters
    public Integer getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Integer idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public ClienteSummaryDTO getCliente() { return cliente; }
    public void setCliente(ClienteSummaryDTO cliente) { this.cliente = cliente; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public LocalDate getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }
    public String getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(String estadoOrden) { this.estadoOrden = estadoOrden; }
    public BigDecimal getTotalOrden() { return totalOrden; }
    public void setTotalOrden(BigDecimal totalOrden) { this.totalOrden = totalOrden; }
    public String getObservacionesOrden() { return observacionesOrden; }
    public void setObservacionesOrden(String observacionesOrden) { this.observacionesOrden = observacionesOrden; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public List<DetalleOrdenVentaResponseDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenVentaResponseDTO> detalles) { this.detalles = detalles; }
}