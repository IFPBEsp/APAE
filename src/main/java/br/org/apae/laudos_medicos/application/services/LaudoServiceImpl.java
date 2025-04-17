package br.org.apae.laudos_medicos.application.services;


import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.application.exceptions.LaudoNaoEncontradoException;
import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.infrastructure.persistences.LaudoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaudoServiceImpl implements LaudoService {

    @Autowired
    private final LaudoRepositoryImpl laudoRepository;

    public LaudoServiceImpl(LaudoRepositoryImpl laudoRepository) {
        this.laudoRepository = laudoRepository;
    }

    @Override
    public Laudo criarNovoLaudo(LaudoRequestDTO laudoRequestDTO) {
        Laudo laudo = new Laudo();
        laudo.setDataEmissao(laudoRequestDTO.dataEmissao());
        laudo.setDescricao(laudoRequestDTO.descricao());
        laudo.setIdMedico(laudoRequestDTO.idMedico());
        laudo.setIdPaciente(laudoRequestDTO.idPaciente());
        return this.laudoRepository.save(laudo);
    }

    @Override
    public Laudo buscarLaudoPorId(Long id) {
        return this.laudoRepository.findById(id).orElseThrow(() -> new LaudoNaoEncontradoException("Laudo não encontrado com esse id."));
    }

    @Override
    public List<Laudo> buscarLaudosPorIdPaciente(Long idPaciente) {
        return this.laudoRepository.findLaudosByPacientId(idPaciente);
    }

    @Override
    public List<Laudo> buscarTodosOsLaudos() {
        return this.laudoRepository.findAll();
    }

    @Override
    public Laudo atualizarLaudo(Long id, LaudoRequestDTO laudoRequestDTO) {
        Laudo persitLaudo = this.buscarLaudoPorId(id);
        if (laudoRequestDTO.idMedico() != null) persitLaudo.setIdMedico(laudoRequestDTO.idMedico());
        if (laudoRequestDTO.idPaciente() != null) persitLaudo.setIdPaciente(laudoRequestDTO.idPaciente());
        if (laudoRequestDTO.descricao() != null) persitLaudo.setDescricao(laudoRequestDTO.descricao());
        if (laudoRequestDTO.dataEmissao() != null) persitLaudo.setDataEmissao(laudoRequestDTO.dataEmissao());
        return this.laudoRepository.save(persitLaudo);
    }

    @Override
    public void deletarLaudo(Long id) {
        Laudo laudo = this.buscarLaudoPorId(id);
        this.laudoRepository.delete(laudo);
    }
}
