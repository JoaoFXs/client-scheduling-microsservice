package br.com.joaofxs.client_scheduling_microsservice.admin.service;

import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.utils.business.BusinessTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BusinessService {

    @Autowired
    private BusinessTools businessTools;

    public Business registerBusiness(BusinessDTO businessDTO){

        return null;
    }



}
