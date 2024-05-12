package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
/*
    Essa classe intercepta erros na camada do controlador
    e customiza a resposta e status de erro. 
*/
@ControllerAdvice
public class ResourceExceptionHandler {
    
    //Customiza erros do tipo ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        // mensagem de erro 
        error.setError("Resource Not Found");
        // descrição do erro
        error.setMessage(e.getMessage());
        error.setPath(request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    } 
    
}
