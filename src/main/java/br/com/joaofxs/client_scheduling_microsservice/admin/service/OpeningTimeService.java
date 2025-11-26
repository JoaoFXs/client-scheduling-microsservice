package br.com.joaofxs.client_scheduling_microsservice.admin.service;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.OpeningTimeDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.OpeningTime;
import br.com.joaofxs.client_scheduling_microsservice.admin.repository.business.OpeningTimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OpeningTimeService {

    @Autowired
    private OpeningTimeRepository openingTimeRepository;


    public List<OpeningTime> registerOpeningTime(List<OpeningTimeDTO>  openingTimeDTO){
            return null;
    }
}
