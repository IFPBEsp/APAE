package br.org.apae.api_crud_pacientes.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;

public interface PessoaResponsavelRepository extends JpaRepository<PessoaResponsavel, UUID> {
    @Query("SELECT p FROM PessoaResponsavel p WHERE p.cpf LIKE %:cpf%")
    Page<PessoaResponsavel> findByCpfContaining(@Param("cpf") String cpf, Pageable pageable);
}
