package br.com.joaofxs.client_scheduling_microsservice.core.dto;

public record AuthRequest(
        String email,
        String password,
        String cpf,
        String phone,
        String username
        ) {}