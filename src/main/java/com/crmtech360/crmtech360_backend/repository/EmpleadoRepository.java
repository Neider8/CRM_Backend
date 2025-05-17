package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    Optional<Empleado> findByNumeroDocumento(String numeroDocumento);
    List<Empleado> findByNombreEmpleadoContainingIgnoreCase(String nombreEmpleado);
    List<Empleado> findByCargoEmpleadoIgnoreCase(String cargoEmpleado);
    List<Empleado> findByAreaEmpleadoIgnoreCase(String areaEmpleado);
    List<Empleado> findByFechaContratacionEmpleadoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    List<Empleado> findBySalarioEmpleadoGreaterThanEqual(java.math.BigDecimal salarioMinimo);
    List<Empleado> findAllByOrderByNombreEmpleadoAsc();
    boolean existsByNumeroDocumento(String numeroDocumento);
}