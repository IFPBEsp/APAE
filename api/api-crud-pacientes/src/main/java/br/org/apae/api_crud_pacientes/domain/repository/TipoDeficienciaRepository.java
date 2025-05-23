package br.org.apae.api_crud_pacientes.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;

public interface TipoDeficienciaRepository extends JpaRepository<TipoDeficiencia, UUID> {

    Page<TipoDeficiencia> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

}
