package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.PagoCobro;
import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import com.crmtech360.crmtech360_backend.entity.OrdenCompra;
import org.springframework.data.domain.Page; // IMPORTANTE
import org.springframework.data.domain.Pageable; // IMPORTANTE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoCobroRepository extends JpaRepository<PagoCobro, Integer> {
    List<PagoCobro> findByOrdenVenta(OrdenVenta ordenVenta);
    // List<PagoCobro> findByOrdenVentaIdOrdenVenta(Integer idOrdenVenta); // Se reemplazará por el paginado de abajo

    List<PagoCobro> findByOrdenCompra(OrdenCompra ordenCompra);
    // List<PagoCobro> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra); // Se reemplazará por el paginado de abajo

    // MÉTODO CORREGIDO para el error "Expected 1 argument but found 2"
    Page<PagoCobro> findByTipoTransaccionIgnoreCase(String tipoTransaccion, Pageable pageable);

    List<PagoCobro> findByEstadoTransaccionIgnoreCase(String estadoTransaccion);
    List<PagoCobro> findByFechaPagoCobroBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<PagoCobro> findByFechaRegistroTransaccionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<PagoCobro> findByMetodoPagoIgnoreCase(String metodoPago);
    List<PagoCobro> findByMontoTransaccionBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);
    List<PagoCobro> findByReferenciaTransaccionIgnoreCase(String referenciaTransaccion);
    List<PagoCobro> findByTipoTransaccionAndEstadoTransaccionAllIgnoreCase(String tipo, String estado);

    // MÉTODOS NUEVOS/CORREGIDOS para los errores "Cannot resolve method..."
    Page<PagoCobro> findByOrdenVenta_IdOrdenVentaAndTipoTransaccionIgnoreCase(Integer idOrdenVenta, String tipoTransaccion, Pageable pageable);
    Page<PagoCobro> findByOrdenCompra_IdOrdenCompraAndTipoTransaccionIgnoreCase(Integer idOrdenCompra, String tipoTransaccion, Pageable pageable);
}