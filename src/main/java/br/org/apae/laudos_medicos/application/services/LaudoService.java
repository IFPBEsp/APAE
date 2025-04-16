package br.org.apae.laudos_medicos.application.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.application.dtos.responses.LaudoResponseDTO;
import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.domain.repositories.LaudoRepository;
import br.org.apae.laudos_medicos.application.exceptions.EntityNotFoundException;

@Service
public class LaudoService {

    private LaudoRepository laudoRepository;

    public LaudoService(LaudoRepository laudoRepository) {
        this.laudoRepository = laudoRepository;
    }
    
    public LaudoResponseDTO criarNovoLaudo(LaudoRequestDTO laudoRequestDTO) {
        Laudo laudo = new Laudo(null, laudoRequestDTO.getId(), laudoRequestDTO.getIdPaciente(), laudoRequestDTO.getDataEmissao(), laudoRequestDTO.getDescricao());
        laudoRepository.save(laudo);
        return toResponse(laudo);
    }

    public Laudo buscarLaudoPorId(Long id) {
        return laudoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado"));
    }

    public LaudoResponseDTO atualizarLaudo(Long id, LaudoRequestDTO novoDto) {
        Laudo laudo = laudoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Laudo não encontrado"));

        Laudo atualizado = new Laudo(id, novoDto.getId(), novoDto.getIdPaciente(), novoDto.getDataEmissao(), novoDto.getDescricao());
        laudoRepository.save(atualizado);
        return toResponse(atualizado);
    }

    private LaudoResponseDTO toResponse(Laudo laudo) {
        LaudoResponseDTO dto = new LaudoResponseDTO(null, null, null, null);
        dto.setId(laudo.getId());
        dto.setIdPaciente(laudo.getIdPaciente());
        dto.setDataEmissao(laudo.getDataEmissao());
        dto.setDescricao(laudo.getDescricao());
        return dto;
    }

    public void deletarLaudo(Long id) {
        laudoRepository.delete(id);
    }

   /*  public LaudoResponseDTO buscarLaudoPacientePorId(Long id, Long idPaciente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarLaudoPacientePorId'");
    } 
*/


    
}
