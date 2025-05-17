package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.PermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;
import java.util.List;

public interface PermisoService {

    PermisoResponseDTO createPermiso(PermisoRequestDTO permisoRequestDTO);

    List<PermisoResponseDTO> findAllPermisos();

    PermisoResponseDTO findPermisoById(Integer id);

    PermisoResponseDTO findByNombrePermiso(String nombre);

    PermisoResponseDTO updatePermiso(Integer id, PermisoRequestDTO permisoRequestDTO);

    void deletePermiso(Integer id);

    boolean existsPermiso(String nombrePermiso);
}