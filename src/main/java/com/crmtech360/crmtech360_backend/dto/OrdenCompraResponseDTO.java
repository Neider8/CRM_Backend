package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO para la respuesta de información detallada de una orden de compra, incluyendo sus detalles e información del proveedor.")
public class OrdenCompraResponseDTO {

    @Schema(description = "Identificador único de la orden de compra.", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idOrdenCompra;

    @Schema(description = "Información resumida del proveedor asociado a esta orden de compra.", accessMode = Schema.AccessMode.READ_ONLY)
    private ProveedorSummaryDTO proveedor; // Asegúrate que ProveedorSummaryDTO esté anotado

    @Schema(description = "Fecha y hora en que se registró la orden de compra en el sistema.", example = "2024-05-10T09:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaPedidoCompra;

    @Schema(description = "Fecha estimada en la que se espera recibir los insumos de esta orden.", example = "2024-06-15", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaEntregaEstimadaCompra;

    @Schema(description = "Fecha real en la que se recibieron completamente los insumos de esta orden. Será nula si la orden no ha sido recibida.", example = "2024-06-14", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaEntregaRealCompra;

    @Schema(description = "Estado actual de la orden de compra (ej. 'Pendiente', 'Parcialmente Recibida', 'Recibida Completa', 'Anulada').", example = "Pendiente", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoCompra;

    @Schema(description = "Valor total calculado de la orden de compra.", example = "7580.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalCompra;

    @Schema(description = "Observaciones o notas adicionales para la orden de compra.", example = "Pedido urgente. Contactar a Juan Pérez.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String observacionesCompra;

    @Schema(description = "Fecha y hora de la última actualización de este registro de orden de compra.", example = "2024-05-10T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    @Schema(description = "Lista de detalles (ítems) que componen esta orden de compra.", accessMode = Schema.AccessMode.READ_ONLY)
    @ArraySchema(schema = @Schema(implementation = DetalleOrdenCompraResponseDTO.class))
    private List<DetalleOrdenCompraResponseDTO> detalles; // Asegúrate que DetalleOrdenCompraResponseDTO esté anotado

    // Constructores
    public OrdenCompraResponseDTO() {
    }

    public OrdenCompraResponseDTO(Integer idOrdenCompra, ProveedorSummaryDTO proveedor, LocalDateTime fechaPedidoCompra, LocalDate fechaEntregaEstimadaCompra, LocalDate fechaEntregaRealCompra, String estadoCompra, BigDecimal totalCompra, String observacionesCompra, LocalDateTime fechaActualizacion, List<DetalleOrdenCompraResponseDTO> detalles) {
        this.idOrdenCompra = idOrdenCompra;
        this.proveedor = proveedor;
        this.fechaPedidoCompra = fechaPedidoCompra;
        this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra;
        this.fechaEntregaRealCompra = fechaEntregaRealCompra;
        this.estadoCompra = estadoCompra;
        this.totalCompra = totalCompra;
        this.observacionesCompra = observacionesCompra;
        this.fechaActualizacion = fechaActualizacion;
        this.detalles = detalles;
    }

    // Getters y Setters
    public Integer getIdOrdenCompra() { return idOrdenCompra; }
    public void setIdOrdenCompra(Integer idOrdenCompra) { this.idOrdenCompra = idOrdenCompra; }
    public ProveedorSummaryDTO getProveedor() { return proveedor; }
    public void setProveedor(ProveedorSummaryDTO proveedor) { this.proveedor = proveedor; }
    public LocalDateTime getFechaPedidoCompra() { return fechaPedidoCompra; }
    public void setFechaPedidoCompra(LocalDateTime fechaPedidoCompra) { this.fechaPedidoCompra = fechaPedidoCompra; }
    public LocalDate getFechaEntregaEstimadaCompra() { return fechaEntregaEstimadaCompra; }
    public void setFechaEntregaEstimadaCompra(LocalDate fechaEntregaEstimadaCompra) { this.fechaEntregaEstimadaCompra = fechaEntregaEstimadaCompra; }
    public LocalDate getFechaEntregaRealCompra() { return fechaEntregaRealCompra; }
    public void setFechaEntregaRealCompra(LocalDate fechaEntregaRealCompra) { this.fechaEntregaRealCompra = fechaEntregaRealCompra; }
    public String getEstadoCompra() { return estadoCompra; }
    public void setEstadoCompra(String estadoCompra) { this.estadoCompra = estadoCompra; }
    public BigDecimal getTotalCompra() { return totalCompra; }
    public void setTotalCompra(BigDecimal totalCompra) { this.totalCompra = totalCompra; }
    public String getObservacionesCompra() { return observacionesCompra; }
    public void setObservacionesCompra(String observacionesCompra) { this.observacionesCompra = observacionesCompra; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public List<DetalleOrdenCompraResponseDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrdenCompraResponseDTO> detalles) { this.detalles = detalles; }
}