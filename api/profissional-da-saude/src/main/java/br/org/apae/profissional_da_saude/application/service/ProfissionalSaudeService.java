package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;
import br.org.apae.profissional_da_saude.domain.model.Endereco;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfissionalSaudeService {

  private final ProfissionalSaudeRepository repository;

  @Autowired
  public ProfissionalSaudeService(ProfissionalSaudeRepository repository) {
    this.repository = repository;
  }

  public ProfissionalSaudeResponseDTO save(ProfissionalSaudeCreateDTO dto) {
    if (this.repository.existsByDocProfissional(dto.getDocProfissional())) {
      throw new ValidacaoNegocioException("Documento profissional já cadastrado.");
    }

    if (this.repository.existsByEmail(dto.getEmail())) {
      throw new ValidacaoNegocioException("E-mail já cadastrado.");
    }

    ProfissionalSaude domain = new ProfissionalSaude(
            dto.getAreaDaSaude(),
            dto.getTelefone(),
            dto.getDocProfissional(),
            dto.getEmail(),
            dto.getNome(),
            dto.getRg(),
            dto.isAtivo(),
            new Endereco(
                    dto.getEndereco().getEstado(),
                    dto.getEndereco().getCidade(),
                    dto.getEndereco().getBairro(),
                    dto.getEndereco().getRua(),
                    dto.getEndereco().getNumero(),
                    dto.getEndereco().getCep(),
                    dto.getEndereco().getComplemento()
            )
    );

    ProfissionalSaude saved = this.repository.save(domain);
    return ProfissionalSaudeMapper.toResponseDTO(saved);
  }

  public Page<ProfissionalSaudeResponseDTO> findAll(Pageable pageable) {
    return this.repository.findAll(pageable)
        .map(ProfissionalSaudeMapper::toResponseDTO);
  }

  public ProfissionalSaudeResponseDTO desativar(UUID id) {
    ProfissionalSaude existente = this.repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));
    existente.setAtivo(false);
    ProfissionalSaude saved = this.repository.update(existente);
    return ProfissionalSaudeMapper.toResponseDTO(saved);
  }

  // public void delete(UUID id) {
  //   this.repository.deleteById(id);
  // }

  public Optional<ProfissionalSaudeResponseDTO> findById(UUID id) {
    Optional<ProfissionalSaudeResponseDTO> dto = this.repository.findById(id)
            .map(ProfissionalSaudeMapper::toResponseDTO);

    if (dto.isEmpty()) {
      throw new EntidadeNaoEncontradaException("Profissional de não encontrado.");
    }
    return dto;
  }

  public ProfissionalSaudeResponseDTO update(UUID id, ProfissionalSaudeUpdateDTO dto) {
    ProfissionalSaude existente = this.repository.findById(id)
        .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional não encontrado"));

    Optional.ofNullable(dto.getAreaDaSaude()).ifPresent(existente::setAreaDaSaude);
    Optional.ofNullable(dto.getDocProfissional()).ifPresent(existente::setDocProfissional);
    Optional.ofNullable(dto.getNome()).ifPresent(existente::setNome);
    Optional.ofNullable(dto.getEmail()).ifPresent(existente::setEmail);
    Optional.ofNullable(dto.getTelefone()).ifPresent(existente::setTelefone);
    Optional.ofNullable(dto.getRg()).ifPresent(existente::setRg);


    Optional.ofNullable(dto.getEndereco().getEstado()).ifPresent(existente.getEndereco()::setEstado);
    Optional.ofNullable(dto.getEndereco().getCidade()).ifPresent(existente.getEndereco()::setCidade);
    Optional.ofNullable(dto.getEndereco().getBairro()).ifPresent(existente.getEndereco()::setBairro);
    Optional.ofNullable(dto.getEndereco().getRua()).ifPresent(existente.getEndereco()::setRua);
    Optional.ofNullable(dto.getEndereco().getNumero()).ifPresent(existente.getEndereco()::setNumero);
    Optional.ofNullable(dto.getEndereco().getCep()).ifPresent(existente.getEndereco()::setCep);
    Optional.ofNullable(dto.getEndereco().getComplemento()).ifPresent(existente.getEndereco()::setComplemento);

    ProfissionalSaude saved = this.repository.update(existente);

    return ProfissionalSaudeMapper.toResponseDTO(saved);
  }
}
