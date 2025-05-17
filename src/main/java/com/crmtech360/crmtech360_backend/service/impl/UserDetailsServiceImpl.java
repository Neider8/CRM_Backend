package com.crmtech360.crmtech360_backend.service.impl;

import com.crmtech360.crmtech360_backend.entity.Permiso;
import com.crmtech360.crmtech360_backend.entity.RolPermiso;
import com.crmtech360.crmtech360_backend.entity.Usuario;
import com.crmtech360.crmtech360_backend.repository.RolPermisoRepository;
import com.crmtech360.crmtech360_backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User; // Correct import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userDetailsService") // Nombre explícito del bean
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UsuarioRepository usuarioRepository;
    private final RolPermisoRepository rolPermisoRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository, RolPermisoRepository rolPermisoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolPermisoRepository = rolPermisoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando cargar usuario por nombre de usuario: {}", username);

        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con nombre de usuario: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
                });

        log.info("Usuario encontrado: {}, Rol: {}", usuario.getNombreUsuario(), usuario.getRolUsuario());

        Set<GrantedAuthority> authorities = new HashSet<>();

        // 1. Añadir el ROL principal del usuario.
        if (usuario.getRolUsuario() != null && !usuario.getRolUsuario().trim().isEmpty()) {
            String roleName = "ROLE_" + usuario.getRolUsuario().toUpperCase().replace(" ", "_");
            authorities.add(new SimpleGrantedAuthority(roleName));
            log.debug("Rol principal añadido como autoridad: {}", roleName);
        } else {
            log.warn("El usuario '{}' no tiene un rol principal asignado o está vacío.", username);
        }

        // 2. Añadir PERMISOS específicos asociados al rol del usuario.
        if (usuario.getRolUsuario() != null && !usuario.getRolUsuario().trim().isEmpty()) {
            // Usar un método que haga JOIN FETCH para evitar N+1 si es posible,
            // o asegurar que la carga LAZY funcione dentro de la transacción.
            // El método findByRolNombre en RolPermisoRepository debería cargar los Permisos.
            // Si RolPermiso.permiso es LAZY, la transacción @Transactional(readOnly = true) lo manejará.
            // Alternativamente, RolPermisoRepository puede tener un método específico con JOIN FETCH.
            // Voy a asumir que tu `RolPermisoRepository.findByRolNombre` funciona bien en este contexto transaccional
            // o que tienes un método como `findByRolNombreWithPermisos` (que has añadido en una versión posterior del repo).
            // Aquí usaré findByRolNombreWithPermisos para ser explícito.
            List<RolPermiso> rolesPermisos = rolPermisoRepository.findByRolNombreWithPermisos(usuario.getRolUsuario());
            if (rolesPermisos.isEmpty()) {
                log.info("El rol '{}' para el usuario '{}' no tiene permisos específicos asignados en la tabla RolesPermisos.", usuario.getRolUsuario(), username);
            } else {
                rolesPermisos.forEach(rp -> {
                    Permiso permiso = rp.getPermiso();
                    if (permiso != null && permiso.getNombrePermiso() != null && !permiso.getNombrePermiso().trim().isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority(permiso.getNombrePermiso().toUpperCase().replace(" ", "_")));
                        log.debug("Permiso específico añadido como autoridad para el rol '{}': {}", usuario.getRolUsuario(), permiso.getNombrePermiso().toUpperCase().replace(" ", "_"));
                    } else {
                        log.warn("Se encontró una asignación de RolPermiso (ID: {}) para el rol '{}' pero el permiso o su nombre es nulo/vacío.",
                                rp.getIdRolPermiso() != null ? rp.getIdRolPermiso() : "N/A",
                                usuario.getRolUsuario());
                    }
                });
            }
        } else {
            log.warn("No se pueden cargar permisos específicos para el usuario '{}' porque no tiene un rol principal definido.", username);
        }

        if(authorities.isEmpty()){
            log.warn("El usuario '{}' no tiene roles ni permisos asignados. La autenticación podría funcionar pero la autorización fallará para recursos protegidos.", username);
        }

        // Los flags booleanos indican el estado de la cuenta.
        // Por ahora, los asumimos como true. Podrías tener campos en tu entidad Usuario
        // para manejar esto (ej. isEnabled, isAccountNonExpired, etc.)
        return new User(
                usuario.getNombreUsuario(),
                usuario.getContrasena(), // La contraseña ya debe estar codificada en la BD
                true, // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}