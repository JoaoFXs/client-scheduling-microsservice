package br.com.joaofxs.client_scheduling_microsservice.core.exception;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException() {
        super("Token inválido ou expirado");
    }
}
