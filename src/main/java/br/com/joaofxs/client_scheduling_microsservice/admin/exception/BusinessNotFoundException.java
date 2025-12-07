package br.com.joaofxs.client_scheduling_microsservice.admin.exception;

public class BusinessNotFoundException extends RuntimeException {
    public BusinessNotFoundException(String message) {
        super(message);
    }
}
