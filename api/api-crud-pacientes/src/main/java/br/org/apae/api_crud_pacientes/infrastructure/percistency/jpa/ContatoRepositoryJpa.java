package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import br.org.apae.api_crud_pacientes.infrastructure.entity.ContatoEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepositoryJpa extends JpaRepository<ContatoEntity, UUID> {
  Page<ContatoEntity> findByEnderecoIgnoreCase(String endereco, Pageable pageable);
}
