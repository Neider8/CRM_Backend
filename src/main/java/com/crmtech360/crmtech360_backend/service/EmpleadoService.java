package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.EmpleadoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpleadoService {

    EmpleadoResponseDTO createEmpleado(EmpleadoCreateRequestDTO createDTO);

    Page<EmpleadoResponseDTO> findAllEmpleados(Pageable pageable);

    EmpleadoResponseDTO findEmpleadoById(Integer id);

    EmpleadoResponseDTO findEmpleadoByNumeroDocumento(String numeroDocumento);

    EmpleadoResponseDTO updateEmpleado(Integer id, EmpleadoUpdateRequestDTO updateDTO);

    void deleteEmpleado(Integer id);
}