package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PessoaRepositoryJpa extends JpaRepository<PessoaEntity, UUID> {
  @Query("SELECT p FROM PessoaEntity p WHERE p.cpf LIKE %:cpf%")
  Page<PessoaEntity> findByCpfContaining(@Param("cpf") String cpf, Pageable pageable);

  @Query(
      "SELECT p FROM PessoaEntity p WHERE LOWER(p.nomeCompleto) LIKE LOWER(CONCAT('%',"
          + " :nomeCompleto, '%'))")
  Page<PessoaEntity> findByNomeCompletoContainingIgnoreCase(
      @Param("nomeCompleto") String nome, Pageable pageable);

  @Query(
      "SELECT p FROM PessoaEntity p WHERE p.cpf LIKE %:cpf% AND LOWER(p.nomeCompleto) LIKE"
          + " LOWER(CONCAT('%', :nomeCompleto, '%'))")
  Page<PessoaEntity> findByCpfContainingAndNomeCompletoContainingIgnoreCase(
      @Param("cpf") String cpf, @Param("nomeCompleto") String nome, Pageable pageable);
}
