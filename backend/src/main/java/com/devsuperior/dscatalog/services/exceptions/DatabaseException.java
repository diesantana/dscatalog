package com.devsuperior.dscatalog.services.exceptions;

/*
    Trata exceções relacionadas ao banco de dados
*/
public class DatabaseException extends RuntimeException{
    public DatabaseException(String message) {
        super(message);
    }
}
