package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;

public interface PessoaRepositoryJpa extends JpaRepository<PessoaEntity, UUID> {
    @Query("SELECT p FROM PessoaEntity p WHERE p.cpf LIKE %:cpf%")
    Page<PessoaEntity> findByCpfContaining(@Param("cpf") String cpf, Pageable pageable);

    @Query("SELECT p FROM PessoaEntity p WHERE LOWER(p.nome_completo) LIKE LOWER(CONCAT('%', :nome_completo, '%'))")
    Page<PessoaEntity> findByNomeCompletoContainingIgnoreCase(@Param("nome_completo") String nome, Pageable pageable);

    @Query("SELECT p FROM PessoaEntity p WHERE p.cpf LIKE %:cpf% AND LOWER(p.nome_completo) LIKE LOWER(CONCAT('%', :nome_completo, '%'))")
    Page<PessoaEntity> findByCpfContainingAndNomeCompletoContainingIgnoreCase(
            @Param("cpf") String cpf,
            @Param("nome_completo") String nome,
            Pageable pageable);
}
