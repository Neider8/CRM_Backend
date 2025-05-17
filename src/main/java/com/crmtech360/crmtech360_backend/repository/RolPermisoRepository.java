package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.RolPermiso;
import com.crmtech360.crmtech360_backend.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Integer> {
    List<RolPermiso> findByRolNombre(String rolNombre);

    // Asegúrate de que este método usa FetchType.EAGER o un JOIN FETCH si necesitas los permisos cargados
    // Aquí un ejemplo con JOIN FETCH para cargar el permiso asociado y evitar N+1 en UserDetailsServiceImpl
    @Query("SELECT rp FROM RolPermiso rp JOIN FETCH rp.permiso WHERE rp.rolNombre = :rolNombre")
    List<RolPermiso> findByRolNombreWithPermisos(@Param("rolNombre") String rolNombre);

    List<RolPermiso> findByPermiso(Permiso permiso);
    Optional<RolPermiso> findByRolNombreAndPermiso(String rolNombre, Permiso permiso);
    Optional<RolPermiso> findByRolNombreAndPermisoIdPermiso(String rolNombre, Integer idPermiso); // Añadido por si es útil
    boolean existsByRolNombreAndPermiso(String rolNombre, Permiso permiso);
    boolean existsByRolNombreAndPermisoIdPermiso(String rolNombre, Integer idPermiso); // Añadido por si es útil

    @Transactional
    void deleteByRolNombreAndPermiso(String rolNombre, Permiso permiso);

    @Transactional
    void deleteByRolNombreAndPermisoIdPermiso(String rolNombre, Integer idPermiso); // Añadido
}