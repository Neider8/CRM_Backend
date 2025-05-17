package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.OrdenCompra;
import com.crmtech360.crmtech360_backend.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    List<OrdenCompra> findByProveedor(Proveedor proveedor);
    List<OrdenCompra> findByProveedorIdProveedor(Integer idProveedor);
    List<OrdenCompra> findByEstadoCompraIgnoreCase(String estadoCompra);
    List<OrdenCompra> findByFechaPedidoCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<OrdenCompra> findByFechaEntregaEstimadaCompraBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<OrdenCompra> findByFechaEntregaRealCompraIsNotNull();
    List<OrdenCompra> findByTotalCompraGreaterThanEqual(BigDecimal montoMinimo);
    List<OrdenCompra> findByProveedorAndEstadoCompraAllIgnoreCase(Proveedor proveedor, String estadoCompra);
    long countByEstadoCompraIgnoreCase(String estadoCompra);
}