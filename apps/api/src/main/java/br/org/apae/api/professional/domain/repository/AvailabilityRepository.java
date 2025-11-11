package br.org.apae.api.professional.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api.professional.domain.model.Availability;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID> {

}