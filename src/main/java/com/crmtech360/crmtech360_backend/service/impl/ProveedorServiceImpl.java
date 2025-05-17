package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.ProveedorCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorResponseDTO;
import com.crmtech360.crmtech360_backend.dto.ProveedorUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.entity.Proveedor;
import com.crmtech360.crmtech360_backend.repository.ProveedorRepository;
import com.crmtech360.crmtech360_backend.service.ProveedorService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException; // Placeholder
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException; // Placeholder
import com.crmtech360.crmtech360_backend.exception.BadRequestException; // Placeholder
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private static final Logger log = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public ProveedorResponseDTO createProveedor(ProveedorCreateRequestDTO createDTO) {
        log.info("Intentando crear proveedor con NIT: {}", createDTO.getNitProveedor());
        proveedorRepository.findByNitProveedor(createDTO.getNitProveedor())
                .ifPresent(existing -> {
                    log.warn("Intento de crear proveedor duplicado por NIT: {}", createDTO.getNitProveedor());
                    throw new DuplicateResourceException("Proveedor", "nitProveedor", createDTO.getNitProveedor());
                });

        Proveedor proveedor = mapToEntity(createDTO);
        Proveedor savedProveedor = proveedorRepository.save(proveedor);
        log.info("Proveedor creado con ID: {}", savedProveedor.getIdProveedor());
        return mapToResponseDTO(savedProveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProveedorResponseDTO> findAllProveedores(Pageable pageable) {
        log.info("Buscando todos los proveedores, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return proveedorRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO findProveedorById(Integer id) {
        log.info("Buscando proveedor con ID: {}", id);
        Proveedor proveedor = findProveedorEntityById(id);
        return mapToResponseDTO(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO findProveedorByNit(String nit) {
        log.info("Buscando proveedor con NIT: {}", nit);
        Proveedor proveedor = proveedorRepository.findByNitProveedor(nit)
                .orElseThrow(() -> {
                    log.warn("Proveedor no encontrado con NIT: {}", nit);
                    return new ResourceNotFoundException("Proveedor", "nitProveedor", nit);
                });
        return mapToResponseDTO(proveedor);
    }

    @Override
    public ProveedorResponseDTO updateProveedor(Integer id, ProveedorUpdateRequestDTO updateDTO) {
        log.info("Intentando actualizar proveedor con ID: {}", id);
        Proveedor proveedorToUpdate = findProveedorEntityById(id);

        // NIT no se actualiza. Actualizar otros campos si vienen en el DTO.
        if (updateDTO.getNombreComercialProveedor() != null) {
            proveedorToUpdate.setNombreComercialProveedor(updateDTO.getNombreComercialProveedor());
        }
        if (updateDTO.getRazonSocialProveedor() != null) {
            proveedorToUpdate.setRazonSocialProveedor(updateDTO.getRazonSocialProveedor());
        }
        if (updateDTO.getDireccionProveedor() != null) {
            proveedorToUpdate.setDireccionProveedor(updateDTO.getDireccionProveedor());
        }
        if (updateDTO.getTelefonoProveedor() != null) {
            proveedorToUpdate.setTelefonoProveedor(updateDTO.getTelefonoProveedor());
        }
        if (updateDTO.getCorreoProveedor() != null) {
            proveedorToUpdate.setCorreoProveedor(updateDTO.getCorreoProveedor());
        }
        if (updateDTO.getContactoPrincipalProveedor() != null) {
            proveedorToUpdate.setContactoPrincipalProveedor(updateDTO.getContactoPrincipalProveedor());
        }

        Proveedor updatedProveedor = proveedorRepository.save(proveedorToUpdate);
        log.info("Proveedor actualizado con ID: {}", updatedProveedor.getIdProveedor());
        return mapToResponseDTO(updatedProveedor);
    }

    @Override
    public void deleteProveedor(Integer id) {
        log.info("Intentando eliminar proveedor con ID: {}", id);
        Proveedor proveedor = findProveedorEntityById(id);
        // Considerar impacto en OrdenesCompra (ON DELETE SET NULL en BD)
        // Spring Data/Hibernate debería manejar el SET NULL si la relación es opcional y no hay cascade.
        // Podríamos verificar si hay órdenes de compra PENDIENTES antes de eliminar.
        // if (ordenCompraRepository.existsByProveedorAndEstadoCompraIn(proveedor, List.of("Pendiente", "Enviada", "Recibida Parcial"))) {
        //    throw new BadRequestException("No se puede eliminar el proveedor ID " + id + " porque tiene órdenes de compra activas.");
        // }
        proveedorRepository.delete(proveedor);
        log.info("Proveedor eliminado con ID: {}", id);
    }


    // --- Métodos de Ayuda y Mapeo Privados ---

    private Proveedor findProveedorEntityById(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Proveedor no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Proveedor", "id", id);
                });
    }

    private Proveedor mapToEntity(ProveedorCreateRequestDTO dto) {
        Proveedor entity = new Proveedor();
        entity.setNombreComercialProveedor(dto.getNombreComercialProveedor());
        entity.setRazonSocialProveedor(dto.getRazonSocialProveedor());
        entity.setNitProveedor(dto.getNitProveedor());
        entity.setDireccionProveedor(dto.getDireccionProveedor());
        entity.setTelefonoProveedor(dto.getTelefonoProveedor());
        entity.setCorreoProveedor(dto.getCorreoProveedor());
        entity.setContactoPrincipalProveedor(dto.getContactoPrincipalProveedor());
        return entity;
    }

    private ProveedorResponseDTO mapToResponseDTO(Proveedor entity) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setIdProveedor(entity.getIdProveedor());
        dto.setNombreComercialProveedor(entity.getNombreComercialProveedor());
        dto.setRazonSocialProveedor(entity.getRazonSocialProveedor());
        dto.setNitProveedor(entity.getNitProveedor());
        dto.setDireccionProveedor(entity.getDireccionProveedor());
        dto.setTelefonoProveedor(entity.getTelefonoProveedor());
        dto.setCorreoProveedor(entity.getCorreoProveedor());
        dto.setContactoPrincipalProveedor(entity.getContactoPrincipalProveedor());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }
}