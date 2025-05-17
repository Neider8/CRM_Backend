package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.InsumoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.InsumoUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.entity.Insumo;
import com.crmtech360.crmtech360_backend.repository.InsumoRepository;
import com.crmtech360.crmtech360_backend.service.InsumoService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import com.crmtech360.crmtech360_backend.exception.BadRequestException; // <--- ¡AÑADE ESTA LÍNEA!
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsumoServiceImpl implements InsumoService {

    private static final Logger log = LoggerFactory.getLogger(InsumoServiceImpl.class);

    private final InsumoRepository insumoRepository;

    public InsumoServiceImpl(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    @Override
    public InsumoResponseDTO createInsumo(InsumoCreateRequestDTO createDTO) {
        log.info("Intentando crear insumo con nombre: {}", createDTO.getNombreInsumo());
        insumoRepository.findByNombreInsumoIgnoreCase(createDTO.getNombreInsumo())
                .ifPresent(existing -> {
                    log.warn("Intento de crear insumo duplicado por nombre: {}", createDTO.getNombreInsumo());
                    throw new DuplicateResourceException("Insumo", "nombreInsumo", createDTO.getNombreInsumo());
                });

        Insumo insumo = mapToEntity(createDTO);
        Insumo savedInsumo = insumoRepository.save(insumo);
        log.info("Insumo creado con ID: {}", savedInsumo.getIdInsumo());
        return mapToResponseDTO(savedInsumo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InsumoResponseDTO> findAllInsumos(Pageable pageable) {
        log.info("Buscando todos los insumos, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return insumoRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO findInsumoById(Integer id) {
        log.info("Buscando insumo con ID: {}", id);
        Insumo insumo = findInsumoEntityById(id);
        return mapToResponseDTO(insumo);
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO findInsumoByNombre(String nombre) {
        log.info("Buscando insumo con nombre: {}", nombre);
        Insumo insumo = insumoRepository.findByNombreInsumoIgnoreCase(nombre)
                .orElseThrow(() -> {
                    log.warn("Insumo no encontrado con nombre: {}", nombre);
                    return new ResourceNotFoundException("Insumo", "nombreInsumo", nombre);
                });
        return mapToResponseDTO(insumo);
    }

    @Override
    public InsumoResponseDTO updateInsumo(Integer id, InsumoUpdateRequestDTO updateDTO) {
        log.info("Intentando actualizar insumo con ID: {}", id);
        Insumo insumoToUpdate = findInsumoEntityById(id);

        // Validar nombre duplicado si se cambia
        if (updateDTO.getNombreInsumo() != null && !insumoToUpdate.getNombreInsumo().equalsIgnoreCase(updateDTO.getNombreInsumo())) {
            insumoRepository.findByNombreInsumoIgnoreCase(updateDTO.getNombreInsumo())
                    .ifPresent(existing -> {
                        if (!existing.getIdInsumo().equals(id)) { // Asegurarse que no es el mismo insumo
                            log.warn("Intento de actualizar a nombre de insumo duplicado: {}", updateDTO.getNombreInsumo());
                            throw new DuplicateResourceException("Insumo", "nombreInsumo", updateDTO.getNombreInsumo());
                        }
                    });
            insumoToUpdate.setNombreInsumo(updateDTO.getNombreInsumo());
        }

        // Actualizar otros campos
        if (updateDTO.getDescripcionInsumo() != null) {
            insumoToUpdate.setDescripcionInsumo(updateDTO.getDescripcionInsumo());
        }
        if (updateDTO.getUnidadMedidaInsumo() != null) {
            insumoToUpdate.setUnidadMedidaInsumo(updateDTO.getUnidadMedidaInsumo());
        }
        if (updateDTO.getStockMinimoInsumo() != null) {
            insumoToUpdate.setStockMinimoInsumo(updateDTO.getStockMinimoInsumo());
        }

        Insumo updatedInsumo = insumoRepository.save(insumoToUpdate);
        log.info("Insumo actualizado con ID: {}", updatedInsumo.getIdInsumo());
        return mapToResponseDTO(updatedInsumo);
    }

    @Override
    public void deleteInsumo(Integer id) {
        log.info("Intentando eliminar insumo con ID: {}", id);
        Insumo insumo = findInsumoEntityById(id);
        // Verificar dependencias antes de borrar (InsumoPorProducto, DetallesOrdenCompra, InventarioInsumos)
        // La BD tiene ON DELETE RESTRICT en DetallesOrdenCompra, podría lanzar DataIntegrityViolationException
        try {
            insumoRepository.delete(insumo);
            log.info("Insumo eliminado con ID: {}", id);
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al eliminar insumo ID {}: {}", id, e.getMessage());
            // Podríamos verificar específicamente qué relación falló
            throw new BadRequestException("No se puede eliminar el insumo ID " + id + " porque está siendo utilizado en órdenes de compra, inventario o lista de materiales.");
        }
    }

    // --- Métodos de Ayuda y Mapeo Privados ---

    private Insumo findInsumoEntityById(Integer id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Insumo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Insumo", "id", id);
                });
    }

    private Insumo mapToEntity(InsumoCreateRequestDTO dto) {
        Insumo entity = new Insumo();
        entity.setNombreInsumo(dto.getNombreInsumo());
        entity.setDescripcionInsumo(dto.getDescripcionInsumo());
        entity.setUnidadMedidaInsumo(dto.getUnidadMedidaInsumo());
        entity.setStockMinimoInsumo(dto.getStockMinimoInsumo()); // Ya tiene valor por defecto en DTO/Entidad si es null
        return entity;
    }

    private InsumoResponseDTO mapToResponseDTO(Insumo entity) {
        InsumoResponseDTO dto = new InsumoResponseDTO();
        dto.setIdInsumo(entity.getIdInsumo());
        dto.setNombreInsumo(entity.getNombreInsumo());
        dto.setDescripcionInsumo(entity.getDescripcionInsumo());
        dto.setUnidadMedidaInsumo(entity.getUnidadMedidaInsumo());
        dto.setStockMinimoInsumo(entity.getStockMinimoInsumo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());
        return dto;
    }
}