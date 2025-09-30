package br.org.apae.api.paciente.domain.repository;

import br.org.apae.api.paciente.domain.model.Patient;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatientSpecification {

    /**
     * Cria uma Specification para a entidade Patient com base em um mapa de filtros dinâmicos.
     * @param filters Um mapa onde a chave é o nome do campo e o valor é o valor a ser filtrado.
     * @return uma Specification que pode ser usada pelo JpaRepository.
     */
    public static Specification<Patient> filterBy(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                if (value != null && !value.isBlank()) {
                    switch (key) {
                        case "fullName":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + value.toLowerCase() + "%"));
                            break;
                        case "cpf":
                            predicates.add(criteriaBuilder.equal(root.get("cpf"), value));
                            break;
                        case "city":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + value.toLowerCase() + "%"));
                            break;

                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
