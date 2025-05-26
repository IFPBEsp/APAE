package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoDeficienciaEntity;

public interface TipoDeficienciaRepositoryJpa extends JpaRepository<TipoDeficienciaEntity, UUID> {

    Page<TipoDeficienciaEntity> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

}
