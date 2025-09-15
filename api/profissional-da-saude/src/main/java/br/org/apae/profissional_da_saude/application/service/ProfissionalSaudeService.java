package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;
import br.org.apae.profissional_da_saude.domain.model.Disponibilidade;
import br.org.apae.profissional_da_saude.domain.model.Endereco;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository repository;

    @Autowired
    public ProfissionalSaudeService(ProfissionalSaudeRepository repository) {
        this.repository = repository;
    }

    public ProfissionalSaudeResponseDTO save(ProfissionalSaudeCreateDTO dto) {
        if (repository.existsByDocProfissional(dto.getDocProfissional())) {
            throw new ValidacaoNegocioException("Documento profissional já cadastrado.");
        }

        if (repository.existsByEmail(dto.getEmail())) {
            throw new ValidacaoNegocioException("E-mail já cadastrado.");
        }

        List<Disponibilidade> disponibilidades = dto.getDisponibilidades() != null
                ? dto.getDisponibilidades().stream()
                        .map(d -> new Disponibilidade(d.getDia(), d.getTurno()))
                        .collect(Collectors.toList())
                : new ArrayList<>();

        ProfissionalSaude profissional = new ProfissionalSaude(
                dto.getAreaDaSaude(),
                dto.getTelefone(),
                dto.getDocProfissional(),
                dto.getEmail(),
                dto.getNome(),
                dto.getRg(),
                new Endereco(
                        dto.getEndereco().getEstado(),
                        dto.getEndereco().getCidade(),
                        dto.getEndereco().getBairro(),
                        dto.getEndereco().getRua(),
                        dto.getEndereco().getNumero(),
                        dto.getEndereco().getCep(),
                        dto.getEndereco().getComplemento()),
                disponibilidades);

        profissional.getDisponibilidades().forEach(d -> d.setProfissional(profissional));

        ProfissionalSaude saved = repository.save(profissional);
        return ProfissionalSaudeMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProfissionalSaudeResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ProfissionalSaudeMapper::toResponseDTO);
    }

    public Optional<ProfissionalSaudeResponseDTO> findById(UUID id) {
        return repository.findById(id)
                .map(ProfissionalSaudeMapper::toResponseDTO)
                .or(() -> {
                    throw new EntidadeNaoEncontradaException("Profissional não encontrado.");
                });
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public ProfissionalSaudeResponseDTO update(UUID id, ProfissionalSaudeUpdateDTO dto) {
        ProfissionalSaude existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

        Optional.ofNullable(dto.getAreaDaSaude()).ifPresent(existente::setAreaDaSaude);
        Optional.ofNullable(dto.getDocProfissional()).ifPresent(existente::setDocProfissional);
        Optional.ofNullable(dto.getNome()).ifPresent(existente::setNome);
        Optional.ofNullable(dto.getEmail()).ifPresent(existente::setEmail);
        Optional.ofNullable(dto.getTelefone()).ifPresent(existente::setTelefone);
        Optional.ofNullable(dto.getRg()).ifPresent(existente::setRg);

        if (dto.getEndereco() != null) {
            Optional.ofNullable(dto.getEndereco().getEstado()).ifPresent(existente.getEndereco()::setEstado);
            Optional.ofNullable(dto.getEndereco().getCidade()).ifPresent(existente.getEndereco()::setCidade);
            Optional.ofNullable(dto.getEndereco().getBairro()).ifPresent(existente.getEndereco()::setBairro);
            Optional.ofNullable(dto.getEndereco().getRua()).ifPresent(existente.getEndereco()::setRua);
            Optional.ofNullable(dto.getEndereco().getNumero()).ifPresent(existente.getEndereco()::setNumero);
            Optional.ofNullable(dto.getEndereco().getCep()).ifPresent(existente.getEndereco()::setCep);
            Optional.ofNullable(dto.getEndereco().getComplemento()).ifPresent(existente.getEndereco()::setComplemento);
        }

        existente.getDisponibilidades().clear();
        if (dto.getDisponibilidades() != null) {
            dto.getDisponibilidades().stream()
                    .map(d -> new Disponibilidade(d.getDia(), d.getTurno()))
                    .forEach(d -> {
                        d.setProfissional(existente);
                        existente.getDisponibilidades().add(d);
                    });
        }

        ProfissionalSaude saved = repository.update(existente);
        return ProfissionalSaudeMapper.toResponseDTO(saved);
    }
}
