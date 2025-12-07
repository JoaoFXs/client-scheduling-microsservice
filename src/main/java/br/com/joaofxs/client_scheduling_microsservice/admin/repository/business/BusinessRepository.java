package br.com.joaofxs.client_scheduling_microsservice.admin.repository.business;


import br.com.joaofxs.client_scheduling_microsservice.admin.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    List<Business> findByEmail(String email);
}
