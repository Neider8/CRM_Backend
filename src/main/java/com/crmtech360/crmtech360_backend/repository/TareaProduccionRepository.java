package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.TareaProduccion;
import com.crmtech360.crmtech360_backend.entity.OrdenProduccion;
import com.crmtech360.crmtech360_backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Asegúrate de importar Optional

@Repository
public interface TareaProduccionRepository extends JpaRepository<TareaProduccion, Integer> {
    List<TareaProduccion> findByOrdenProduccion(OrdenProduccion ordenProduccion);
    List<TareaProduccion> findByOrdenProduccionIdOrdenProduccion(Integer idOrdenProduccion);
    List<TareaProduccion> findByEmpleado(Empleado empleado);
    List<TareaProduccion> findByEmpleadoIdEmpleado(Integer idEmpleado);
    List<TareaProduccion> findByEstadoTareaIgnoreCase(String estadoTarea);
    List<TareaProduccion> findByOrdenProduccionAndEmpleado(OrdenProduccion ordenProduccion, Empleado empleado);
    List<TareaProduccion> findByFechaInicioTareaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<TareaProduccion> findByFechaFinTareaIsNullAndEstadoTareaIgnoreCase(String estadoTarea);
    List<TareaProduccion> findByEmpleadoAndEstadoTareaIn(Empleado empleado, List<String> estados);

    // MÉTODO AÑADIDO/CORREGIDO:
    Optional<TareaProduccion> findByIdTareaProduccionAndOrdenProduccion_IdOrdenProduccion(Integer idTareaProduccion, Integer idOrdenProduccion);
}