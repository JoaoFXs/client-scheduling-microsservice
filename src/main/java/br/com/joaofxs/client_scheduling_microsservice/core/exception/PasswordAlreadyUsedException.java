package br.com.joaofxs.client_scheduling_microsservice.core.exception;

public class PasswordAlreadyUsedException extends RuntimeException {
    public PasswordAlreadyUsedException() {
        super("Esta senha já foi utilizada recentemente. Por favor, escolha uma senha diferente.");
    }
}
