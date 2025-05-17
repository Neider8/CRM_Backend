package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.PermisoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.RolPermisoRequestDTO;
import com.crmtech360.crmtech360_backend.dto.RolPermisoResponseDTO;
import com.crmtech360.crmtech360_backend.entity.Permiso;
import com.crmtech360.crmtech360_backend.entity.RolPermiso;
import com.crmtech360.crmtech360_backend.repository.PermisoRepository;
import com.crmtech360.crmtech360_backend.repository.RolPermisoRepository;
import com.crmtech360.crmtech360_backend.service.RolPermisoService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolPermisoServiceImpl implements RolPermisoService {

    private static final Logger log = LoggerFactory.getLogger(RolPermisoServiceImpl.class);

    private final RolPermisoRepository rolPermisoRepository;
    private final PermisoRepository permisoRepository; // Para validar existencia del permiso

    public RolPermisoServiceImpl(RolPermisoRepository rolPermisoRepository, PermisoRepository permisoRepository) {
        this.rolPermisoRepository = rolPermisoRepository;
        this.permisoRepository = permisoRepository;
    }

    @Override
    public RolPermisoResponseDTO assignPermisoToRol(RolPermisoRequestDTO requestDTO) {
        log.info("Asignando permiso ID {} al rol '{}'", requestDTO.getIdPermiso(), requestDTO.getRolNombre());

        Permiso permiso = permisoRepository.findById(requestDTO.getIdPermiso())
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", requestDTO.getIdPermiso()));

        if (rolPermisoRepository.existsByRolNombreAndPermiso(requestDTO.getRolNombre(), permiso)) {
            throw new DuplicateResourceException("RolPermiso", "rolNombre/idPermiso", requestDTO.getRolNombre() + "/" + requestDTO.getIdPermiso());
        }

        RolPermiso rolPermiso = new RolPermiso();
        rolPermiso.setRolNombre(requestDTO.getRolNombre());
        rolPermiso.setPermiso(permiso);

        RolPermiso savedEntity = rolPermisoRepository.save(rolPermiso);
        log.info("Permiso ID {} asignado al rol '{}' con ID de asignación {}", permiso.getIdPermiso(), requestDTO.getRolNombre(), savedEntity.getIdRolPermiso());
        return mapToResponseDTO(savedEntity);
    }

    @Override
    public void removePermisoFromRol(String rolNombre, Integer idPermiso) {
        log.info("Removiendo permiso ID {} del rol '{}'", idPermiso, rolNombre);
        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() -> new ResourceNotFoundException("Permiso", "id", idPermiso));

        RolPermiso rolPermiso = rolPermisoRepository.findByRolNombreAndPermiso(rolNombre, permiso)
                .orElseThrow(() -> new ResourceNotFoundException("RolPermiso", "rolNombre/permisoId", rolNombre + "/" + idPermiso));

        rolPermisoRepository.delete(rolPermiso);
        log.info("Permiso ID {} removido del rol '{}'", idPermiso, rolNombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermisoResponseDTO> getPermisosForRol(String rolNombre) {
        log.info("Obteniendo permisos para el rol '{}'", rolNombre);
        List<RolPermiso> rolesPermisos = rolPermisoRepository.findByRolNombre(rolNombre);
        return rolesPermisos.stream()
                .map(rp -> mapPermisoToResponseDTO(rp.getPermiso()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkIfRolHasPermiso(String rolNombre, Integer idPermiso) {
        Permiso permiso = permisoRepository.findById(idPermiso).orElse(null);
        if (permiso == null) return false;
        return rolPermisoRepository.existsByRolNombreAndPermiso(rolNombre, permiso);
    }

    // --- Métodos de Mapeo Privados ---
    private RolPermisoResponseDTO mapToResponseDTO(RolPermiso entity) {
        return new RolPermisoResponseDTO(
                entity.getIdRolPermiso(),
                entity.getRolNombre(),
                mapPermisoToResponseDTO(entity.getPermiso())
        );
    }

    private PermisoResponseDTO mapPermisoToResponseDTO(Permiso permiso) {
        if (permiso == null) return null;
        return new PermisoResponseDTO(permiso.getIdPermiso(), permiso.getNombrePermiso());
    }
}