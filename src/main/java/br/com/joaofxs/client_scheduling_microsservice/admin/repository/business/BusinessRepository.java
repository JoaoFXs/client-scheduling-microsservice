package br.com.joaofxs.client_scheduling_microsservice.admin.repository.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
