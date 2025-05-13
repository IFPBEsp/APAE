package br.org.apae.api_crud_pacientes.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;

public interface TipoAtendimentoRepository extends JpaRepository<TipoAtendimento, UUID> {
    Page<TipoAtendimento> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
}
