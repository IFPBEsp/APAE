package br.org.apae.api_crud_pacientes.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

}
