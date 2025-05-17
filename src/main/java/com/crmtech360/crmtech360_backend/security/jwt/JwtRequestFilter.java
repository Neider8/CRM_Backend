package com.crmtech360.crmtech360_backend.security.jwt;

// Asegúrate que la ruta a tu UserDetailsServiceImpl sea correcta
import com.crmtech360.crmtech360_backend.service.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException; // Import correcto para SignatureException de JJWT

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl; // Correcto, no UserDetailsService directamente

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.getUsernameFromToken(jwt);
                log.debug("Token JWT válido para usuario: {} en URI: {}", username, request.getRequestURI());

                // Solo configurar la autenticación si no existe ya una en el contexto
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    // Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Las credenciales (contraseña) no son necesarias aquí post-autenticación JWT
                            userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Usuario '{}' autenticado correctamente vía JWT. Contexto de seguridad establecido para URI: {}", username, request.getRequestURI());
                } else if (username == null) {
                    log.warn("El nombre de usuario extraído del token JWT es nulo para URI: {}", request.getRequestURI());
                }
            } else {
                if (jwt == null) {
                    log.trace("No se encontró token JWT en la solicitud para URI: {}", request.getRequestURI());
                } else {
                    // La validación del token falló, jwtUtil.validateToken() ya habrá logueado la causa específica.
                    log.warn("Token JWT inválido o expirado detectado para URI: {}", request.getRequestURI());
                }
            }
        } catch (ExpiredJwtException e) {
            log.warn("Token JWT ha expirado: {}. URI: {}", e.getMessage(), request.getRequestURI());
            // La respuesta de error será manejada por JwtAuthenticationEntryPoint.
            // No es necesario enviar una respuesta desde aquí, solo evitar que la autenticación se establezca.
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.warn("Error de token JWT (firma, malformado, no soportado, ilegal): {}. URI: {}", e.getMessage(), request.getRequestURI());
        } catch (UsernameNotFoundException e) {
            log.warn("Usuario no encontrado para el token JWT: {}. URI: {}", e.getMessage(), request.getRequestURI());
        } catch (Exception e) {
            log.error("No se pudo establecer la autenticación del usuario en el contexto de seguridad para URI: {}. Error: {}", request.getRequestURI(), e.getMessage(), e);
        }

        filterChain.doFilter(request, response); // Continuar con la cadena de filtros
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        log.trace("Encabezado Authorization no encontrado o no es de tipo Bearer para URI: {}", request.getRequestURI());
        return null;
    }
}