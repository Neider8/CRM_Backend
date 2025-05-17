package com.crmtech360.crmtech360_backend.security.config;

import com.crmtech360.crmtech360_backend.entity.Usuario; // Asegúrate de que la entidad Usuario tenga el campo 'empleado'
import com.crmtech360.crmtech360_backend.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; // Es buena práctica usar inyección por constructor
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    private static final Logger log = LoggerFactory.getLogger(UserSecurity.class);

    private final UsuarioRepository usuarioRepository;

    @Autowired // Inyección por constructor es preferida
    public UserSecurity(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verifica si el ID de usuario proporcionado coincide con el ID del usuario autenticado.
     * Útil para permitir a un usuario acceder o modificar sus propios datos.
     *
     * @param authentication El objeto de autenticación actual.
     * @param idResourceOwner El ID del usuario que es dueño del recurso.
     * @return true si el usuario autenticado es el dueño del recurso, false en caso contrario.
     */
    public boolean hasUserId(Authentication authentication, Integer idResourceOwner) {
        if (idResourceOwner == null) {
            log.warn("Intento de verificación de hasUserId con idResourceOwner nulo.");
            return false;
        }
        if (!isAuthenticated(authentication)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.debug("Verificando hasUserId: Usuario autenticado='{}', ID del recurso={}", userDetails.getUsername(), idResourceOwner);

        return usuarioRepository.findByNombreUsuario(userDetails.getUsername())
                .map(usuarioAutenticado -> {
                    boolean isOwner = usuarioAutenticado.getIdUsuario().equals(idResourceOwner);
                    log.trace("ID Usuario autenticado: {}, ID Recurso: {}, Coinciden: {}",
                            usuarioAutenticado.getIdUsuario(), idResourceOwner, isOwner);
                    return isOwner;
                })
                .orElseGet(() -> {
                    log.warn("Usuario autenticado '{}' no encontrado en la base de datos durante la verificación de hasUserId.", userDetails.getUsername());
                    return false;
                });
    }

    /**
     * Verifica si el nombre de usuario proporcionado (generalmente desde un path variable)
     * coincide con el nombre de usuario del principal autenticado.
     *
     * @param authentication El objeto de autenticación actual.
     * @param usernameFromPath El nombre de usuario extraído de la ruta o solicitud.
     * @return true si los nombres de usuario coinciden, false en caso contrario.
     */
    public boolean isOwner(Authentication authentication, String usernameFromPath) {
        if (usernameFromPath == null || usernameFromPath.isBlank()) {
            log.warn("Intento de verificación de isOwner con usernameFromPath nulo o vacío.");
            return false;
        }
        if (!isAuthenticated(authentication)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isMatch = userDetails.getUsername().equalsIgnoreCase(usernameFromPath); // Considera ignorar mayúsculas/minúsculas
        log.debug("Verificando isOwner: Usuario autenticado='{}', Username del recurso='{}', Coinciden: {}",
                userDetails.getUsername(), usernameFromPath, isMatch);
        return isMatch;
    }

    /**
     * Verifica si el usuario autenticado está asociado al empleado cuyo ID se proporciona.
     * Útil para permitir a un empleado acceder o modificar sus propios datos relacionados.
     *
     * @param authentication El objeto de autenticación actual.
     * @param idEmpleadoResource El ID del empleado que es dueño del recurso.
     * @return true si el usuario autenticado está vinculado al empleado, false en caso contrario.
     */
    public boolean isEmployeeSelf(Authentication authentication, Integer idEmpleadoResource) {
        if (idEmpleadoResource == null) {
            log.warn("Intento de verificación de isEmployeeSelf con idEmpleadoResource nulo.");
            return false;
        }
        if (!isAuthenticated(authentication)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.debug("Verificando isEmployeeSelf: Usuario autenticado='{}', ID Empleado del recurso={}", userDetails.getUsername(), idEmpleadoResource);

        return usuarioRepository.findByNombreUsuario(userDetails.getUsername())
                .map(usuarioAutenticado -> {
                    if (usuarioAutenticado.getEmpleado() != null) {
                        boolean isLinkedEmployee = usuarioAutenticado.getEmpleado().getIdEmpleado().equals(idEmpleadoResource);
                        log.trace("ID Empleado vinculado: {}, ID Empleado recurso: {}, Coinciden: {}",
                                usuarioAutenticado.getEmpleado().getIdEmpleado(), idEmpleadoResource, isLinkedEmployee);
                        return isLinkedEmployee;
                    } else {
                        log.trace("Usuario autenticado '{}' no tiene un empleado asociado.", userDetails.getUsername());
                        return false;
                    }
                })
                .orElseGet(() -> {
                    log.warn("Usuario autenticado '{}' no encontrado en la base de datos durante la verificación de isEmployeeSelf.", userDetails.getUsername());
                    return false;
                });
    }


    // --- Otras implementaciones sugeridas (si las necesitas) ---

    /**
     * Verifica si el usuario autenticado tiene un rol específico.
     * Alternativa a hasRole() si necesitas lógica adicional o logging.
     *
     * @param authentication El objeto de autenticación.
     * @param roleName El nombre del rol (sin el prefijo ROLE_).
     * @return true si el usuario tiene el rol, false en caso contrario.
     */
    public boolean hasSpecificRole(Authentication authentication, String roleName) {
        if (roleName == null || roleName.isBlank()) {
            log.warn("Intento de verificación de hasSpecificRole con roleName nulo o vacío.");
            return false;
        }
        if (!isAuthenticated(authentication)) {
            return false;
        }
        // Spring Security's hasRole() espera el nombre del rol sin el prefijo "ROLE_"
        // y las autoridades en UserDetails deben tener el prefijo "ROLE_".
        // Esta implementación manual es más explícita.
        String authorityToCheck = "ROLE_" + roleName.toUpperCase();
        boolean hasRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authorityToCheck));
        log.debug("Verificando hasSpecificRole: Usuario autenticado='{}', Rol buscado='{}', Resultado: {}",
                ((UserDetails) authentication.getPrincipal()).getUsername(), roleName, hasRole);
        return hasRole;
    }


    /**
     * Verifica si el usuario autenticado tiene un permiso (autoridad) específico.
     * Alternativa a hasAuthority() si necesitas lógica adicional o logging.
     *
     * @param authentication El objeto de autenticación.
     * @param authorityName El nombre completo de la autoridad/permiso.
     * @return true si el usuario tiene la autoridad, false en caso contrario.
     */
    public boolean hasSpecificAuthority(Authentication authentication, String authorityName) {
        if (authorityName == null || authorityName.isBlank()) {
            log.warn("Intento de verificación de hasSpecificAuthority con authorityName nulo o vacío.");
            return false;
        }
        if (!isAuthenticated(authentication)) {
            return false;
        }
        boolean hasAuthority = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authorityName.toUpperCase()));
        log.debug("Verificando hasSpecificAuthority: Usuario autenticado='{}', Autoridad buscada='{}', Resultado: {}",
                ((UserDetails) authentication.getPrincipal()).getUsername(), authorityName, hasAuthority);
        return hasAuthority;
    }


    /**
     * Método helper para verificar si la autenticación es válida y el principal es UserDetails.
     */
    private boolean isAuthenticated(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.trace("Autenticación nula o no autenticada.");
            return false;
        }
        if (!(authentication.getPrincipal() instanceof UserDetails)) {
            log.warn("El principal de la autenticación no es una instancia de UserDetails: {}", authentication.getPrincipal().getClass().getName());
            return false;
        }
        return true;
    }
}