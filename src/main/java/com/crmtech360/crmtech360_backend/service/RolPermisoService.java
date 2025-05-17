package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO; // Para devolver la lista de permisos
import com.crmtech360.crmtech360_backend.dto.RolPermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.RolPermisoResponseDTO;
import java.util.List;

public interface RolPermisoService {

    RolPermisoResponseDTO assignPermisoToRol(RolPermisoRequestDTO requestDTO);

    void removePermisoFromRol(String rolNombre, Integer idPermiso);

    List<PermisoResponseDTO> getPermisosForRol(String rolNombre);

    boolean checkIfRolHasPermiso(String rolNombre, Integer idPermiso);
}