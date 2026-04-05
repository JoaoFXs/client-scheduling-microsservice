package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.exception.BusinessNotFoundException;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository.BusinessRepository;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // Usa injeção de dependência via construtor (melhor prática)
public class BusinessService {

    private final BusinessTools businessMapper;
    private final BusinessRepository businessRepository;

    @Transactional
    public String registerBusiness(BusinessDTO businessDTO) {
        log.info("Iniciando registro para o negócio com e-mail: {}", businessDTO.email());


        Business business = businessMapper.convertDTOtoBusiness(businessDTO);
        Business savedBusiness = businessRepository.save(business);

        log.info("Negócio '{}' registrado com sucesso com o ID: {}", savedBusiness.getName(), savedBusiness.getId());
        // Retorna o DTO do negócio salvo, que é mais útil para o cliente.
        return savedBusiness.getId().toString();
    }

    public List<BusinessDTO> getAllBusiness() {
        log.info("Buscando todos os negócios cadastrados.");
        List<Business> businesses = businessRepository.findAll();
        return businesses.stream().map(businessMapper::convertBusinessToDTO).toList();
    }

    public List<BusinessDTO> getAllBusinessByEmail(String email){
        log.info("Buscando todos os negócios cadastrados no email.");
        List<Business> businesses = businessRepository.findByEmail(email);
        if (businesses.isEmpty()){
            throw new BusinessNotFoundException("Nenhum negócio encontrado para o email " + email);
        }
        return businesses.stream().map(businessMapper::convertBusinessToDTO).toList();
    }

    public Business findByBusinessId(Long id) {
        log.info("Buscando negócio pelo ID: {}", id);
        return businessRepository.findById(id)
                .orElseThrow(() -> new BusinessNotFoundException("Negócio com ID " + id + " não encontrado."));
    }

    public Page<EnterpriseFilterProjection> getFilters(Pageable pageable){
        return businessRepository.getFilters(pageable);
    }
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
}
