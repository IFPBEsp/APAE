package br.org.apae.api_crud_pacientes.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.apae.api_crud_pacientes.domain.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    @Query("SELECT p FROM Paciente p WHERE p.cpf LIKE %:cpf%")
    Page<Paciente> findByCpfContaining(@Param("cpf") String cpf, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nome_completo) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Paciente> findByNomeCompletoContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);

    @Query("SELECT p FROM Paciente p WHERE p.cpf LIKE %:cpf% AND LOWER(p.nome_completo) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Paciente> findByCpfContainingAndNomeCompletoContainingIgnoreCase(
            @Param("cpf") String cpf,
            @Param("nome") String nome,
            Pageable pageable);
}
