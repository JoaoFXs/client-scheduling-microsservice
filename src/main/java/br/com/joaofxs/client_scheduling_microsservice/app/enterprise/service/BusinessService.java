package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.service;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.exception.BusinessNotFoundException;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository.BusinessRepository;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business.BusinessTools;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor // Usa injeção de dependência via construtor (melhor prática)
public class BusinessService {

    private final BusinessTools businessMapper;
    private final BusinessRepository businessRepository;

    @Transactional
    public String registerBusiness(BusinessDTO businessDTO, MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString().concat(this.getExtension(file.getOriginalFilename()));
        log.info("Iniciando registro para o negócio com e-mail: {}", businessDTO.email());
        Bucket bucket = StorageClient.getInstance().bucket();

        // Faz o upload do arquivo
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());

        // Retorna a URL pública (assinada ou direta dependendo das regras do bucket)
        // Gera uma URL assinada que expira em 10 anos (ou o tempo que você desejar)
        // Isso já resolve o problema do Token e das permissões de leitura
        String publicUrl = blob.signUrl(3650, TimeUnit.DAYS).toString();

        Business business = businessMapper.convertDTOtoBusiness(businessDTO, publicUrl);
        Business savedBusiness = businessRepository.save(business);

        log.info("Negócio '{}' registrado com sucesso com o ID: {}", savedBusiness.getName(), savedBusiness.getId());
        // Retorna o DTO do negócio salvo, que é mais útil para o cliente.
        return savedBusiness.getId().toString();
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
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
