package br.com.joaofxs.client_scheduling_microsservice.admin.controller.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.dto.OpeningTimeDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.BusinessService;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.OpeningTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private OpeningTimeService openingTimeService;

    @PostMapping
    private ResponseEntity<Business> registerBusiness(@RequestBody BusinessDTO business) {
        return ResponseEntity.ok(businessService.registerBusiness(business));
    }

    @PostMapping("{id}/hours")
    private ResponseEntity<?> registerHoursOfBusiness(@PathVariable Long id, @RequestBody List<OpeningTimeDTO> openingTimeDTO){
        openingTimeService.registerOpeningTime(openingTimeDTO);
        return null;
    }



}
