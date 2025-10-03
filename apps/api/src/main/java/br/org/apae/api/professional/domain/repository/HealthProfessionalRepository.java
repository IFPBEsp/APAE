package br.org.apae.api.professional.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HealthProfessionalRepository extends JpaRepository<br.org.apae.api.professional.infra.entity.HealthProfessionalEntity, UUID> {

    Page<br.org.apae.api.professional.infra.entity.HealthProfessionalEntity> findAll(Pageable pageable);

    boolean existsByProfessionalDocument(String professionalDocument);

    boolean existsByEmail(String email);
}