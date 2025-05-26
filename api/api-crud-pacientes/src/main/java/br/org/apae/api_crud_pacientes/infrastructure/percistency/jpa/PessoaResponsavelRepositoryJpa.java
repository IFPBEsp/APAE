package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaResponsavelEntity;

public interface PessoaResponsavelRepositoryJpa extends JpaRepository<PessoaResponsavelEntity, UUID> {
    @Query("SELECT p FROM PessoaResponsavelEntity p WHERE p.cpf LIKE CONCAT('%', :cpf, '%')")
    Page<PessoaResponsavelEntity> findByCpfContaining(@Param("cpf") String cpf, Pageable pageable);
}
