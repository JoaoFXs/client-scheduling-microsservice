package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BusinessTools {

    public Business convertDTOtoBusiness(BusinessDTO businessDTO, String filePublicUrl) {
        return Business.builder()
                .name(businessDTO.name())
                .description(businessDTO.description())
                .phone(businessDTO.phone())
                .email(businessDTO.email())
                .website(businessDTO.website())
                .service(businessDTO.service())
                .address(businessDTO.address())
                .number(businessDTO.number())
                .neighborhood(businessDTO.neighborhood())
                .state(businessDTO.state())
                .city(businessDTO.city())
                .uf(businessDTO.UF())
                .cep(businessDTO.cep())
                .filePublicUrl(filePublicUrl)
                .build();
    }

    public BusinessDTO convertBusinessToDTO(Business business){

        return new BusinessDTO(
                business.getName(),
                business.getDescription(),
                business.getPhone(),
                business.getEmail(),
                business.getWebsite(),
                business.getService(),
                business.getAddress(),
                business.getNumber(),
                business.getNeighborhood(),
                business.getState(),
                business.getCity(),
                business.getUf(),
                business.getCep(),
                business.getFilePublicUrl()
        );
    }

    public Page<BusinessDTO> convertPageableBusinessToPageDTO(Page<Business> businesses){
        return businesses.map(this::convertBusinessToDTO);
    }
}
