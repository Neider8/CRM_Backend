package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    Optional<Proveedor> findByNitProveedor(String nitProveedor);
    List<Proveedor> findByNombreComercialProveedorContainingIgnoreCase(String nombreComercial);
    List<Proveedor> findByRazonSocialProveedorContainingIgnoreCase(String razonSocial);
    Optional<Proveedor> findByCorreoProveedorIgnoreCase(String correoProveedor);
    List<Proveedor> findByContactoPrincipalProveedorContainingIgnoreCase(String contacto);
    List<Proveedor> findAllByOrderByNombreComercialProveedorAsc();
    boolean existsByNitProveedor(String nitProveedor);
}