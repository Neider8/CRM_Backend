package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.MovimientoInventarioInsumo;
import com.crmtech360.crmtech360_backend.entity.InventarioInsumo; // Asegúrate que esté importado si lo usas en otros métodos
import org.springframework.data.domain.Page; // IMPORTANTE
import org.springframework.data.domain.Pageable; // IMPORTANTE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioInsumoRepository extends JpaRepository<MovimientoInventarioInsumo, Integer> {
    List<MovimientoInventarioInsumo> findByInventarioInsumo(InventarioInsumo inventarioInsumo);

    // MÉTODO CORREGIDO:
    Page<MovimientoInventarioInsumo> findByInventarioInsumoIdInventarioInsumo(Integer idInventarioInsumo, Pageable pageable);

    List<MovimientoInventarioInsumo> findByTipoMovimientoIgnoreCase(String tipoMovimiento);
    List<MovimientoInventarioInsumo> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoInventarioInsumo> findByInventarioInsumoAndFechaMovimientoBetween(InventarioInsumo inventarioInsumo, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<MovimientoInventarioInsumo> findByDescripcionMovimientoContainingIgnoreCase(String descripcion);
}