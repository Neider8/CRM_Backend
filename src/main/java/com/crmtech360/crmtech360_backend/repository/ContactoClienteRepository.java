package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.ContactoCliente;
import com.crmtech360.crmtech360_backend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContactoClienteRepository extends JpaRepository<ContactoCliente, Integer> {
    List<ContactoCliente> findByCliente(Cliente cliente);
    List<ContactoCliente> findByClienteIdCliente(Integer idCliente);
    List<ContactoCliente> findByNombreContactoContainingIgnoreCase(String nombreContacto);
    Optional<ContactoCliente> findByCorreoContactoIgnoreCase(String correoContacto);
    List<ContactoCliente> findByTelefonoContactoContaining(String telefonoContacto);
    List<ContactoCliente> findByClienteAndCargoContactoIgnoreCase(Cliente cliente, String cargoContacto);
}