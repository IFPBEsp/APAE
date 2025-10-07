// br.org.apae.api.professional.domain.repository.HealthProfessionalRepository
package br.org.apae.api.professional.domain.repository;

import br.org.apae.api.professional.domain.model.HealthProfessional; // Importar a classe consolidada
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// O tipo de entidade referenciado no JpaRepository deve ser HealthProfessional
public interface HealthProfessionalRepository extends JpaRepository<HealthProfessional, UUID> {

    Page<HealthProfessional> findAll(Pageable pageable);

    boolean existsByProfessionalDocument(String professionalDocument);

    boolean existsByEmail(String email);
}