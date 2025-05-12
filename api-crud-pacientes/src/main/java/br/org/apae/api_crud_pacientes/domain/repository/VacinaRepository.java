package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VacinaRepository extends JpaRepository<Vacina, UUID> {
}
