package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.repository;


import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.interfaces.EnterpriseFilterProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByEmail(String email);

    @Query("SELECT DISTINCT b.service as service, b.neighborhood as neighBorhood, b.city as city, b.state as state, b.cep as cep FROM Business b")
    Page<EnterpriseFilterProjection> getFilters(Pageable pageable);

}
