package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.impl;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.exception.BusinessNotFoundException;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository.BusinessRepository;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.BusinessService;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service.FileService;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.specs.EnterpriseSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools.distinctByKey;
import static br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools.getNullPropertyNames;
import static java.util.Arrays.stream;

@Service
@Slf4j
@RequiredArgsConstructor // Usa injeção de dependência via construtor (melhor prática)
public class BusinessServiceImpl implements BusinessService {

    private final BusinessTools businessMapper;
    private final BusinessRepository businessRepository;
    private final FileService fileService;
    @Override
    @Transactional
    public String registerBusiness(BusinessDTO businessDTO, MultipartFile file) throws IOException {
        log.info("Iniciando registro para o negócio com e-mail: {}", businessDTO.email());
        String publicUrl = (!file.isEmpty()) ? fileService.uploadFile(file) : "";
        Business business = businessMapper.convertDTOtoBusiness(businessDTO, publicUrl);
        Business savedBusiness = businessRepository.save(business);
        log.info("Negócio '{}' registrado com sucesso com o ID: {}", savedBusiness.getName(), savedBusiness.getId());
        // Retorna o DTO do negócio salvo, que é mais útil para o cliente.
        return savedBusiness.getId().toString();
    }

    @Override
    public Page<BusinessDTO> getAllBusiness(Pageable page, String name, List<String> category, List<String> uf) {
        log.info("Buscando todos os negócios cadastrados.");
        Page<Business> businesses = businessRepository.findAll(Specification
                .where(EnterpriseSpec.hasName(name))
                .and(EnterpriseSpec.hasValues(category, "service"))
                .and(EnterpriseSpec.hasValues(uf, "uf")), page);
        return businessMapper.convertPageableBusinessToPageDTO(businesses);
    }


    @Override
    public List<BusinessDTO> getAllBusinessByEmail(String email){
        log.info("Buscando todos os negócios cadastrados no email.");
        List<Business> businesses = businessRepository.findByEmail(email);
        if (businesses.isEmpty()){
            throw new BusinessNotFoundException("Nenhum negócio encontrado para o email " + email);
        }
        return businesses.stream().map(businessMapper::convertBusinessToDTO).toList();
    }

    @Override
    public Business findByBusinessId(Long id) {
        log.info("Buscando negócio pelo ID: {}", id);
        return businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException("Negócio com ID " + id + " não encontrado."));
    }

    @Override
    public Set<EnterpriseFilterProjection> getFilters() {
        return businessRepository.getFilters().stream()
                .filter(distinctByKey(p -> Arrays.asList(p.getService(), p.getUf())))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteBusinessById(Long id) {   
        log.info("Tentando deletar negócio com ID: {}", id);
        // Tratamento de erro: Verifica se o negócio existe antes de tentar deletar.
        if (!businessRepository.existsById(id)) {
            log.warn("Tentativa de deletar negócio com ID inexistente: {}", id);
            throw new BusinessNotFoundException("Negócio com ID " + id + " não encontrado para exclusão.");
        }
        businessRepository.deleteById(id);
        log.info("Negócio com ID {} deletado com sucesso.", id);
    }
    @Transactional
    @Override
    public void changeBusiness(Long businessId, BusinessDTO businessDto, MultipartFile file) throws IOException {
        Business findBusiness = businessRepository
                        .findById(businessId)
                        .orElseThrow(
                                RuntimeException::new
                        );

        // Copia apenas o que não for nulo do DTO para a Entidade
        BeanUtils.copyProperties(businessDto, findBusiness, getNullPropertyNames(businessDto));

        // Lógica do ficheiro (especializada)
        if (file != null && !file.isEmpty()) {
            findBusiness.setFilePublicUrl(fileService.uploadFile(file));
        }
    }

}
