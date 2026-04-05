package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.exception;

public class BusinessAlreadyExistsException extends RuntimeException {
    public BusinessAlreadyExistsException(String message) {
        super(message);
    }
}