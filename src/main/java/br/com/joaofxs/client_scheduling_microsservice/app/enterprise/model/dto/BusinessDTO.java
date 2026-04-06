package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto;

public record BusinessDTO(
         String name,
         String description,
         String phone,
         String email,
         String website,
         String service,
         String address,
         String number,
         String neighborhood,
         String state,
         String city,
         String UF,
         String cep,
         String filePublicUrl
) {
}

