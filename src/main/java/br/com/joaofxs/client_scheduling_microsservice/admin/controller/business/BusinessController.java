package br.com.joaofxs.client_scheduling_microsservice.admin.controller.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @PostMapping
    private ResponseEntity<?> registerBusiness(@RequestBody BusinessDTO business) {
        businessService.registerBusiness(business);
        return null;
    }



}
