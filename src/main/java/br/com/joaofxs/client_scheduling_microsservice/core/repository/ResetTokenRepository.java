package br.com.joaofxs.client_scheduling_microsservice.core.repository;

import br.com.joaofxs.client_scheduling_microsservice.core.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {


}
