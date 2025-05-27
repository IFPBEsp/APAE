package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import br.org.apae.api_crud_pacientes.infrastructure.entity.VacinaEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacinaRepositoryJpa extends JpaRepository<VacinaEntity, UUID> {
  Page<VacinaEntity> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
