package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.EmpleadoCreateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoResponseDTO;
import com.crmtech360.crmtech360_backend.dto.EmpleadoUpdateRequestDTO;
import com.crmtech360.crmtech360_backend.dto.UsuarioSummaryDTO; // Para el DTO anidado
import com.crmtech360.crmtech360_backend.entity.Empleado;
import com.crmtech360.crmtech360_backend.entity.Usuario;
import com.crmtech360.crmtech360_backend.repository.EmpleadoRepository;
import com.crmtech360.crmtech360_backend.repository.UsuarioRepository; // Para desvincular usuarios
import com.crmtech360.crmtech360_backend.service.EmpleadoService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository; // Para manejar la desvinculación

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, UsuarioRepository usuarioRepository) {
        this.empleadoRepository = empleadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public EmpleadoResponseDTO createEmpleado(EmpleadoCreateRequestDTO createDTO) {
        log.info("Intentando crear empleado con documento: {}", createDTO.getNumeroDocumento());
        empleadoRepository.findByNumeroDocumento(createDTO.getNumeroDocumento())
                .ifPresent(existing -> {
                    log.warn("Intento de crear empleado duplicado por documento: {}", createDTO.getNumeroDocumento());
                    throw new DuplicateResourceException("Empleado", "numeroDocumento", createDTO.getNumeroDocumento());
                });

        Empleado empleado = mapToEntity(createDTO);
        Empleado savedEmpleado = empleadoRepository.save(empleado);
        log.info("Empleado creado con ID: {}", savedEmpleado.getIdEmpleado());
        return mapToResponseDTO(savedEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponseDTO> findAllEmpleados(Pageable pageable) {
        log.info("Buscando todos los empleados, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return empleadoRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO findEmpleadoById(Integer id) {
        log.info("Buscando empleado con ID: {}", id);
        Empleado empleado = findEmpleadoEntityById(id);
        return mapToResponseDTO(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO findEmpleadoByNumeroDocumento(String numeroDocumento) {
        log.info("Buscando empleado con documento: {}", numeroDocumento);
        Empleado empleado = empleadoRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con documento: {}", numeroDocumento);
                    return new ResourceNotFoundException("Empleado", "numeroDocumento", numeroDocumento);
                });
        return mapToResponseDTO(empleado);
    }

    @Override
    public EmpleadoResponseDTO updateEmpleado(Integer id, EmpleadoUpdateRequestDTO updateDTO) {
        log.info("Intentando actualizar empleado con ID: {}", id);
        Empleado empleadoToUpdate = findEmpleadoEntityById(id);

        // No se actualiza tipo ni número de documento aquí. Se asumen fijos.
        if (updateDTO.getNombreEmpleado() != null) {
            empleadoToUpdate.setNombreEmpleado(updateDTO.getNombreEmpleado());
        }
        if (updateDTO.getCargoEmpleado() != null) {
            empleadoToUpdate.setCargoEmpleado(updateDTO.getCargoEmpleado());
        }
        if (updateDTO.getAreaEmpleado() != null) {
            empleadoToUpdate.setAreaEmpleado(updateDTO.getAreaEmpleado());
        }
        if (updateDTO.getSalarioEmpleado() != null) {
            empleadoToUpdate.setSalarioEmpleado(updateDTO.getSalarioEmpleado());
        }
        if (updateDTO.getFechaContratacionEmpleado() != null) {
            empleadoToUpdate.setFechaContratacionEmpleado(updateDTO.getFechaContratacionEmpleado());
        }

        Empleado updatedEmpleado = empleadoRepository.save(empleadoToUpdate);
        log.info("Empleado actualizado con ID: {}", updatedEmpleado.getIdEmpleado());
        return mapToResponseDTO(updatedEmpleado);
    }

    @Override
    public void deleteEmpleado(Integer id) {
        log.info("Intentando eliminar empleado con ID: {}", id);
        Empleado empleadoToDelete = findEmpleadoEntityById(id);

        // La BD tiene ON DELETE SET NULL para Usuarios.id_empleado y TareasProduccion.id_empleado.
        // Si un usuario está vinculado, su campo id_empleado se volverá NULL.
        // Para TareasProduccion, igual.
        // No se necesita intervención manual aquí si la BD lo maneja, pero es bueno ser consciente.
        // Opcionalmente, se podría desvincular explícitamente aquí:
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmpleadoIdEmpleado(id);
        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            log.info("Desvinculando empleado ID {} del usuario ID {}", id, usuario.getIdUsuario());
            usuario.setEmpleado(null); // Asegura que JPA haga el SET NULL
            usuarioRepository.save(usuario); // Guarda el cambio en el usuario
        }
        // Lógica similar podría ser necesaria para TareasProduccion si no se confía plenamente en el ON DELETE SET NULL de la BD vía JPA.

        empleadoRepository.delete(empleadoToDelete);
        log.info("Empleado eliminado con ID: {}", id);
    }

    // --- Métodos de Ayuda y Mapeo Privados ---
    private Empleado findEmpleadoEntityById(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Empleado", "id", id);
                });
    }

    private Empleado mapToEntity(EmpleadoCreateRequestDTO dto) {
        Empleado entity = new Empleado();
        entity.setTipoDocumento(dto.getTipoDocumento());
        entity.setNumeroDocumento(dto.getNumeroDocumento());
        entity.setNombreEmpleado(dto.getNombreEmpleado());
        entity.setCargoEmpleado(dto.getCargoEmpleado());
        entity.setAreaEmpleado(dto.getAreaEmpleado());
        entity.setSalarioEmpleado(dto.getSalarioEmpleado());
        entity.setFechaContratacionEmpleado(dto.getFechaContratacionEmpleado());
        return entity;
    }

    private EmpleadoResponseDTO mapToResponseDTO(Empleado entity) {
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setIdEmpleado(entity.getIdEmpleado());
        dto.setTipoDocumento(entity.getTipoDocumento());
        dto.setNumeroDocumento(entity.getNumeroDocumento());
        dto.setNombreEmpleado(entity.getNombreEmpleado());
        dto.setCargoEmpleado(entity.getCargoEmpleado());
        dto.setAreaEmpleado(entity.getAreaEmpleado());
        dto.setSalarioEmpleado(entity.getSalarioEmpleado());
        dto.setFechaContratacionEmpleado(entity.getFechaContratacionEmpleado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        // Mapear información resumida del usuario si está asociado
        if (entity.getUsuario() != null) {
            Usuario usuario = entity.getUsuario();
            dto.setUsuario(new UsuarioSummaryDTO(usuario.getIdUsuario(), usuario.getNombreUsuario(), usuario.getRolUsuario()));
        }
        return dto;
    }
}