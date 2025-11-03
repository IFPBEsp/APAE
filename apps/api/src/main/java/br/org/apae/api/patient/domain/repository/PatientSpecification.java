package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.model.Patient;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PatientSpecification {
    public static Specification<Patient> filterBy(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);

            filters.forEach((key, value) -> {
                if (value != null && !value.isBlank()) {
                    switch (key) {
                        case "Nome":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + value.toLowerCase() + "%"));
                            break;

                        case "cidade":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + value.toLowerCase() + "%"));
                            break;
                        case "ano":
                            Integer anoValor = Integer.parseInt(value);

                            Subquery<UUID> anoSubQuery = query.subquery(UUID.class);
                            Root<AnnualRegistry> anoRoot = anoSubQuery.from(AnnualRegistry.class);

                            anoSubQuery.select(anoRoot.get("patientId"))
                                    .where(
                                            criteriaBuilder.equal(anoRoot.get("year"), anoValor)
                                    );

                            predicates.add(root.get("id").in(anoSubQuery));
                            break;

                        case "transtorno":
                            Subquery<UUID> transtornoSubQuery = query.subquery(UUID.class);
                            Root<AnnualRegistry> transtornoRoot = transtornoSubQuery.from(AnnualRegistry.class);
                            Join<AnnualRegistry, Disorder> disorderJoin = transtornoRoot.join("disorders");
                            transtornoSubQuery.select(transtornoRoot.get("patientId"))
                                    .where(
                                            criteriaBuilder.like(criteriaBuilder.lower(disorderJoin.get("name")), "%" + value.toLowerCase() + "%")
                                    );
                            predicates.add(root.get("id").in(transtornoSubQuery));
                            break;
                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
