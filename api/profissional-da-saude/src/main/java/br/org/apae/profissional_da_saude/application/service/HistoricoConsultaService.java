package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;
import br.org.apae.profissional_da_saude.domain.repository.HistoricoConsultaRepository;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaUpdateDTO;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.HistoricoConsultaMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class HistoricoConsultaService {

    private final HistoricoConsultaRepository repository;
    private final HistoricoConsultaMapper mapper;

    public HistoricoConsultaResponseDTO criar(HistoricoConsultaCreateDTO dto) {
        if (!validarDadosCriacao(dto)) {
            return null;
        }

        HistoricoConsulta historico = mapper.toDomain(dto);
        historico = repository.salvar(historico);
        return mapper.toResponseDTO(historico);
    }

    public List<HistoricoConsultaResponseDTO> listarTodos() {
        return repository.listarTodos().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public HistoricoConsultaResponseDTO buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .map(mapper::toResponseDTO)
                .orElse(null);
    }

    public HistoricoConsultaResponseDTO atualizar(Long id, HistoricoConsultaUpdateDTO dto) {
        Optional<HistoricoConsulta> optionalHistorico = repository.buscarPorId(id);
        if (optionalHistorico.isEmpty()) {
            return null;
        }

        HistoricoConsulta historico = optionalHistorico.get();

        if (!validarDadosAtualizacao(dto, historico)) {
            return null;
        }

        historico = mapper.updateFromDTO(dto, historico);
        historico = repository.salvar(historico);
        return mapper.toResponseDTO(historico);
    }

    public boolean deletar(Long id) {
        if (repository.buscarPorId(id).isEmpty()) {
            return false;
        }

        repository.deletar(id);
        return true;
    }

    private boolean validarDadosCriacao(HistoricoConsultaCreateDTO dto) {
        if (!dto.getFoiRealizada() && (dto.getJustificativa() == null || dto.getJustificativa().trim().isEmpty())) {
            return false;
        }

        if (repository.existePorAgendamentoEData(dto.getIdAgendamento(), dto.getDataConsulta())) {
            return false;
        }

        return true;
    }

    private boolean validarDadosAtualizacao(HistoricoConsultaUpdateDTO dto, HistoricoConsulta historico) {
        if (!dto.getFoiRealizada() && (dto.getJustificativa() == null || dto.getJustificativa().trim().isEmpty())) {
            return false;
        }

        if (dto.getFoiRealizada()) {
            historico.setJustificativa(null);
        }

        return true;
    }
}