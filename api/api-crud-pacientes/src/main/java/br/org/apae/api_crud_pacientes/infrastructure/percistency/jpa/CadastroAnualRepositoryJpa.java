package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api_crud_pacientes.infrastructure.entity.CadastroAnualEntity;

@Repository
public interface CadastroAnualRepositoryJpa extends JpaRepository<CadastroAnualEntity, UUID> {
}
