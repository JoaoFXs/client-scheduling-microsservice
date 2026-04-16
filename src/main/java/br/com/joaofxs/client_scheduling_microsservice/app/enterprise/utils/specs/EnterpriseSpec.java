package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.specs;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseSpec {
    public static Specification<Business> filter(String name, List<String> services) {
        return (root, query, cb) -> {
            List<Predicate> filtrosPrincipais = new ArrayList<>();

            // 1. Filtro por Nome (AND)
            if (name != null && !name.trim().isEmpty()) {
                filtrosPrincipais.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            // 2. Filtro por Lista de Serviços (OR entre eles)
            if (services != null && !services.isEmpty()) {
                List<Predicate> filtrosServico = new ArrayList<>();

                services.forEach(s -> {
                    if (s != null && !s.trim().isEmpty()) {
                        filtrosServico.add(cb.like(cb.lower(root.get("service")), "%" + s.toLowerCase() + "%"));
                    }
                });

                // Se encontrou termos de serviço, agrupa-os com OR
                if (!filtrosServico.isEmpty()) {
                    Predicate orServicos = cb.or(filtrosServico.toArray(new Predicate[0]));
                    filtrosPrincipais.add(orServicos);
                }
            }

            // Junta o Nome com o Bloco de Serviços usando AND
            return cb.and(filtrosPrincipais.toArray(new Predicate[0]));
        };
    }
}