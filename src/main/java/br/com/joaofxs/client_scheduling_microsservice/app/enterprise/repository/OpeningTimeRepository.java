package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.OpeningTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long>{

    OpeningTime findByDayOfWeek(DayOfWeek dayOfWeek);
}