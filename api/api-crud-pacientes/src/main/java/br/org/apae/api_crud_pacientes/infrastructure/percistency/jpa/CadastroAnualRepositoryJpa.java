package br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa;

import br.org.apae.api_crud_pacientes.infrastructure.entity.CadastroAnualEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadastroAnualRepositoryJpa extends JpaRepository<CadastroAnualEntity, UUID> {}
