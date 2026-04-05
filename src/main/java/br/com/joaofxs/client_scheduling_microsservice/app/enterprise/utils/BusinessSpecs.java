package br.com.joaofxs.client_scheduling_microsservice.app.enterprise.utils;

import br.com.joaofxs.client_scheduling_microsservice.app.enterprise.model.Business;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BusinessSpecs {
    public static Specification<Business> withFilter(String searchTerm) {
        return (root, query, cb) -> {
            // Se a busca estiver vazia, retorna um predicado sempre verdadeiro (SELECT * FROM ...)
            if (searchTerm == null || searchTerm.isBlank()) {
                return cb.conjunction();
            }

            String filterValue = "%" + searchTerm.toLowerCase() + "%";

            // Criamos o predicado para o campo "name"
            // nome do parâmetro do método para 'searchTerm' para não conflitar com 'query' do Lambda
            return cb.like(cb.lower(root.get("name")), filterValue);
        };
    }
}
