package com.crmtech360.crmtech360_backend.repository;

import com.crmtech360.crmtech360_backend.entity.Usuario;
import com.crmtech360.crmtech360_backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario); // Crucial para login
    Optional<Usuario> findByEmpleado(Empleado empleado);
    Optional<Usuario> findByEmpleadoIdEmpleado(Integer idEmpleado);
    List<Usuario> findByRolUsuario(String rolUsuario);
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmpleado(Empleado empleado);
    boolean existsByEmpleadoIdEmpleado(Integer idEmpleado);
}