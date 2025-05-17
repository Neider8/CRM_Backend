package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    List<Cliente> findByTipoDocumento(String tipoDocumento);
    List<Cliente> findByNombreClienteContainingIgnoreCase(String nombreCliente);
    Optional<Cliente> findByCorreoClienteIgnoreCase(String correoCliente);
    List<Cliente> findByTelefonoClienteContaining(String telefonoCliente);
    List<Cliente> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Cliente> findAllByOrderByNombreClienteAsc();
    boolean existsByNumeroDocumento(String numeroDocumento);
    boolean existsByCorreoClienteIgnoreCase(String correoCliente);
}