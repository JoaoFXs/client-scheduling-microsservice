package br.com.joaofxs.client_scheduling_microsservice.admin.controller.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.dto.OpeningTimeDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.BusinessService;
import br.com.joaofxs.client_scheduling_microsservice.admin.service.OpeningTimeService;
import br.com.joaofxs.client_scheduling_microsservice.admin.utils.business.BusinessTools;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private BusinessTools businessTools;

    @PostMapping
    private ResponseEntity<String> registerBusiness(@RequestBody BusinessDTO business) {
        String businessId = businessService.registerBusiness(business);
        return new ResponseEntity<>(businessId, HttpStatus.CREATED);
    }


    @GetMapping
    private ResponseEntity<?> getAllBusiness(@RequestParam(required = false) String email){
        List<BusinessDTO> businessDTO;

        if (email != null && !email.isEmpty()) {
            // Se o email foi fornecido, chama a lógica de filtragem
            businessDTO = businessService.getAllBusinessByEmail(email);
        } else {
            // Se o email for null (não foi fornecido), chama todos
            businessDTO = businessService.getAllBusiness();
        }

        return ResponseEntity.ok(businessDTO);
    }



    @GetMapping("{id}")
    private ResponseEntity<?> getBusinessById(@PathVariable Long id){
        Business business = businessService.findByBusinessId(id);
        BusinessDTO businessDTO =  businessTools.convertBusinessToDTO(business);
        return ResponseEntity.ok(businessDTO);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<Object> deleteBusinessById(@PathVariable Long id) {
        businessService.deleteBusinessById(id);
        return ResponseEntity.noContent().build();

    }




}
