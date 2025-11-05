package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {
    Optional<Vaccine> findByName(String name);

    Set<Vaccine> findByNameInIgnoreCase(Collection<String> names);
}