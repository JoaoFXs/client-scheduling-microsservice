package br.com.joaofxs.client_scheduling_microsservice.core.config;

import br.com.joaofxs.client_scheduling_microsservice.core.model.LastPassword;
import br.com.joaofxs.client_scheduling_microsservice.core.repository.OldPasswordsCleanUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OldPasswordsCleanUp {

    private final OldPasswordsCleanUpRepository repository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void clearOldPasswords(){
        log.info("Iniciando a limpeza automática de senhas antigas...");
        repository.deleteAllByExpiredAt(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
        log.info("Limpeza de senhas antigas finalizada com sucesso.");
    }


}
