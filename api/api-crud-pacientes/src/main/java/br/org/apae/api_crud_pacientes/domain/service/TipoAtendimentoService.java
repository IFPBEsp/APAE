package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.request.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoAtendimentoEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.TipoAtendimentoMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.TipoAtendimentoRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TipoAtendimentoService {
  private final TipoAtendimentoRepositoryJpa tipoAtendimentoRepository;
  private final TipoAtendimentoMapper tipoAtendimentoMapper;
  private final PessoaService pessoaService;

  public TipoAtendimentoService(
      TipoAtendimentoRepositoryJpa tipoAtendimentoRepository,
      TipoAtendimentoMapper tipoAtendimentoMapper,
      PessoaService pessoaService) {
    this.tipoAtendimentoRepository = tipoAtendimentoRepository;
    this.tipoAtendimentoMapper = tipoAtendimentoMapper;
    this.pessoaService = pessoaService;
  }

  public TipoAtendimentoResponse getById(UUID id) {
    Optional<TipoAtendimentoEntity> optionalTipoAtendimento = tipoAtendimentoRepository.findById(id);
    if (optionalTipoAtendimento.isEmpty()) {
      throw new EntityNotFoundException("Tipo de Atendimento não encontrado");
    }

    TipoAtendimentoEntity tipoAtendimento = optionalTipoAtendimento.get();
    return tipoAtendimentoMapper.toResponse(tipoAtendimento);
  }

  public TipoAtendimentoResponse create(TipoAtendimentoRequest request) {
    PessoaEntity pessoaExistente = pessoaService.getById(request.getPessoaId());
    TipoAtendimentoEntity tipoAtendimento = tipoAtendimentoMapper.toEntity(request, pessoaExistente);
    return tipoAtendimentoMapper.toResponse(tipoAtendimentoRepository.save(tipoAtendimento));
  }

  public Page<TipoAtendimentoResponse> getAll(Pageable pageable, String descricao) {

    if (descricao != null) {
      return tipoAtendimentoRepository
          .findByDescricaoContainingIgnoreCase(descricao, pageable)
          .map(tipoAtendimentoMapper::toResponse);
    } else {
      return tipoAtendimentoRepository.findAll(pageable).map(tipoAtendimentoMapper::toResponse);
    }
  }

  public TipoAtendimentoResponse update(UUID id, TipoAtendimentoRequest request) {
    TipoAtendimentoEntity tipoAtendimentoExistente = tipoAtendimentoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Tipo de Atendimento não encontrado"));

    atualizarCamposTipoAtendimento(tipoAtendimentoExistente, request);

    TipoAtendimentoEntity tipoAtendimentoAtualizado = tipoAtendimentoRepository.save(tipoAtendimentoExistente);
    return tipoAtendimentoMapper.toResponse(tipoAtendimentoAtualizado);
  }

  private void atualizarCamposTipoAtendimento(TipoAtendimentoEntity entity, TipoAtendimentoRequest request) {
    entity.setDescricao(request.getDescricao());
  }

  public void delete(UUID id) {
    if (!tipoAtendimentoRepository.existsById(id)) {
      throw new EntityNotFoundException("Tipo de Atendimento não encontrado.");
    }
    tipoAtendimentoRepository.deleteById(id);
  }
}
