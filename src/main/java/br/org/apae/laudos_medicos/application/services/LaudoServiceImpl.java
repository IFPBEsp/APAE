package br.org.apae.laudos_medicos.application.services;


import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.application.exceptions.LaudoNaoEncontradoException;
import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.infrastructure.clients.MedicoClient;
import br.org.apae.laudos_medicos.infrastructure.clients.PacienteClient;
import br.org.apae.laudos_medicos.infrastructure.clients.dtos.MedicoResponseDTO;
import br.org.apae.laudos_medicos.infrastructure.clients.dtos.PacienteResponseDTO;
import br.org.apae.laudos_medicos.infrastructure.persistences.LaudoRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaudoServiceImpl implements LaudoService {

    @Autowired
    private LaudoRepositoryImpl laudoRepository;

    @Autowired
    private PacienteClient pacienteClient;

    @Autowired
    private MedicoClient medicoClient;

    @Override
    public Laudo criarNovoLaudo(LaudoRequestDTO laudoRequestDTO) {
        PacienteResponseDTO pacienteResponseDTO = this.pacienteClient.buscarPacientePorId(laudoRequestDTO.idPaciente());
        MedicoResponseDTO medicoResponseDTO = this.medicoClient.buscarMedicoPorId(laudoRequestDTO.idMedico());
        Laudo laudo = new Laudo();
        laudo.setDataEmissao(laudoRequestDTO.dataEmissao());
        laudo.setDescricao(laudoRequestDTO.descricao());
        laudo.setIdMedico(medicoResponseDTO.id());
        laudo.setIdPaciente(pacienteResponseDTO.id());
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
        if (laudoRequestDTO.idPaciente() != null && !laudoRequestDTO.idPaciente().equals(persitLaudo.getIdPaciente())) {
            PacienteResponseDTO pacienteResponseDTO = this.pacienteClient.buscarPacientePorId(laudoRequestDTO.idPaciente());
            persitLaudo.setIdPaciente(pacienteResponseDTO.id());
        }
        if (laudoRequestDTO.idMedico() != null && !laudoRequestDTO.idMedico().equals(persitLaudo.getIdMedico())) {
            MedicoResponseDTO medicoResponseDTO = this.medicoClient.buscarMedicoPorId(laudoRequestDTO.idMedico());
            persitLaudo.setIdPaciente(medicoResponseDTO.id());
        }
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
