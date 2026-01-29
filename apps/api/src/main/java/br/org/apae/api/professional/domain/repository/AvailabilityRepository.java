package br.org.apae.api.professional.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api.professional.domain.model.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
    List<Availability> findAllByProfessional_Id(UUID professionalId);
    boolean existsByProfessional_IdAndDayAndShift(UUID professionalId,
            br.org.apae.api.professional.domain.model.enums.Day day,
            br.org.apae.api.professional.domain.model.enums.Shift shift);
}
