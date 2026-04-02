package br.com.joaofxs.client_scheduling_microsservice.core.dto;

import br.com.joaofxs.client_scheduling_microsservice.core.model.enums.TypeUser;

public record UserDTO(String username, String email, String password, String cpf, String phone, boolean isCompany) {
}
