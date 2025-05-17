package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.ChangePasswordRequestDTO; // Necesitaremos este DTO
import com.crmtech360.crmtech360_backend.dto.UsuarioCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.UsuarioResponseDTO;
import com.crmtech360.crmtech360_backend.dto.UsuarioUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponseDTO createUsuario(UsuarioCreateRequestDTO createDTO);

    Page<UsuarioResponseDTO> findAllUsuarios(Pageable pageable);

    UsuarioResponseDTO findUsuarioById(Integer id);

    UsuarioResponseDTO findUsuarioByNombreUsuario(String nombreUsuario);

    UsuarioResponseDTO updateUsuario(Integer id, UsuarioUpdateRequestDTO updateDTO);

    void changePassword(Integer idUsuario, ChangePasswordRequestDTO passwordRequestDTO);

    void deleteUsuario(Integer id);

    // Podríamos añadir métodos para asignar/desasignar empleado, cambiar rol específicamente, etc.
}