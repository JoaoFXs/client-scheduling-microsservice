package br.com.joaofxs.client_scheduling_microsservice.core.repository;

import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    Optional<ResetToken> findByToken(String token);

    void deleteAllByExpiresAtBefore(LocalDateTime localDateTime);
}
