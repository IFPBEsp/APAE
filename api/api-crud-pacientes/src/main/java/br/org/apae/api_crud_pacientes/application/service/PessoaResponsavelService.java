package br.org.apae.api_crud_pacientes.application.service;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaResponsavelEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.PessoaResponsavelMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaResponsavelRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PessoaResponsavelService {
  private final PessoaResponsavelRepositoryJpa repository;
  private final PessoaResponsavelMapper pessoaResponsavelMapper;
  private final PessoaService pessoaService;

  public PessoaResponsavelService(
      PessoaResponsavelRepositoryJpa repository,
      PessoaResponsavelMapper pessoaResponsavelMapper,
      PessoaService pessoaService) {
    this.repository = repository;
    this.pessoaResponsavelMapper = pessoaResponsavelMapper;
    this.pessoaService = pessoaService;
  }

  public PessoaResponsavelResponse create(PessoaResponsavelRequest pessoaReponsavelRequest) {
    PessoaEntity pessoaExistente = pessoaService.getById(pessoaReponsavelRequest.getPessoaId());
    PessoaResponsavelEntity pessoaResponsavel =
        pessoaResponsavelMapper.toEntity(pessoaReponsavelRequest, pessoaExistente);
    return pessoaResponsavelMapper.toResponse(repository.save(pessoaResponsavel));
  }

  public PessoaResponsavelResponse update(UUID id, PessoaResponsavelRequest request) {
    PessoaResponsavelEntity pessoaResponsavelExistente = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pessoa Responsável não encontrada."));

    atualizarCamposPessoaResponsavel(pessoaResponsavelExistente, request);

    PessoaResponsavelEntity pessoaResponsavelAtualizada = repository.save(pessoaResponsavelExistente);
    return pessoaResponsavelMapper.toResponse(pessoaResponsavelAtualizada);
  }

  private void atualizarCamposPessoaResponsavel(PessoaResponsavelEntity entity, PessoaResponsavelRequest request) {
    entity.setCpf(request.getCpf());
    entity.setEmergencia(request.getEmergencia());
    entity.setOndeProcurar(request.getOndeProcurar());
    entity.setProfissao(request.getProfissao());
    entity.setRg(request.getRg());
    entity.setVivo(request.isVivo());
  }

  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw new EntityNotFoundException();
    }
    repository.deleteById(id);
  }

  public PessoaResponsavelResponse getById(UUID id) {
    Optional<PessoaResponsavelEntity> optionalPessoaResponsavel = repository.findById(id);
    if (optionalPessoaResponsavel.isEmpty()) {
      throw new EntityNotFoundException("Pessoa responsável não encontrada.");
    }
    PessoaResponsavelEntity pessoaResponsavel = optionalPessoaResponsavel.get();
    return pessoaResponsavelMapper.toResponse(pessoaResponsavel);
  }

  public Page<PessoaResponsavelResponse> getAll(Pageable pageable, String cpf) {
    if (cpf != null) {
      return repository.findByCpfContaining(cpf, pageable).map(pessoaResponsavelMapper::toResponse);
    }
    // Mais conficionais com mais atríbutos relevantes
    return repository.findByCpfContaining(cpf, pageable).map(pessoaResponsavelMapper::toResponse);
  }
}
