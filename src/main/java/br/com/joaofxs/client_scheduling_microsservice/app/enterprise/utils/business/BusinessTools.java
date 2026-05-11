package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.business;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.dto.BusinessDTO;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class BusinessTools {

    public Business convertDTOtoBusiness(BusinessDTO businessDTO, String filePublicUrl) {
        return Business.builder()
                .name(businessDTO.name())
                .description(businessDTO.description())
                .phone(businessDTO.phone())
                .email(businessDTO.email())
                .website(businessDTO.website())
                .service(businessDTO.service())
                .address(businessDTO.address())
                .number(businessDTO.number())
                .neighborhood(businessDTO.neighborhood())
                .state(businessDTO.state())
                .city(businessDTO.city())
                .uf(businessDTO.UF())
                .cep(businessDTO.cep())
                .filePublicUrl(filePublicUrl)
                .build();
    }

    public BusinessDTO convertBusinessToDTO(Business business){

        return new BusinessDTO(
                business.getName(),
                business.getDescription(),
                business.getPhone(),
                business.getEmail(),
                business.getWebsite(),
                business.getService(),
                business.getAddress(),
                business.getNumber(),
                business.getNeighborhood(),
                business.getState(),
                business.getCity(),
                business.getUf(),
                business.getCep(),
                business.getFilePublicUrl()
        );
    }

    public Page<BusinessDTO> convertPageableBusinessToPageDTO(Page<Business> businesses){
        return businesses.map(this::convertBusinessToDTO);
    }

    /**
     * Cria um filtro (Predicate) para ser usado em Streams que permite filtrar elementos
     * duplicados com base em uma propriedade específica do objeto.
     * * Exemplo de uso: list.stream().filter(distinctByKey(Enterprise::getService))
     * * @param keyExtractor Função que extrai a chave que será usada para comparar a duplicidade.
     * @param <T> O tipo do objeto que está a ser processado no Stream.
     * @return Um Predicate que retorna 'true' apenas para a primeira ocorrência de cada chave.
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    // Método utilitário para encontrar campos nulos no DTO
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }
}
