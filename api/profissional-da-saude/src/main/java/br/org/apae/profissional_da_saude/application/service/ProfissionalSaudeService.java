package br.org.apae.profissional_da_saude.application.service;

import br.org.apae.profissional_da_saude.api.dto.DisponibilidadeDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.domain.exception.EntidadeNaoEncontradaException;
import br.org.apae.profissional_da_saude.domain.exception.ValidacaoNegocioException;
import br.org.apae.profissional_da_saude.domain.model.Endereco;
import br.org.apae.profissional_da_saude.domain.model.ProfissionalSaude;
import br.org.apae.profissional_da_saude.domain.repository.DisponibilidadeRepository;
import br.org.apae.profissional_da_saude.domain.repository.ProfissionalSaudeRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.DisponibilidadeEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.ProfissionalSaudeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository repository;
    private final DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    public ProfissionalSaudeService(ProfissionalSaudeRepository repository,
                                    DisponibilidadeRepository disponibilidadeRepository) {
        this.repository = repository;
        this.disponibilidadeRepository = disponibilidadeRepository;
    }

    public ProfissionalSaudeResponseDTO save(ProfissionalSaudeCreateDTO dto) {
        if (this.repository.existsByDocProfissional(dto.getDocProfissional())) {
            throw new ValidacaoNegocioException("Documento profissional já cadastrado.");
        }

        if (this.repository.existsByEmail(dto.getEmail())) {
            throw new ValidacaoNegocioException("E-mail já cadastrado.");
        }

        // Validar duplicidade de disponibilidades se fornecidas
        if (dto.getDisponibilidades() != null) {
            validarDuplicidadeDisponibilidades(dto.getDisponibilidades());
        }

        ProfissionalSaude domain = ProfissionalSaudeMapper.toDomain(dto);

        // Salvar profissional primeiro para gerar ID
        ProfissionalSaude saved = this.repository.save(domain);

        // Salvar disponibilidades se existirem
        if (dto.getDisponibilidades() != null && !dto.getDisponibilidades().isEmpty()) {
            List<DisponibilidadeEntity> disponibilidadesSalvas = new ArrayList<>();

            for (DisponibilidadeDTO dispDto : dto.getDisponibilidades()) {
                DisponibilidadeEntity disponibilidade = new DisponibilidadeEntity(
                        dispDto.getDia(),
                        dispDto.getTurno(),
                        saved.getId()
                );
                // Salvar diretamente no repositório
                DisponibilidadeEntity disponibilidadeSalva = disponibilidadeRepository.save(disponibilidade);
                disponibilidadesSalvas.add(disponibilidadeSalva);
            }

            // Atualizar a lista no objeto domain
            saved.setDisponibilidades(disponibilidadesSalvas);
        }

        return ProfissionalSaudeMapper.toResponseDTO(saved);
    }

    ProfissionalSaude domain = new ProfissionalSaude(
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
                    dto.getEndereco().getComplemento()
            )
    );

    ProfissionalSaude saved = this.repository.save(domain);
    return ProfissionalSaudeMapper.toResponseDTO(saved);
  }

    @Transactional(readOnly = true)
    public Page<ProfissionalSaudeResponseDTO> findAll(Pageable pageable) {
        return this.repository.findAll(pageable)
                .map(ProfissionalSaudeMapper::toResponseDTO);
    }

    public void delete(UUID id) {
        // Deletar disponibilidades primeiro devido à constraint de foreign key
        disponibilidadeRepository.deleteByProfissionalId(id);
        this.repository.deleteById(id);
    }

  public Optional<ProfissionalSaudeResponseDTO> findById(UUID id) {
    Optional<ProfissionalSaudeResponseDTO> dto = this.repository.findById(id)
            .map(ProfissionalSaudeMapper::toResponseDTO);

    if (dto.isEmpty()) {
      throw new EntidadeNaoEncontradaException("Profissional de não encontrado.");
    }
    return dto;
  }

        if (profissional.isPresent()) {
            ProfissionalSaude prof = profissional.get();
            // Carregar disponibilidades do banco
            List<DisponibilidadeEntity> disponibilidades = disponibilidadeRepository.findByFkProfissionalId(id);
            prof.setDisponibilidades(disponibilidades);

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

        Optional.ofNullable(dto.getNome())
                .ifPresent(existente::setNome);

        Optional.ofNullable(dto.getEmail())
                .ifPresent(existente::setEmail);

        Optional.ofNullable(dto.getTelefone())
                .ifPresent(existente::setTelefone);

        // Atualizar disponibilidades se fornecidas
        if (dto.getDisponibilidades() != null) {
            validarDuplicidadeDisponibilidades(dto.getDisponibilidades());

            // Remover disponibilidades existentes do banco
            disponibilidadeRepository.deleteByProfissionalId(id);

            // Salvar novas disponibilidades
            List<DisponibilidadeEntity> novasDisponibilidades = new ArrayList<>();
            for (DisponibilidadeDTO dispDto : dto.getDisponibilidades()) {
                DisponibilidadeEntity disponibilidade = new DisponibilidadeEntity(
                        dispDto.getDia(),
                        dispDto.getTurno(),
                        id
                );
                DisponibilidadeEntity disponibilidadeSalva = disponibilidadeRepository.save(disponibilidade);
                novasDisponibilidades.add(disponibilidadeSalva);
            }

            // Atualizar a lista no objeto domain
            existente.setDisponibilidades(novasDisponibilidades);
        }

        ProfissionalSaude saved = this.repository.save(existente);

        return ProfissionalSaudeMapper.toResponseDTO(saved);
    }

    private void validarDuplicidadeDisponibilidades(List<DisponibilidadeDTO> disponibilidades) {
        Set<String> combinacoes = new HashSet<>();

        for (DisponibilidadeDTO disp : disponibilidades) {
            String combinacao = disp.getDia().name() + "-" + disp.getTurno().name();

            if (combinacoes.contains(combinacao)) {
                throw new ValidacaoNegocioException(
                        String.format("Disponibilidade duplicada encontrada: %s - %s",
                                disp.getDia().getValor(),
                                disp.getTurno().getValor())
                );
            }
            combinacoes.add(combinacao);
        }
    }
}