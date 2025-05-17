package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    Optional<Permiso> findByNombrePermiso(String nombrePermiso);
    List<Permiso> findByNombrePermisoContainingIgnoreCase(String keyword);
}