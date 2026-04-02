package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository;


import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByEmail(String email);

    @Query("SELECT DISTINCT b.service FROM Business b")
    List<String> getAllServices();
}
