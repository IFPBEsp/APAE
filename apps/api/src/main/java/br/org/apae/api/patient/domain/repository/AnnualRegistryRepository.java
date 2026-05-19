package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.AnnualRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AnnualRegistryRepository extends JpaRepository<AnnualRegistry, UUID> {

    Optional<AnnualRegistry> findByPatientIdAndYear(UUID patientId, Integer year);

    List<AnnualRegistry> findAllByPatientId(UUID patientId);

    @Query("SELECT DISTINCT ar.year FROM AnnualRegistry ar WHERE ar.year IS NOT NULL ORDER BY ar.year DESC")
    List<String> findDistinctYears();

    @Query("SELECT DISTINCT ar.year FROM AnnualRegistry ar WHERE ar.patientId = :patientId ORDER BY ar.year DESC")
    List<Integer> findYearsByPatientId(@Param("patientId") UUID patientId);

    @Query("SELECT COUNT(ar) > 0 FROM AnnualRegistry ar JOIN ar.disorders d WHERE d.id = :disorderId")
    boolean isDisorderInUse(@Param("disorderId") UUID disorderId);

    @Query("SELECT DISTINCT d.id FROM AnnualRegistry ar JOIN ar.disorders d")
    Set<UUID> findAllUseDisordersIds();

}