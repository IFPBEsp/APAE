package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistoricoConsultaRepository {
    HistoricoConsulta salvar(HistoricoConsulta historico);
    List<HistoricoConsulta> listarTodos();
    Optional<HistoricoConsulta> buscarPorId(Long id);
    void deletar(Long id);
    boolean existePorAgendamentoEData(Long idAgendamento, LocalDate dataConsulta);
}