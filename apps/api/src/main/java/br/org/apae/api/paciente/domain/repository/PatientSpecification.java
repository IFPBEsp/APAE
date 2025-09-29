package br.org.apae.api.paciente.domain.repository;

import br.org.apae.api.paciente.domain.model.Patient;
import br.org.apae.api.paciente.dto.filter.PatientFilterDTO;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class PatientSpecification {
    public static Specification<Patient> filterBy(PatientFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + filter.name().toLowerCase() + "%"));
            }

            if (filter.cpf() != null && !filter.cpf().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), filter.cpf()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
