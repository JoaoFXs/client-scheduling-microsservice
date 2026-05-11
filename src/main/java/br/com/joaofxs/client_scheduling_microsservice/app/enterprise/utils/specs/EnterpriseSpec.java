package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils.specs;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
public class EnterpriseSpec {

    // Specifications atômicas
    public static Specification<Business> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.trim().isEmpty() ? null :
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Business> hasServices(List<String> services) {
        return (root, query, cb) -> {
            if (services == null || services.isEmpty()) return null;

            List<Predicate> predicates = services.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(s -> cb.like(cb.lower(root.get("service")), "%" + s.toLowerCase() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Business> hasUf(List<String> ufs) {
        return (root, query, cb) -> {
            if (ufs == null || ufs.isEmpty()) return null;

            List<Predicate> predicates = ufs.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(s -> cb.like(cb.lower(root.get("uf")), "%" + s.toLowerCase() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}