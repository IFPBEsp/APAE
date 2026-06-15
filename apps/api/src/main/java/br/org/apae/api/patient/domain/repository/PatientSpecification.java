package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.servicetype.domain.model.ServiceType;
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
            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            filters.forEach((key, value) -> {
                if (value != null && !value.isBlank()) {
                    switch (key) {
                        case "name":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + value.toLowerCase() + "%"));
                            break;

                        case "city":
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("address").get("city")), "%" + value.toLowerCase() + "%"));
                            break;
                        case "year":
                            try {
                                Integer anoValor = Integer.parseInt(value);

                                Subquery<UUID> anoSubQuery = query.subquery(UUID.class);
                                Root<AnnualRegistry> anoRoot = anoSubQuery.from(AnnualRegistry.class);

                                anoSubQuery.select(anoRoot.get("patientId"))
                                        .where(
                                                criteriaBuilder.equal(anoRoot.get("year"), anoValor)
                                        );

                                predicates.add(root.get("id").in(anoSubQuery));
                            } catch (NumberFormatException ignored) {
                            }
                            break;

                        case "disorder":
                            Subquery<UUID> transtornoSubQuery = query.subquery(UUID.class);
                            Root<AnnualRegistry> transtornoRoot = transtornoSubQuery.from(AnnualRegistry.class);
                            Join<AnnualRegistry, Disorder> disorderJoin = transtornoRoot.join("disorders");
                            transtornoSubQuery.select(transtornoRoot.get("patientId"))
                                    .where(
                                            criteriaBuilder.like(criteriaBuilder.lower(disorderJoin.get("name")), "%" + value.toLowerCase() + "%")
                                    );
                            predicates.add(root.get("id").in(transtornoSubQuery));
                            break;

                        case "treatmentType":
                            Subquery<UUID> tipoSubQuery = query.subquery(UUID.class);
                            Root<AnnualRegistry> tipoRoot = tipoSubQuery.from(AnnualRegistry.class);
                            Join<AnnualRegistry, ServiceType> serviceAreaJoin = tipoRoot.join("serviceAreas");
                            tipoSubQuery.select(tipoRoot.get("patientId"))
                                    .where(criteriaBuilder.like(
                                            criteriaBuilder.lower(serviceAreaJoin.get("area")),
                                            "%" + value.toLowerCase() + "%"));

                            predicates.add(root.get("id").in(tipoSubQuery));
                            break;
                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
