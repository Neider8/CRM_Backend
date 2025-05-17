package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.OrdenProduccion;
import com.crmtech360.crmtech360_backend.entity.OrdenVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, Integer> {
    List<OrdenProduccion> findByOrdenVenta(OrdenVenta ordenVenta);
    List<OrdenProduccion> findByOrdenVentaIdOrdenVenta(Integer idOrdenVenta);
    List<OrdenProduccion> findByEstadoProduccionIgnoreCase(String estadoProduccion);
    List<OrdenProduccion> findByFechaInicioProduccionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<OrdenProduccion> findByFechaFinEstimadaProduccionBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<OrdenProduccion> findByFechaFinRealProduccionIsNullAndEstadoProduccionNotIn(List<String> estadosExcluidos); // Ej: estados como 'Anulada'
    List<OrdenProduccion> findByEstadoProduccionIn(List<String> estados);
    long countByEstadoProduccionIgnoreCase(String estadoProduccion);
}