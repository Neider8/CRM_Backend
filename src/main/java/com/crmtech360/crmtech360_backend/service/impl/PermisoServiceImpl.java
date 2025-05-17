package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.PermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;
import com.crmtech360.crmtech360_backend.entity.Permiso;
import com.crmtech360.crmtech360_backend.repository.PermisoRepository;
import com.crmtech360.crmtech360_backend.service.PermisoService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException; // Placeholder
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException; // Placeholder
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional // Aplica transaccionalidad por defecto a todos los métodos públicos
public class PermisoServiceImpl implements PermisoService {

    private static final Logger log = LoggerFactory.getLogger(PermisoServiceImpl.class);

    private final PermisoRepository permisoRepository;

    // Inyección por constructor
    public PermisoServiceImpl(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    @Override
    public PermisoResponseDTO createPermiso(PermisoRequestDTO permisoRequestDTO) {
        log.info("Intentando crear permiso con nombre: {}", permisoRequestDTO.getNombrePermiso());
        // Validar si ya existe un permiso con ese nombre (ignorando mayúsculas/minúsculas)
        permisoRepository.findByNombrePermiso(permisoRequestDTO.getNombrePermiso())
                .ifPresent(existing -> {
                    log.warn("Intento de crear permiso duplicado: {}", permisoRequestDTO.getNombrePermiso());
                    throw new DuplicateResourceException("Permiso", "nombrePermiso", permisoRequestDTO.getNombrePermiso());
                });

        Permiso permiso = mapToEntity(permisoRequestDTO);
        Permiso savedPermiso = permisoRepository.save(permiso);
        log.info("Permiso creado con ID: {}", savedPermiso.getIdPermiso());
        return mapToResponseDTO(savedPermiso);
    }

    @Override
    @Transactional(readOnly = true) // Optimización para consultas
    public List<PermisoResponseDTO> findAllPermisos() {
        log.info("Buscando todos los permisos");
        return permisoRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PermisoResponseDTO findPermisoById(Integer id) {
        log.info("Buscando permiso con ID: {}", id);
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Permiso no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Permiso", "id", id);
                });
        return mapToResponseDTO(permiso);
    }

    @Override
    @Transactional(readOnly = true)
    public PermisoResponseDTO findByNombrePermiso(String nombre) {
        log.info("Buscando permiso con nombre: {}", nombre);
        Permiso permiso = permisoRepository.findByNombrePermiso(nombre)
                .orElseThrow(() -> {
                    log.warn("Permiso no encontrado con nombre: {}", nombre);
                    return new ResourceNotFoundException("Permiso", "nombrePermiso", nombre);
                });
        return mapToResponseDTO(permiso);
    }


    @Override
    public PermisoResponseDTO updatePermiso(Integer id, PermisoRequestDTO permisoRequestDTO) {
        log.info("Intentando actualizar permiso con ID: {}", id);
        Permiso permisoToUpdate = permisoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Permiso no encontrado para actualizar con ID: {}", id);
                    return new ResourceNotFoundException("Permiso", "id", id);
                });

        // Verificar si el nuevo nombre ya existe en otro permiso
        permisoRepository.findByNombrePermiso(permisoRequestDTO.getNombrePermiso())
                .ifPresent(existing -> {
                    if (!existing.getIdPermiso().equals(id)) {
                        log.warn("Intento de actualizar a nombre de permiso duplicado: {}", permisoRequestDTO.getNombrePermiso());
                        throw new DuplicateResourceException("Permiso", "nombrePermiso", permisoRequestDTO.getNombrePermiso());
                    }
                });

        permisoToUpdate.setNombrePermiso(permisoRequestDTO.getNombrePermiso());
        // Nota: Las fechas de actualización son manejadas por @PreUpdate en la entidad

        Permiso updatedPermiso = permisoRepository.save(permisoToUpdate);
        log.info("Permiso actualizado con ID: {}", updatedPermiso.getIdPermiso());
        return mapToResponseDTO(updatedPermiso);
    }

    @Override
    public void deletePermiso(Integer id) {
        log.info("Intentando eliminar permiso con ID: {}", id);
        if (!permisoRepository.existsById(id)) {
            log.warn("Permiso no encontrado para eliminar con ID: {}", id);
            throw new ResourceNotFoundException("Permiso", "id", id);
        }
        // Considerar el impacto en RolesPermisos (ON DELETE CASCADE definido en BD debería manejarlo)
        permisoRepository.deleteById(id);
        log.info("Permiso eliminado con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPermiso(String nombrePermiso) {
        return permisoRepository.findByNombrePermiso(nombrePermiso).isPresent();
    }

    // --- Métodos de Mapeo Privados ---

    private Permiso mapToEntity(PermisoRequestDTO dto) {
        Permiso permiso = new Permiso();
        permiso.setNombrePermiso(dto.getNombrePermiso());
        return permiso;
    }

    private PermisoResponseDTO mapToResponseDTO(Permiso entity) {
        PermisoResponseDTO dto = new PermisoResponseDTO();
        dto.setIdPermiso(entity.getIdPermiso());
        dto.setNombrePermiso(entity.getNombrePermiso());
        return dto;
    }
}