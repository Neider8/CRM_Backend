package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.InsumoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsumoService {

    InsumoResponseDTO createInsumo(InsumoCreateRequestDTO insumoCreateRequestDTO);

    Page<InsumoResponseDTO> findAllInsumos(Pageable pageable);

    InsumoResponseDTO findInsumoById(Integer id);

    InsumoResponseDTO findInsumoByNombre(String nombre);

    InsumoResponseDTO updateInsumo(Integer id, InsumoUpdateRequestDTO insumoUpdateRequestDTO);

    void deleteInsumo(Integer id);
}