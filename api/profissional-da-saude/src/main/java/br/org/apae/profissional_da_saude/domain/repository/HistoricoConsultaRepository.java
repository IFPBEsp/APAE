package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoricoConsultaRepository {
    HistoricoConsulta salvar(HistoricoConsulta historico);
    List<HistoricoConsulta> listarTodos();
    Optional<HistoricoConsulta> buscarPorId(UUID id);
    void deletar(UUID id);
    boolean existePorAgendamentoEData(UUID idAgendamento, LocalDate dataConsulta, LocalTime horaConsulta);
}