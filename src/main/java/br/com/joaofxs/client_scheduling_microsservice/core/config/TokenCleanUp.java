package br.com.joaofxs.client_scheduling_microsservice.core.config;


import br.com.joaofxs.client_scheduling_microsservice.core.repository.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class TokenCleanUp {

    private final ResetTokenRepository resetTokenRepository;


    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void clearExpiredTokens(){
        log.info("Iniciando a limpeza automática de tokens expirados...");
        resetTokenRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        log.info("Limpeza de tokens finalizada com sucesso.");
    }

}
