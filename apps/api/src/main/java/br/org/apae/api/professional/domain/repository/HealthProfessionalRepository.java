package br.org.apae.api.professional.domain.repository;

import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HealthProfessionalRepository extends JpaRepository<HealthProfessional, UUID> {

   // Page<HealthProfessional> findAll(Pageable pageable);

    Page<HealthProfessional> findByAtivo(Boolean ativo, Pageable pageable);

    boolean existsByProfessionalDocument(String professionalDocument);

    boolean existsByEmail(String email);

    boolean existsByIdentityDocument(String identityDocument);
}