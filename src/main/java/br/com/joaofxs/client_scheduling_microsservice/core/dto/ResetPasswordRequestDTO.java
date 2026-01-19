package br.com.joaofxs.client_scheduling_microsservice.core.dto;

public record ResetPasswordRequestDTO (
        String token,
        String newPassword

){
}
