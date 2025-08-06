package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AgendamentoRepository {
    Agendamento save(Agendamento agendamento);

    Page<Agendamento> findAll(Pageable pageable);

    Optional<Agendamento> findById(UUID id);

    Agendamento update(Agendamento agendamento);

    void deleteById(UUID id);
}
