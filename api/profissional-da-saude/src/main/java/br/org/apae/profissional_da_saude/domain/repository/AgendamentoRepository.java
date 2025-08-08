package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public interface AgendamentoRepository {
    Agendamento save(Agendamento agendamento);

    Page<Agendamento> findAll(Pageable pageable);

    Optional<Agendamento> findById(UUID id);

    Agendamento update(Agendamento agendamento);

    void deleteById(UUID id);

    Page<Agendamento> findAllByProximaConsulta(LocalDate data, Pageable pageable);

    Page<Agendamento> findAllByProximaConsultaAndHoraProximaConsulta(LocalDate data, LocalTime hora, Pageable pageable);
}
