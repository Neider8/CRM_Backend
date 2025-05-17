package com.crmtech360.crmtech360_backend.service;

import com.crmtech360.crmtech360_backend.dto.ProveedorCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProveedorService {

    ProveedorResponseDTO createProveedor(ProveedorCreateRequestDTO createDTO);

    Page<ProveedorResponseDTO> findAllProveedores(Pageable pageable);

    ProveedorResponseDTO findProveedorById(Integer id);

    ProveedorResponseDTO findProveedorByNit(String nit);

    ProveedorResponseDTO updateProveedor(Integer id, ProveedorUpdateRequestDTO updateDTO);

    void deleteProveedor(Integer id);
}