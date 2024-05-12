package com.devsuperior.dscatalog.services.exceptions;

/*
    Trata exceções de recursos não encontrados 
*/
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
