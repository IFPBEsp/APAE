package br.org.apae.api.professional.domain.repository;

import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface HealthProfessionalRepository extends JpaRepository<HealthProfessional, UUID> {

   // Page<HealthProfessional> findAll(Pageable pageable);

    Page<HealthProfessional> findByAtivo(Boolean ativo, Pageable pageable);

    boolean existsByProfessionalDocument(String professionalDocument);

    boolean existsByEmail(String email);

    boolean existsByIdentityDocument(String identityDocument);

        @Query("""
            SELECT a.hour
            FROM Appointment a
            WHERE a.professional.id = :professionalId
              AND a.initialDate = :date
              AND a.isActive = true
        """)
        List<LocalTime> findOccupiedHours(UUID professionalId, LocalDate date);
}