package br.org.apae.api.patient.domain.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.Vaccine;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {
    Optional<Vaccine> findByName(String name);

    Set<Vaccine> findByNameInIgnoreCase(Collection<String> names);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, java.util.UUID id);
}