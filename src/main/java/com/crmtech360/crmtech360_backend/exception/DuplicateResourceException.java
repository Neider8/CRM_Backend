package com.crmtech360.crmtech360_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict es apropiado
public class DuplicateResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String message) { // Espera 1 argumento
        super(message);
    }

    // Este constructor espera 3 argumentos
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Ya existe un recurso %s con %s : '%s'", resourceName, fieldName, fieldValue));
    }
}