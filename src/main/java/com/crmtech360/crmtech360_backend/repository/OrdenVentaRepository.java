package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import com.crmtech360.crmtech360_backend.entity.Cliente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenVentaRepository extends JpaRepository<OrdenVenta, Integer> {
    List<OrdenVenta> findByCliente(Cliente cliente);
    List<OrdenVenta> findByClienteIdCliente(Integer idCliente);
    List<OrdenVenta> findByEstadoOrdenIgnoreCase(String estadoOrden);
    List<OrdenVenta> findByFechaPedidoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<OrdenVenta> findByFechaEntregaEstimadaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<OrdenVenta> findByTotalOrdenGreaterThanEqual(BigDecimal montoMinimo);
    List<OrdenVenta> findByTotalOrdenBetween(BigDecimal montoMinimo, BigDecimal montoMaximo);
    List<OrdenVenta> findByClienteAndEstadoOrdenAllIgnoreCase(Cliente cliente, String estadoOrden);
    long countByEstadoOrdenIgnoreCase(String estadoOrden);
    List<OrdenVenta> findTop10ByOrderByFechaPedidoDesc(); // Para un dashboard, por ejemplo
    // Ejemplo de query con @Query para algo más complejo
    @Query("SELECT ov FROM OrdenVenta ov WHERE ov.cliente.nombreCliente LIKE %?1% AND ov.estadoOrden = ?2")
    List<OrdenVenta> findByNombreClienteContainingAndEstadoOrden(String nombreClienteFragmento, String estadoOrden, Pageable pageable);
}