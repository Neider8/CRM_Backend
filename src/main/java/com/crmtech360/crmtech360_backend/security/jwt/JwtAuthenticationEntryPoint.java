package com.crmtech360.crmtech360_backend.security.jwt;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature; // Para configurar la serialización de fechas
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
// import java.util.Collections; // Descomentar si se usa el campo 'details'

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // Configurar para que las fechas se serialicen como strings ISO-8601 en lugar de timestamps numéricos
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.error("Error de autenticación no autorizado: {}. Causa: {}. Ruta: {}",
                authException.getMessage(),
                authException.getCause() != null ? authException.getCause().getMessage() : "N/A",
                request.getRequestURI());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "No Autorizado", // Mensaje de error más estándar para el campo 'error'
                "Se requiere autenticación completa para acceder a este recurso. Por favor, inicie sesión o proporcione un token JWT válido.",
                request.getRequestURI()
                // Si quieres enviar más detalles específicos (con precaución):
                // , Collections.singletonList(authException.getMessage())
        );

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}