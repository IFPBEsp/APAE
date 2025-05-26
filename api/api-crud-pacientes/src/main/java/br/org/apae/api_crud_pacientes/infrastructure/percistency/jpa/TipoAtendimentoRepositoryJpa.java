
package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoAtendimentoEntity;

public interface TipoAtendimentoRepositoryJpa extends JpaRepository<TipoAtendimentoEntity, UUID> {
    Page<TipoAtendimentoEntity> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
