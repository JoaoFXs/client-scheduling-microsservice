package br.com.joaofxs.client_scheduling_microsservice.admin.service;

import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.repository.business.BusinessRepository;
import br.com.joaofxs.client_scheduling_microsservice.admin.utils.business.BusinessTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class BusinessService {

    @Autowired
    private BusinessTools businessTools;

    @Autowired
    private BusinessRepository businessRepository;

    @Transactional
    public Business registerBusiness(BusinessDTO businessDTO){

        Business business = businessTools.convertDTOtoBusiness(businessDTO);

        return businessRepository.save(business);
    }

}
