package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoAtendimentoEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoAtendimentoRepositoryJpa extends JpaRepository<TipoAtendimentoEntity, UUID> {
  Page<TipoAtendimentoEntity> findByDescricaoContainingIgnoreCase(
      String descricao, Pageable pageable);
}
