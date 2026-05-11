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

    public static Specification<Business> hasValues(List<String> values, String field) {
        return (root, query, cb) -> {
            if (values == null || values.isEmpty()) return null;

            List<Predicate> predicates = values.stream()
                    .filter(s -> s != null && !s.trim().isEmpty())
                    .map(s -> cb.like(cb.lower(root.get(field)), "%" + s.toLowerCase() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}