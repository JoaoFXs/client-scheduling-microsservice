package br.com.joaofxs.client_scheduling_microsservice.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record BusinessDTO(
         String name,
         String description,
         String phone,
         String email,
         String website,
         String address,
         Long cep
) {
}
