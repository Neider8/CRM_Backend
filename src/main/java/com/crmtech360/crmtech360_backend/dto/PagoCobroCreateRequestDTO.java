package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de creación de una nueva transacción de Pago o Cobro.")
public class PagoCobroCreateRequestDTO {

    @Schema(description = "Tipo de transacción. Debe ser 'Pago' (para órdenes de compra) o 'Cobro' (para órdenes de venta).",
            example = "Cobro",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El tipo de transacción es obligatorio.")
    @Pattern(regexp = "Pago|Cobro", message = "Tipo inválido. Debe ser Pago o Cobro.")
    private String tipoTransaccion;

    @Schema(description = "Identificador único de la Orden de Venta a la que se asocia este cobro. " +
            "Requerido si tipoTransaccion es 'Cobro'. Debe ser nulo si tipoTransaccion es 'Pago'.",
            example = "1001",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Condicionalmente requerido
            nullable = true)
    private Integer idOrdenVenta;

    @Schema(description = "Identificador único de la Orden de Compra a la que se asocia este pago. " +
            "Requerido si tipoTransaccion es 'Pago'. Debe ser nulo si tipoTransaccion es 'Cobro'.",
            example = "501",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, // Condicionalmente requerido
            nullable = true)
    private Integer idOrdenCompra;

    @Schema(description = "Fecha en que se realizó el pago o cobro. No puede ser una fecha futura.",
            example = "2024-05-20", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha del pago/cobro es obligatoria.")
    @PastOrPresent(message = "La fecha de pago/cobro no puede ser futura.")
    private LocalDate fechaPagoCobro;

    @Schema(description = "Método utilizado para el pago o cobro (ej. 'Efectivo', 'Transferencia Bancaria', 'Cheque', 'PSE', 'Tarjeta Crédito').",
            example = "Transferencia Bancaria",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El método de pago no puede estar vacío.")
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres.")
    private String metodoPago;

    @Schema(description = "Monto de la transacción. Debe ser un valor positivo y puede tener hasta 2 decimales.",
            example = "150000.75",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El monto de la transacción es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero.")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales.") // Ajustado integer a 10
    private BigDecimal montoTransaccion;

    @Schema(description = "Número de referencia o comprobante asociado a la transacción (ej. Nro. Comprobante de pago, Nro. Cheque).",
            example = "TRN-0012345",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 100, message = "La referencia no puede exceder los 100 caracteres.")
    private String referenciaTransaccion;

    @Schema(description = "Estado de la transacción al momento de la creación (ej. 'Pendiente', 'Pagado', 'Cobrado', 'Anulado'). " +
            "El estado 'Vencido' usualmente se determina por lógica de negocio basada en fechas.",
            example = "Cobrado",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El estado de la transacción es obligatorio.")
    @Pattern(regexp = "Pendiente|Pagado|Cobrado|Anulado", message = "Estado de transacción inválido para creación. Valores permitidos: Pendiente, Pagado, Cobrado, Anulado.")
    private String estadoTransaccion; // 'Vencido' generalmente no se establece al crear.

    @Schema(description = "Observaciones o notas adicionales para la transacción.",
            example = "Abono a factura FV-00123.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres.") // Ajusta el max si es necesario
    private String observacionesTransaccion;

    // Constructores
    public PagoCobroCreateRequestDTO() {
    }

    public PagoCobroCreateRequestDTO(String tipoTransaccion, Integer idOrdenVenta, Integer idOrdenCompra, LocalDate fechaPagoCobro, String metodoPago, BigDecimal montoTransaccion, String referenciaTransaccion, String estadoTransaccion, String observacionesTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
        this.idOrdenVenta = idOrdenVenta;
        this.idOrdenCompra = idOrdenCompra;
        this.fechaPagoCobro = fechaPagoCobro;
        this.metodoPago = metodoPago;
        this.montoTransaccion = montoTransaccion;
        this.referenciaTransaccion = referenciaTransaccion;
        this.estadoTransaccion = estadoTransaccion;
        this.observacionesTransaccion = observacionesTransaccion;
    }

    // Getters y Setters
    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }
    public Integer getIdOrdenVenta() { return idOrdenVenta; }
    public void setIdOrdenVenta(Integer idOrdenVenta) { this.idOrdenVenta = idOrdenVenta; }
    public Integer getIdOrdenCompra() { return idOrdenCompra; }
    public void setIdOrdenCompra(Integer idOrdenCompra) { this.idOrdenCompra = idOrdenCompra; }
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
}