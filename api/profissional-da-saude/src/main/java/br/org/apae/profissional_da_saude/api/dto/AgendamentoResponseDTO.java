
package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoUpdateDTO;
import br.org.apae.profissional_da_saude.application.service.exceptions.AgendamentoNaoEncontradoException;
import br.org.apae.profissional_da_saude.domain.model.Agendamento;
import br.org.apae.profissional_da_saude.domain.repository.AgendamentoRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.PacienteEntity;
import br.org.apae.profissional_da_saude.infrastructure.entity.ProfissionalSaudeEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.AgendamentoMapper;
import br.org.apae.profissional_da_saude.infrastructure.persistency.repository.PacienteRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.repository.ProfissionalSaudeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalSaudeRepository profissionalRepository;

    public AgendamentoService(
        AgendamentoRepository repository,
        PacienteRepository pacienteRepository,
        ProfissionalSaudeRepository profissionalRepository
    ) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public AgendamentoResponseDTO create(AgendamentoCreateDTO dto) {
        PacienteEntity paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getIdPaciente()));

        ProfissionalSaudeEntity profissional = profissionalRepository.findById(dto.getIdProfissional())
                .orElseThrow(() -> new RuntimeException("Profissional da Saúde não encontrado com id: " + dto.getIdProfissional()));

        Agendamento agendamento = AgendamentoMapper.toDomain(dto);

        agendamento.setPaciente(paciente);
        agendamento.setProfissionalDaSaude(profissional);

        Agendamento agendamentoSalvo = repository.save(agendamento);

        return AgendamentoMapper.toResponseDTO(agendamentoSalvo);
    }

    public AgendamentoResponseDTO findById(UUID id) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(AgendamentoNaoEncontradoException::new);
        return AgendamentoMapper.toResponseDTO(agendamento);
    }

    public Page<AgendamentoResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AgendamentoMapper::toResponseDTO);
    }

    public AgendamentoResponseDTO update(UUID id, AgendamentoUpdateDTO dto) {
        Agendamento agendamentoSaved = repository.findById(id)
                .orElseThrow(AgendamentoNaoEncontradoException::new);

        if (dto.getIdPaciente() != null) {
            PacienteEntity paciente = pacienteRepository.findById(dto.getIdPaciente())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado com id: " + dto.getIdPaciente()));
            agendamentoSaved.setPaciente(paciente);
        }

        if (dto.getIdProfissional() != null) {
            ProfissionalSaudeEntity profissional = profissionalRepository.findById(dto.getIdProfissional())
                    .orElseThrow(() -> new RuntimeException("Profissional não encontrado com id: " + dto.getIdProfissional()));
            agendamentoSaved.setProfissionalDaSaude(profissional);
        }

        if (dto.getFrequenciaDias() != null) {
            agendamentoSaved.setFrequenciaDias(dto.getFrequenciaDias());
        }

        if (dto.getProximaConsulta() != null) {
            agendamentoSaved.setProximaConsulta(dto.getProximaConsulta());
        }

        if (dto.getHoraProximaConsulta() != null) {
            agendamentoSaved.setHoraProximaConsulta(dto.getHoraProximaConsulta());
        }

        if (dto.getConfirmado() != null) {
            agendamentoSaved.setConfirmado(dto.getConfirmado());
        }

        Agendamento agendamentoUpdated = repository.save(agendamentoSaved);
        return AgendamentoMapper.toResponseDTO(agendamentoUpdated);
    }

    public Page<AgendamentoResponseDTO> findByProximaConsulta(LocalDate data, Pageable pageable) {
        return repository.findAllByProximaConsulta(data, pageable)
                .map(AgendamentoMapper::toResponseDTO);
    }

    public Page<AgendamentoResponseDTO> findAllByProximaConsultaAndHoraProximaConsulta(LocalDate data, LocalTime hora, Pageable pageable) {
        return repository.findAllByProximaConsultaAndHoraProximaConsulta(data, hora, pageable)
                .map(AgendamentoMapper::toResponseDTO);
    }

    public void remove(UUID id) {
        repository.deleteById(id);
    }
}
