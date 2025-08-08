package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoUpdateDTO;
import br.org.apae.profissional_da_saude.application.service.exceptions.AgendamentoNaoEncontradoException;
import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AgendamentoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public AgendamentoResponseDTO create(AgendamentoCreateDTO dto) {
        Agendamento agendamento = AgendamentoMapper.toDomain(dto);
        Agendamento agendamentoSaved = this.repository.save(agendamento);
        return AgendamentoMapper.toResponseDTO(agendamentoSaved);
    }

    public AgendamentoResponseDTO findById(UUID id) {
        Agendamento agendamento = this.repository.findById(id)
                .orElseThrow(AgendamentoNaoEncontradoException::new);
        return AgendamentoMapper.toResponseDTO(agendamento);
    }

    public Page<AgendamentoResponseDTO> findAll(Pageable pageable) {
        return this.repository.findAll(pageable)
                .map(AgendamentoMapper::toResponseDTO);
    }

    public AgendamentoResponseDTO update(UUID id, AgendamentoUpdateDTO dto) {
        Agendamento agendamentoSaved = this.repository.findById(id)
                .orElseThrow(AgendamentoNaoEncontradoException::new);

        Optional.ofNullable(dto.getIdPaciente())
                .ifPresent(agendamentoSaved::setIdPaciente);

        Optional.ofNullable(dto.getFrequenciaDias())
                .ifPresent(agendamentoSaved::setFrequenciaDias);

        Optional.ofNullable(dto.getIdProfissional())
                .ifPresent(agendamentoSaved::setIdProfissional);

        Optional.ofNullable(dto.getProximaConsulta())
                .ifPresent(agendamentoSaved::setProximaConsulta);

        Optional.ofNullable(dto.getHoraProximaConsulta())
                .ifPresent(agendamentoSaved::setHoraProximaConsulta);

        Agendamento agendamentoUpdated = this.repository.update(agendamentoSaved);
        return AgendamentoMapper.toResponseDTO(agendamentoUpdated);
    }

    public Page<AgendamentoResponseDTO> findByProximaConsulta(LocalDate data, Pageable pageable) {
        return this.repository.findAllByProximaConsulta(data, pageable)
                .map(AgendamentoMapper::toResponseDTO);
    }

    public Page<AgendamentoResponseDTO> findAllByProximaConsultaAndHoraProximaConsulta(LocalDate data, LocalTime hora, Pageable pageable) {
        return  this.repository.findAllByProximaConsultaAndHoraProximaConsulta(data, hora, pageable).map(AgendamentoMapper::toResponseDTO);
    }

    public void remove(UUID id) {
        this.repository.deleteById(id);
    }
}
