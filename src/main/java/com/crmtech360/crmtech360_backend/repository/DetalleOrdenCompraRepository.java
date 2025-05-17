package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.DetalleOrdenCompra;
import com.crmtech360.crmtech360_backend.entity.OrdenCompra;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional; // Asegúrate de importar Optional

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Integer> {
    List<DetalleOrdenCompra> findByOrdenCompra(OrdenCompra ordenCompra);
    List<DetalleOrdenCompra> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra);
    List<DetalleOrdenCompra> findByInsumo(Insumo insumo);
    List<DetalleOrdenCompra> findByInsumoIdInsumo(Integer idInsumo);
    Optional<DetalleOrdenCompra> findByOrdenCompraAndInsumo(OrdenCompra ordenCompra, Insumo insumo);
    List<DetalleOrdenCompra> findByPrecioUnitarioCompraLessThan(BigDecimal precio);

    @Query("SELECT SUM(doc.subtotalCompra) FROM DetalleOrdenCompra doc WHERE doc.ordenCompra = ?1")
    BigDecimal sumSubtotalByOrdenCompra(OrdenCompra ordenCompra);

    // MÉTODO AÑADIDO/CORREGIDO:
    Optional<DetalleOrdenCompra> findByIdDetalleCompraAndOrdenCompra_IdOrdenCompra(Integer idDetalleCompra, Integer idOrdenCompra);
}