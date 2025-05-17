package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO para la respuesta de información detallada de una transacción de Pago o Cobro.")
public class PagoCobroResponseDTO {

    @Schema(description = "Identificador único de la transacción de pago/cobro.", example = "601", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPagoCobro;

    @Schema(description = "Tipo de transacción realizada ('Pago' o 'Cobro').", example = "Cobro", accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoTransaccion;

    @Schema(description = "Información resumida de la Orden de Venta asociada a este cobro. Será nulo si es un Pago.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private OrdenVentaSummaryDTO ordenVenta; // Asegúrate que OrdenVentaSummaryDTO esté anotado

    @Schema(description = "Información resumida de la Orden de Compra asociada a este pago. Será nulo si es un Cobro.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private OrdenCompraSummaryDTO ordenCompra; // Asegúrate que OrdenCompraSummaryDTO esté anotado

    @Schema(description = "Fecha y hora en que se registró la transacción en el sistema.", example = "2024-05-20T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaRegistroTransaccion;

    @Schema(description = "Fecha efectiva en que se realizó el pago o cobro.", example = "2024-05-20", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaPagoCobro;

    @Schema(description = "Método utilizado para el pago o cobro.", example = "Transferencia Bancaria", accessMode = Schema.AccessMode.READ_ONLY)
    private String metodoPago;

    @Schema(description = "Monto de la transacción.", example = "150000.75", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal montoTransaccion;

    @Schema(description = "Número de referencia o comprobante asociado a la transacción.", example = "TRN-0012345", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String referenciaTransaccion;

    @Schema(description = "Estado actual de la transacción (ej. 'Pendiente', 'Pagado', 'Cobrado', 'Anulado').", example = "Cobrado", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoTransaccion;

    @Schema(description = "Observaciones o notas adicionales para la transacción.", example = "Abono a factura FV-00123.", nullable = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String observacionesTransaccion;

    @Schema(description = "Fecha y hora de la última actualización de esta transacción.", example = "2024-05-20T10:05:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public PagoCobroResponseDTO() {
    }

    public PagoCobroResponseDTO(Integer idPagoCobro, String tipoTransaccion, OrdenVentaSummaryDTO ordenVenta, OrdenCompraSummaryDTO ordenCompra, LocalDateTime fechaRegistroTransaccion, LocalDate fechaPagoCobro, String metodoPago, BigDecimal montoTransaccion, String referenciaTransaccion, String estadoTransaccion, String observacionesTransaccion, LocalDateTime fechaActualizacion) {
        this.idPagoCobro = idPagoCobro;
        this.tipoTransaccion = tipoTransaccion;
        this.ordenVenta = ordenVenta;
        this.ordenCompra = ordenCompra;
        this.fechaRegistroTransaccion = fechaRegistroTransaccion;
        this.fechaPagoCobro = fechaPagoCobro;
        this.metodoPago = metodoPago;
        this.montoTransaccion = montoTransaccion;
        this.referenciaTransaccion = referenciaTransaccion;
        this.estadoTransaccion = estadoTransaccion;
        this.observacionesTransaccion = observacionesTransaccion;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters
    public Integer getIdPagoCobro() { return idPagoCobro; }
    public void setIdPagoCobro(Integer idPagoCobro) { this.idPagoCobro = idPagoCobro; }
    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }
    public OrdenVentaSummaryDTO getOrdenVenta() { return ordenVenta; }
    public void setOrdenVenta(OrdenVentaSummaryDTO ordenVenta) { this.ordenVenta = ordenVenta; }
    public OrdenCompraSummaryDTO getOrdenCompra() { return ordenCompra; }
    public void setOrdenCompra(OrdenCompraSummaryDTO ordenCompra) { this.ordenCompra = ordenCompra; }
    public LocalDateTime getFechaRegistroTransaccion() { return fechaRegistroTransaccion; }
    public void setFechaRegistroTransaccion(LocalDateTime fechaRegistroTransaccion) { this.fechaRegistroTransaccion = fechaRegistroTransaccion; }
    public LocalDate getFechaPagoCobro() { return fechaPagoCobro; }
    public void setFechaPagoCobro(LocalDate fechaPagoCobro) { this.fechaPagoCobro = fechaPagoCobro; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public BigDecimal getMontoTransaccion() { return montoTransaccion; }
    public void setMontoTransaccion(BigDecimal montoTransaccion) { this.montoTransaccion = montoTransaccion; }
    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public void setReferenciaTransaccion(String referenciaTransaccion) { this.referenciaTransaccion = referenciaTransaccion; }
    public String getEstadoTransaccion() { return estadoTransaccion; }
    public void setEstadoTransaccion(String estadoTransaccion) { this.estadoTransaccion = estadoTransaccion; }
    public String getObservacionesTransaccion() { return observacionesTransaccion; }
    public void setObservacionesTransaccion(String observacionesTransaccion) { this.observacionesTransaccion = observacionesTransaccion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}