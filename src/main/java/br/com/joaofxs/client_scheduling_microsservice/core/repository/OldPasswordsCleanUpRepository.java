package br.com.joaofxs.client_scheduling_microsservice.core.repository;

import br.com.joaofxs.client_scheduling_microsservice.core.model.LastPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OldPasswordsCleanUpRepository extends JpaRepository<LastPassword, Long> {
    void deleteAllByExpiredAt(LocalDateTime localDateTime);
}
