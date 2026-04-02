package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.exception;

public class BusinessNotFoundException extends RuntimeException {
    public BusinessNotFoundException(String message) {
        super(message);
    }
}
