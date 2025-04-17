package br.org.apae.laudos_medicos.application.services;

import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.domain.entities.Laudo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LaudoService {
    Laudo criarNovoLaudo(LaudoRequestDTO laudoRequestDTO);
    Laudo buscarLaudoPorId(Long id);
    List<Laudo> buscarLaudosPorIdPaciente(Long idPaciente);
    List<Laudo> buscarTodosOsLaudos();
    Laudo atualizarLaudo(Long id, LaudoRequestDTO laudoRequestDTO);
    void deletarLaudo(Long id);
}
