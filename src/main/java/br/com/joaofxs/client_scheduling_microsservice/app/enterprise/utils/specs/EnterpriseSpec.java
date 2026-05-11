package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.specs;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseSpec {
    public static Specification<Business> filter(String name, List<String> services, List<String> uf) {
        return (root, query, cb) -> {
            System.out.println("Filtrando -> name: " + name + " | services: " + services + " | uf: " + uf);

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

            // 2. Filtro por Lista de UF (OR entre eles)
            if (uf != null && !uf.isEmpty()) {
                List<Predicate> filtrosUf = new ArrayList<>();

                uf.forEach(s -> {
                    if (s != null && !s.trim().isEmpty()) {
                        filtrosUf.add(cb.like(cb.lower(root.get("uf")), "%" + s.toLowerCase() + "%"));
                    }
                });

                // Se encontrou termos de serviço, agrupa-os com OR
                if (!filtrosUf.isEmpty()) {
                    Predicate orUf= cb.or(filtrosUf.toArray(new Predicate[0]));
                    filtrosPrincipais.add(orUf);
                }
            }

            // Junta o Nome com o Bloco de Serviços usando AND
            return cb.and(filtrosPrincipais.toArray(new Predicate[0]));
        };
    }
}