package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.MovimientoInventarioProducto;
import com.crmtech360.crmtech360_backend.entity.InventarioProducto; // Asegúrate que esté importado
import org.springframework.data.domain.Page; // IMPORTANTE
import org.springframework.data.domain.Pageable; // IMPORTANTE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioProductoRepository extends JpaRepository<MovimientoInventarioProducto, Integer> {
    List<MovimientoInventarioProducto> findByInventarioProducto(InventarioProducto inventarioProducto);

    // MÉTODO CORREGIDO:
    Page<MovimientoInventarioProducto> findByInventarioProductoIdInventarioProducto(Integer idInventarioProducto, Pageable pageable);

    List<MovimientoInventarioProducto> findByTipoMovimientoIgnoreCase(String tipoMovimiento);
    List<MovimientoInventarioProducto> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoInventarioProducto> findByInventarioProductoAndFechaMovimientoBetween(InventarioProducto inventarioProducto, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoInventarioProducto> findByDescripcionMovimientoContainingIgnoreCase(String descripcion);
}