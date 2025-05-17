package com.crmtech360.crmtech360_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importar @Schema
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Schema(description = "DTO para la solicitud de actualización de una transacción de Pago o Cobro existente. " +
        "Campos como tipo de transacción, monto y orden asociada generalmente no se modifican post-creación.")
public class PagoCobroUpdateRequestDTO {

    // Nota: tipoTransaccion, montoTransaccion, idOrdenVenta/idOrdenCompra
    // generalmente no se deberían modificar después de la creación de la transacción.
    // Si se necesita cambiar estos, usualmente implicaría anular la transacción actual y crear una nueva.

    @Schema(description = "Nueva fecha efectiva en que se realizó el pago o cobro. No puede ser una fecha futura.",
            example = "2024-05-21", // Formato YYYY-MM-DD
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @PastOrPresent(message = "La fecha de pago/cobro no puede ser futura si se especifica.")
    private LocalDate fechaPagoCobro;

    @Schema(description = "Nuevo método utilizado para el pago o cobro (ej. 'Efectivo', 'Transferencia Bancaria', 'Cheque').",
            example = "Cheque",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 50, message = "El método de pago no puede exceder los 50 caracteres.")
    private String metodoPago;

    @Schema(description = "Nuevo número de referencia o comprobante asociado a la transacción.",
            example = "CHQ-005678",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 100, message = "La referencia no puede exceder los 100 caracteres.")
    private String referenciaTransaccion;

    @Schema(description = "Nuevo estado de la transacción (ej. 'Pendiente', 'Pagado', 'Cobrado', 'Vencido', 'Anulado'). " +
            "El estado 'Vencido' usualmente se maneja por lógica de negocio.",
            example = "Pagado",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Pattern(regexp = "Pendiente|Pagado|Cobrado|Vencido|Anulado", message = "Estado de transacción inválido.")
    private String estadoTransaccion;

    @Schema(description = "Nuevas observaciones o notas adicionales para la transacción.",
            example = "Pago confirmado por tesorería.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Size(max = 500, message = "Las observaciones no pueden exceder los 500 caracteres.") // Ajusta el max si es necesario
    private String observacionesTransaccion;

    // Constructores
    public PagoCobroUpdateRequestDTO() {
    }

    public PagoCobroUpdateRequestDTO(LocalDate fechaPagoCobro, String metodoPago, String referenciaTransaccion, String estadoTransaccion, String observacionesTransaccion) {
        this.fechaPagoCobro = fechaPagoCobro;
        this.metodoPago = metodoPago;
        this.referenciaTransaccion = referenciaTransaccion;
        this.estadoTransaccion = estadoTransaccion;
        this.observacionesTransaccion = observacionesTransaccion;
    }

    // Getters y Setters
    public LocalDate getFechaPagoCobro() { return fechaPagoCobro; }
    public void setFechaPagoCobro(LocalDate fechaPagoCobro) { this.fechaPagoCobro = fechaPagoCobro; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public void setReferenciaTransaccion(String referenciaTransaccion) { this.referenciaTransaccion = referenciaTransaccion; }
    public String getEstadoTransaccion() { return estadoTransaccion; }
    public void setEstadoTransaccion(String estadoTransaccion) { this.estadoTransaccion = estadoTransaccion; }
    public String getObservacionesTransaccion() { return observacionesTransaccion; }
    public void setObservacionesTransaccion(String observacionesTransaccion) { this.observacionesTransaccion = observacionesTransaccion; }
}