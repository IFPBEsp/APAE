package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.infrastructure.entity.AgendamentoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface AgendamentoRepositoryJpa extends JpaRepository<AgendamentoEntity, UUID> {

    Page<Agendamento> findAllByProximaConsulta(LocalDate data, Pageable pageable);
    Page<Agendamento> findAllByProximaConsultaAndHoraProximaConsulta(LocalDate data, LocalTime hora, Pageable pageable);
}
