package br.org.apae.laudos_medicos.application.services;

import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.domain.entities.Laudo;

import java.util.List;

public interface ILaudoService {
    Laudo criarNovoLaudo(LaudoRequestDTO laudoRequestDTO);
    Laudo buscarLaudoPorId(Long id);
    List<Laudo> buscarLaudosPorIdPaciente(Long idPaciente);
    List<Laudo> buscarTodosOsLaudos();
    Laudo atualizarLaudo(Long id, Laudo laudo);
    void deletarLaudo(Long id);
}
