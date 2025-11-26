package br.com.joaofxs.client_scheduling_microsservice.admin.utils.business;

import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessTools {

    public Business convertDTOtoBusiness(BusinessDTO businessDTO) {
        return Business.builder()
                .cep(businessDTO.cep())
                .phone(businessDTO.phone())
                .email(businessDTO.email())
                .address(businessDTO.address())
                .name(businessDTO.name())
                .description(businessDTO.description())
                .website(businessDTO.website())
                .build();

    }
}
