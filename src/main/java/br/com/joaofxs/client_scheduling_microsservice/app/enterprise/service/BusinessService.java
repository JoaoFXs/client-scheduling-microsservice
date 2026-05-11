package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BusinessService {

    String registerBusiness(BusinessDTO businessDTO, MultipartFile file) throws IOException;

    Page<BusinessDTO> getAllBusiness(Pageable page, String name, List<String> category);

    List<BusinessDTO> getAllBusinessByEmail(String email);

    Business findByBusinessId(Long id);

    Set<EnterpriseFilterProjection> getFilters();

    void deleteBusinessById(Long id);

    void changeBusiness(Long businessId, BusinessDTO business, MultipartFile file) throws IOException;
}
