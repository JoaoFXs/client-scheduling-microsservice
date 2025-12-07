package br.com.joaofxs.client_scheduling_microsservice.admin.service;


import br.com.joaofxs.client_scheduling_microsservice.admin.dto.OpeningTimeDTO;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.admin.model.OpeningTime;
import br.com.joaofxs.client_scheduling_microsservice.admin.repository.business.OpeningTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor // Usa injeção de dependência via construtor, que é a melhor prática.
public class OpeningTimeService {

    private final OpeningTimeRepository openingTimeRepository;
    private final BusinessService businessService;

    /**
     * Atualiza os horários de funcionamento de um negócio com base em uma lista de DTOs.
     * Apenas os dias da semana presentes na lista de DTOs serão atualizados.
     */
    public List<OpeningTime> updateOpeningTimes(Long businessId, List<OpeningTimeDTO> openingTimeDTOs) {
        log.info("Iniciando atualização de horários de funcionamento para o negócio com ID: {}", businessId);

        // 1. Busca a entidade de negócio.
        Business business = businessService.findByBusinessId(businessId);


        // 2. Para otimizar a busca, converte a lista de DTOs em um Map onde a chave é o DayOfWeek.
        Map<DayOfWeek, OpeningTimeDTO> dtoMap = openingTimeDTOs.stream()
                .collect(Collectors.toMap(OpeningTimeDTO::dayOfWeek, dto -> dto, (dto1, dto2) -> dto2));

        // 3. Itera sobre os horários de funcionamento existentes do negócio.
        business.getTimes().forEach(existingTime -> {
            // Verifica se existe um DTO para o dia da semana atual.
            OpeningTimeDTO dto = dtoMap.get(existingTime.getDayOfWeek());
            if (dto != null) {
                // Se existir, atualiza a entidade existente com os dados do DTO.
                log.debug("Atualizando horário para o dia: {}", existingTime.getDayOfWeek());
                existingTime.setOpen(dto.open());
                existingTime.setOpeningHours(dto.openingHours());
                existingTime.setClosedHours(dto.closedHours());
            }
        });

        // 4. Salva todas as entidades atualizadas no banco de dados de uma só vez.
        log.info("Salvando horários de funcionamento atualizados para o negócio: {}", business.getName());
        return openingTimeRepository.saveAll(business.getTimes());
    }


    public List<OpeningTimeDTO> getTimesFromBusiness(Long businessId){

        Business business = businessService.findByBusinessId(businessId);

        return business.getTimes().stream().map(
                times ->{
                    return new OpeningTimeDTO(times.getDayOfWeek(), times.getOpeningHours(), times.getClosedHours(), times.isOpen());
                }
         ).toList();
    }
}
