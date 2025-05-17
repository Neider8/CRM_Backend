package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.DetalleOrdenVenta;
import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import com.crmtech360.crmtech360_backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional; // Asegúrate de que este import esté presente

@Repository
public interface DetalleOrdenVentaRepository extends JpaRepository<DetalleOrdenVenta, Integer> {
    List<DetalleOrdenVenta> findByOrdenVenta(OrdenVenta ordenVenta);
    List<DetalleOrdenVenta> findByOrdenVentaIdOrdenVenta(Integer idOrdenVenta);
    List<DetalleOrdenVenta> findByProducto(Producto producto);
    List<DetalleOrdenVenta> findByProductoIdProducto(Integer idProducto);
    Optional<DetalleOrdenVenta> findByOrdenVentaAndProducto(OrdenVenta ordenVenta, Producto producto);
    List<DetalleOrdenVenta> findByPrecioUnitarioVentaGreaterThan(BigDecimal precio);

    @Query("SELECT SUM(dov.subtotalDetalle) FROM DetalleOrdenVenta dov WHERE dov.ordenVenta = ?1")
    BigDecimal sumSubtotalByOrdenVenta(OrdenVenta ordenVenta);

    // MÉTODO AÑADIDO/CORREGIDO:
    Optional<DetalleOrdenVenta> findByIdDetalleOrdenAndOrdenVenta_IdOrdenVenta(Integer idDetalleOrden, Integer idOrdenVenta);
}