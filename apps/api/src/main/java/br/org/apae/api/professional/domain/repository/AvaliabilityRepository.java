package br.org.apae.api.professional.domain.repository;

import br.org.apae.api.professional.domain.model.Avaliability;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliabilityRepository extends JpaRepository<Avaliability, UUID> {

}