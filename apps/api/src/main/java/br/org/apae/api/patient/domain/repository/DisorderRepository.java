package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Disorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DisorderRepository extends JpaRepository<Disorder, UUID>{

    Optional<Disorder> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}