package br.org.apae.api.patient.domain.repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;

@Repository
public interface AnnualRegistryRepository extends JpaRepository<AnnualRegistry, UUID> {

    Optional<AnnualRegistry> findByPatientIdAndYear(UUID patientId, Year year);

    List<AnnualRegistry> findAllByPatientId(UUID patientId);
}