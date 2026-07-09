package com.sanosysalvos.usuarios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta registrar un usuario con un correo que ya existe.
 * Antes esto llegaba como una DataIntegrityViolationException sin manejar y
 * el GlobalExceptionHandler la convertía en un 500 genérico. Con esta excepción
 * específica el cliente recibe un 409 con un mensaje claro y accionable.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class EmailDuplicadoException extends RuntimeException {
    public EmailDuplicadoException(String message) {
        super(message);
    }
}
