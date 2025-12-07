package br.com.joaofxs.client_scheduling_microsservice.admin.controller.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.OpeningTimeDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.BusinessService;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.OpeningTimeService;
import br.com.joaofxs.client_scheduling_microsservice.admin.utils.business.BusinessTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/business/hours")
public class OpeningTimeController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private OpeningTimeService openingTimeService;


    @PutMapping("{id}")
    private ResponseEntity<Object> updateHoursOfBusinessById(@PathVariable Long id, @RequestBody List<OpeningTimeDTO> openingTimeDTO){
        openingTimeService.updateOpeningTimes(id, openingTimeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}")
    private ResponseEntity<?> getHoursOfBusinessById(@PathVariable Long id){
        List<OpeningTimeDTO> times = openingTimeService.getTimesFromBusiness(id);
        return ResponseEntity.ok(times);
    }



}
