package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.domain.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository {

    Paciente save(Paciente paciente);

    Page<Paciente> findAll(Pageable pageable);

    Optional<Paciente> findById(UUID id);

    Paciente update(Paciente paciente);

    void deleteById(UUID id);
}
