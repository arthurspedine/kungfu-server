package com.spedine.server.infrastructure.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionData> entityNotFoundException(EntityNotFoundException e) {
        logger.error("Entidade não encontrada: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionData(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldExceptionData>> argumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getFieldErrors();
        logger.error("Erro de validação de campo: {}", errors);
        return ResponseEntity.badRequest().body(errors.stream().map(FieldExceptionData::new).toList());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionData> badCredentialsException(BadCredentialsException e) {
        logger.error("Erro de credenciais inválidas: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionData("Credenciais inválidas!"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionData> authenticationException(AuthenticationException e) {
        logger.error("Erro de autenticação inválida: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionData( "Autenticação inválida! Cheque os campos e tente novamente!"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionData> accessDeniedException(AccessDeniedException e) {
        logger.error("Erro de acesso negado: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionData("Acesso negado!"));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionData> validationException(ValidationException e) {
        logger.error("Erro de validação: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionData(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionData> runtimeException(RuntimeException e) {
        logger.error("Erro de execução: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionData(e.getLocalizedMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionData> exception(Exception ex) {
        logger.error("Erro geral: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionData(ex.getLocalizedMessage()));
    }

}
