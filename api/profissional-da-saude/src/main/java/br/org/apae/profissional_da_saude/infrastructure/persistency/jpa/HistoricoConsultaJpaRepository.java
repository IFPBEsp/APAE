package br.org.apae.profissional_da_saude.infrastructure.persistency.jpa;

import br.org.apae.profissional_da_saude.infrastructure.entity.HistoricoConsultaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface HistoricoConsultaJpaRepository extends JpaRepository<HistoricoConsultaEntity, UUID> {

    boolean existsByIdAgendamentoAndDataConsultaAndHoraConsulta(
            UUID idAgendamento,
            LocalDate dataConsulta,
            LocalTime horaConsulta
    );
}
