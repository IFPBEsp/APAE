package br.org.apae.api.profissional.da.saude.services;

import br.org.apae.api.common.model.Endereco;
import br.org.apae.api.profissional.da.saude.domain.model.ProfissionalSaude;
import br.org.apae.api.profissional.da.saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.api.profissional.da.saude.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.api.profissional.da.saude.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.api.profissional.da.saude.dto.ProfissionalSaudeUpdateDTO;
import br.org.apae.api.profissional.da.saude.infra.entity.ProfissionalSaudeEntity;
import br.org.apae.api.profissional.da.saude.domain.exceptions.EntidadeNaoEncontradaException;
import br.org.apae.api.profissional.da.saude.domain.exceptions.ValidacaoNegocioException;
import br.org.apae.api.profissional.da.saude.infra.mapper.ProfissionalSaudeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository repository;
    private final ProfissionalSaudeMapper mapper;

    @Autowired
    public ProfissionalSaudeService(ProfissionalSaudeRepository repository, ProfissionalSaudeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProfissionalSaudeResponseDTO save(ProfissionalSaudeCreateDTO dto) {
        if (this.repository.existsByDocProfissional(dto.getDocProfissional())) {
            throw new ValidacaoNegocioException("Documento profissional já cadastrado.");
        }

        if (this.repository.existsByEmail(dto.getEmail())) {
            throw new ValidacaoNegocioException("E-mail já cadastrado.");
        }

        ProfissionalSaude domainModel = mapper.toDomain(dto);

        ProfissionalSaudeEntity entityToSave = mapper.toEntity(domainModel);

        ProfissionalSaudeEntity savedEntity = this.repository.save(entityToSave);

        ProfissionalSaude savedModel = mapper.toModel(savedEntity);

        return mapper.toResponseDTO(savedModel);
    }

    public Page<ProfissionalSaudeResponseDTO> findAll(Pageable pageable) {
        Page<ProfissionalSaudeEntity> entityPage = this.repository.findAll(pageable);

        return entityPage.map(entity -> {
            ProfissionalSaude model = mapper.toModel(entity);
            return mapper.toResponseDTO(model);
        });
    }

    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    public ProfissionalSaudeResponseDTO findById(UUID id) {
        ProfissionalSaudeEntity entity = this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional de saúde não encontrado."));

        ProfissionalSaude model = mapper.toModel(entity);
        return mapper.toResponseDTO(model);
    }

    public ProfissionalSaudeResponseDTO update(UUID id, ProfissionalSaudeUpdateDTO dto) {
        ProfissionalSaudeEntity entityExistente = this.repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Profissional de saúde não encontrado"));

        ProfissionalSaude modelExistente = mapper.toModel(entityExistente);

        Optional.ofNullable(dto.getAreaDaSaude()).ifPresent(modelExistente::setAreaDaSaude);
        Optional.ofNullable(dto.getDocProfissional()).ifPresent(modelExistente::setDocProfissional);
        Optional.ofNullable(dto.getNome()).ifPresent(modelExistente::setNome);
        Optional.ofNullable(dto.getEmail()).ifPresent(modelExistente::setEmail);
        Optional.ofNullable(dto.getTelefone()).ifPresent(modelExistente::setTelefone);
        Optional.ofNullable(dto.getRg()).ifPresent(modelExistente::setRg);

        if (dto.getEndereco() != null) {
            Endereco enderecoModel = modelExistente.getEndereco();
            Optional.ofNullable(dto.getEndereco().getEstado()).ifPresent(enderecoModel::setEstado);
            Optional.ofNullable(dto.getEndereco().getCidade()).ifPresent(enderecoModel::setCidade);
            Optional.ofNullable(dto.getEndereco().getBairro()).ifPresent(enderecoModel::setBairro);
            Optional.ofNullable(dto.getEndereco().getRua()).ifPresent(enderecoModel::setRua);
            Optional.ofNullable(dto.getEndereco().getNumero()).ifPresent(enderecoModel::setNumero);
            Optional.ofNullable(dto.getEndereco().getCep()).ifPresent(enderecoModel::setCep);
            Optional.ofNullable(dto.getEndereco().getComplemento()).ifPresent(enderecoModel::setComplemento);
        }

        ProfissionalSaudeEntity entityParaSalvar = mapper.toEntity(modelExistente);

        ProfissionalSaudeEntity savedEntity = this.repository.save(entityParaSalvar);

        ProfissionalSaude savedModel = mapper.toModel(savedEntity);
        return mapper.toResponseDTO(savedModel);
    }
}