package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.controller;


import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.BusinessService;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.OpeningTimeService;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/enterprise")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private OpeningTimeService openingTimeService;

    @Autowired
    private BusinessTools businessTools;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<String> registerBusiness(@RequestPart("request") BusinessDTO business, @RequestPart("file") MultipartFile file) throws IOException {
        String businessId = businessService.registerBusiness(business, file);
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

    @GetMapping("filters")
    private ResponseEntity<?> getFilters(@PageableDefault(size = 10, page = 0) Pageable pageable){
        Page<EnterpriseFilterProjection> listServices = businessService.getFilters(pageable);
        return ResponseEntity.ok(listServices);

    }


}
