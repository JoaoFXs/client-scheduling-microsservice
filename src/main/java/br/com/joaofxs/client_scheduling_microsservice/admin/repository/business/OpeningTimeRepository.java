package br.com.joaofxs.client_scheduling_microsservice.admin.repository.business;

import br.com.joaofxs.client_scheduling_microsservice.admin.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long>{

}