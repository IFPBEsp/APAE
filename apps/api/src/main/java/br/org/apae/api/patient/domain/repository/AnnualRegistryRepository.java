package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnualRegistryRepository extends JpaRepository<AnnualRegistry, UUID> {

    Optional<AnnualRegistry> findByPatientIdAndYear(UUID patientId, Year year);

    List<AnnualRegistry> findAllByPatientId(UUID patientId);

    @Query("SELECT DISTINCT ar.year FROM AnnualRegistry ar WHERE ar.year IS NOT NULL ORDER BY ar.year DESC")
    List<String> findDistinctYears();
}