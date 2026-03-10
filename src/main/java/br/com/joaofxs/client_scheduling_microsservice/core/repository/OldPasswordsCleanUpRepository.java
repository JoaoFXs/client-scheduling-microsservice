package br.com.joaofxs.client_scheduling_microsservice.core.repository;

import br.com.joaofxs.client_scheduling_microsservice.core.model.LastPassword;
import br.com.joaofxs.client_scheduling_microsservice.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OldPasswordsCleanUpRepository extends JpaRepository<LastPassword, Long> {
    void deleteAllByExpiredAt(LocalDateTime localDateTime);
    List<LastPassword> findAllByUser(User user);

}
