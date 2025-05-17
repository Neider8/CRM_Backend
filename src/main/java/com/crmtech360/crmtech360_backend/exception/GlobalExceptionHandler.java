package com.crmtech360.crmtech360_backend.exception;

import com.crmtech360.crmtech360_backend.dto.ApiErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // --- Manejadores para Excepciones Personalizadas ---

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        log.warn("Recurso duplicado: {}", ex.getMessage());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        log.warn("Solicitud incorrecta: {}", ex.getMessage());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Object> handleUnauthorizedOperationException(
            UnauthorizedOperationException ex, WebRequest request) {
        log.warn("Operación no autorizada: {}", ex.getMessage());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // --- Manejadores para Excepciones Comunes de Spring ---

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Error de validación de argumentos: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validación. Por favor, revise los campos.",
                request.getDescription(false).replace("uri=", ""),
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.warn("Tipo de argumento de método no coincide: {}", ex.getMessage());
        String message = String.format("El parámetro '%s' debe ser de tipo '%s' pero se recibió '%s'.",
                ex.getName(), ex.getRequiredType().getSimpleName(), ex.getValue());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Parámetro de solicitud faltante: {}", ex.getParameterName());
        String message = "El parámetro requerido '" + ex.getParameterName() + "' del tipo '" + ex.getParameterType() + "' no está presente.";
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("Método HTTP no soportado: {} para la ruta {}", ex.getMethod(), request.getDescription(false));
        StringBuilder builder = new StringBuilder();
        builder.append("El método ");
        builder.append(ex.getMethod());
        builder.append(" no es soportado para esta solicitud. Los métodos soportados son ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));

        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                builder.toString(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("No se encontró manejador para la ruta {}: {}", ex.getRequestURL(), ex.getMessage());
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "La ruta solicitada no fue encontrada en el servidor: " + ex.getRequestURL(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        log.error("Error de integridad de datos: {}", ex.getMostSpecificCause().getMessage());
        // Mensaje genérico para no exponer detalles de la BD
        String userMessage = "Error en la operación de base de datos debido a una violación de integridad (ej. clave duplicada o restricción de clave foránea).";
        // Podrías intentar analizar ex.getMostSpecificCause().getMessage() para dar mensajes más específicos si es seguro.
        // Por ejemplo, si contiene "violates unique constraint" o "violates foreign key constraint".
        if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("unique constraint") ||
                ex.getMostSpecificCause().getMessage().toLowerCase().contains("unique_")) {
            userMessage = "No se pudo completar la operación: Ya existe un registro con alguno de los valores únicos proporcionados.";
        } else if (ex.getMostSpecificCause().getMessage().toLowerCase().contains("foreign key constraint") ||
                ex.getMostSpecificCause().getMessage().toLowerCase().contains("violates foreign key")) {
            userMessage = "No se pudo completar la operación: Uno de los recursos relacionados no existe o no se puede modificar/eliminar debido a dependencias.";
        }

        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.CONFLICT.value(), // O BAD_REQUEST (400) dependiendo del caso
                HttpStatus.CONFLICT.getReasonPhrase(),
                userMessage,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    // --- Manejador Genérico para Otras Excepciones ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
        log.error("Error inesperado en el servidor: {}", ex.getMessage(), ex); // Loguear el stack trace completo
        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error inesperado en el servidor. Por favor, intente más tarde.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}