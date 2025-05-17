package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.dto.*;
import com.crmtech360.crmtech360_backend.entity.Empleado;
import com.crmtech360.crmtech360_backend.entity.Usuario;
import com.crmtech360.crmtech360_backend.repository.EmpleadoRepository;
import com.crmtech360.crmtech360_backend.repository.UsuarioRepository;
import com.crmtech360.crmtech360_backend.service.UsuarioService;
import com.crmtech360.crmtech360_backend.exception.ResourceNotFoundException;
import com.crmtech360.crmtech360_backend.exception.DuplicateResourceException;
import com.crmtech360.crmtech360_backend.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder; // Para codificar contraseñas
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository; // Para vincular empleados
    private final PasswordEncoder passwordEncoder; // Para hashear contraseñas

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              EmpleadoRepository empleadoRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.empleadoRepository = empleadoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO createUsuario(UsuarioCreateRequestDTO createDTO) {
        log.info("Intentando crear usuario: {}", createDTO.getNombreUsuario());
        usuarioRepository.findByNombreUsuario(createDTO.getNombreUsuario())
                .ifPresent(e -> {
                    log.warn("Nombre de usuario ya existe: {}", createDTO.getNombreUsuario());
                    throw new DuplicateResourceException("Usuario", "nombreUsuario", createDTO.getNombreUsuario());
                });

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(createDTO.getNombreUsuario());
        usuario.setContrasena(passwordEncoder.encode(createDTO.getContrasena()));
        usuario.setRolUsuario(createDTO.getRolUsuario());

        if (createDTO.getIdEmpleado() != null) {
            Empleado empleado = empleadoRepository.findById(createDTO.getIdEmpleado())
                    .orElseThrow(() -> {
                        log.warn("Empleado no encontrado con ID: {} para asociar a usuario", createDTO.getIdEmpleado());
                        return new ResourceNotFoundException("Empleado", "id", createDTO.getIdEmpleado());
                    });
            // Verificar si el empleado ya está asociado a otro usuario
            if (empleado.getUsuario() != null) {
                log.warn("Empleado ID {} ya está asociado al usuario ID {}", createDTO.getIdEmpleado(), empleado.getUsuario().getIdUsuario());
                throw new BadRequestException("El empleado con ID " + createDTO.getIdEmpleado() + " ya está asociado a otro usuario.");
            }
            usuario.setEmpleado(empleado);
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        log.info("Usuario creado con ID: {}", savedUsuario.getIdUsuario());
        return mapToResponseDTO(savedUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> findAllUsuarios(Pageable pageable) {
        log.info("Buscando todos los usuarios, página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        return usuarioRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findUsuarioById(Integer id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = findUsuarioEntityById(id);
        return mapToResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO findUsuarioByNombreUsuario(String nombreUsuario) {
        log.info("Buscando usuario por nombre: {}", nombreUsuario);
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con nombre: {}", nombreUsuario);
                    return new ResourceNotFoundException("Usuario", "nombreUsuario", nombreUsuario);
                });
        return mapToResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO updateUsuario(Integer id, UsuarioUpdateRequestDTO updateDTO) {
        log.info("Intentando actualizar usuario con ID: {}", id);
        Usuario usuarioToUpdate = findUsuarioEntityById(id);

        if (updateDTO.getRolUsuario() != null) {
            usuarioToUpdate.setRolUsuario(updateDTO.getRolUsuario());
        }

        if (updateDTO.getIdEmpleado() != null) {
            // Si se quiere cambiar o asignar empleado
            Empleado empleado = empleadoRepository.findById(updateDTO.getIdEmpleado())
                    .orElseThrow(() -> new ResourceNotFoundException("Empleado", "id", updateDTO.getIdEmpleado()));
            // Verificar si el nuevo empleado ya está asociado a OTRO usuario
            if (empleado.getUsuario() != null && !empleado.getUsuario().getIdUsuario().equals(id)) {
                throw new BadRequestException("El empleado con ID " + updateDTO.getIdEmpleado() + " ya está asociado a otro usuario.");
            }
            usuarioToUpdate.setEmpleado(empleado);
        } else {
            // Si se pasa idEmpleado null explícitamente (para desvincular)
            // O si la lógica de negocio es que si no viene no se toca, este 'else' puede quitarse
            // y manejar el desvincular con un método específico o un valor especial.
            // Asumamos que si idEmpleado es null en el DTO, queremos desvincular si estaba vinculado.
            usuarioToUpdate.setEmpleado(null);
        }

        Usuario updatedUsuario = usuarioRepository.save(usuarioToUpdate);
        log.info("Usuario actualizado con ID: {}", updatedUsuario.getIdUsuario());
        return mapToResponseDTO(updatedUsuario);
    }

    @Override
    public void changePassword(Integer idUsuario, ChangePasswordRequestDTO passwordRequestDTO) {
        log.info("Intentando cambiar contraseña para usuario ID: {}", idUsuario);
        Usuario usuario = findUsuarioEntityById(idUsuario);

        if (!passwordEncoder.matches(passwordRequestDTO.getCurrentPassword(), usuario.getContrasena())) {
            log.warn("Intento de cambio de contraseña fallido por contraseña actual incorrecta para usuario ID: {}", idUsuario);
            throw new BadRequestException("La contraseña actual es incorrecta.");
        }
        if (passwordRequestDTO.getCurrentPassword().equals(passwordRequestDTO.getNewPassword())) {
            throw new BadRequestException("La nueva contraseña no puede ser igual a la contraseña actual.");
        }

        usuario.setContrasena(passwordEncoder.encode(passwordRequestDTO.getNewPassword()));
        usuarioRepository.save(usuario);
        log.info("Contraseña cambiada exitosamente para usuario ID: {}", idUsuario);
    }


    @Override
    public void deleteUsuario(Integer id) {
        log.info("Intentando eliminar usuario con ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        // La BD no define CASCADE para usuarios, así que una eliminación simple es suficiente
        // a menos que haya lógica de negocio adicional (ej. reasignar tareas).
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado con ID: {}", id);
    }

    // --- Métodos de Ayuda y Mapeo Privados ---
    private Usuario findUsuarioEntityById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Usuario", "id", id);
                });
    }

    private UsuarioResponseDTO mapToResponseDTO(Usuario entity) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setNombreUsuario(entity.getNombreUsuario());
        dto.setRolUsuario(entity.getRolUsuario());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setFechaActualizacion(entity.getFechaActualizacion());

        if (entity.getEmpleado() != null) {
            Empleado emp = entity.getEmpleado();
            dto.setEmpleado(new EmpleadoSummaryDTO(emp.getIdEmpleado(), emp.getNumeroDocumento(), emp.getNombreEmpleado()));
        }
        return dto;
    }
}