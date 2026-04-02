package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
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
                .service(businessDTO.service())
                .build();

    }

    public BusinessDTO convertBusinessToDTO(Business business){
        return new BusinessDTO(business.getName(), business.getDescription(), business.getPhone(), business.getEmail(), business.getService(), business.getWebsite(), business.getAddress(), business.getCep());
    }
}
