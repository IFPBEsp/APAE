package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnualRegistryRepository extends JpaRepository<AnnualRegistry, Long> {
}
