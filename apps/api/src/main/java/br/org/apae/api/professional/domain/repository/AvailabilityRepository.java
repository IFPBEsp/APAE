package br.org.apae.api.professional.domain.repository;

import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {
    
    List<Availability> findByProfessional(HealthProfessional professional);
    
    boolean existsByProfessionalAndDayAndShift(HealthProfessional professional, Day day, Shift shift);
    
    Optional<Availability> findByProfessionalAndDayAndShift(HealthProfessional professional, Day day, Shift shift);
}
