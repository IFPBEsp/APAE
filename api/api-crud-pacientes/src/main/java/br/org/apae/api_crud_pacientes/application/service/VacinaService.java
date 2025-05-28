package br.org.apae.api_crud_pacientes.application.service;

import br.org.apae.api_crud_pacientes.api.dtos.request.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.VacinaResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.VacinaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.VacinaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaRepositoryJpa;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.VacinaRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class VacinaService {

  private final VacinaRepositoryJpa vacinaRepository;
  private final PessoaRepositoryJpa pessoaRepository;
  private final VacinaMapper vacinaMapper;
  private final PessoaService pessoaService;

  public VacinaService(
      VacinaRepositoryJpa vacinaRepository,
      PessoaRepositoryJpa pessoaRepository,
      VacinaMapper vacinaMapper,
      PessoaService pessoaService) {
    this.vacinaRepository = vacinaRepository;
    this.pessoaRepository = pessoaRepository;
    this.vacinaMapper = vacinaMapper;
    this.pessoaService = pessoaService;
  }

  public VacinaResponse create(VacinaRequest vacinaRequest) {
    PessoaEntity pessoaExistente = pessoaService.getById(vacinaRequest.getPessoaId());
    VacinaEntity vacina = vacinaMapper.toEntity(vacinaRequest, pessoaExistente);
    return vacinaMapper.toResponse(vacinaRepository.save(vacina));
  }

  public VacinaResponse getById(UUID id) {
    Optional<VacinaEntity> vacinaOptional = vacinaRepository.findById(id);

    if (vacinaOptional.isEmpty()) {
      throw new EntityNotFoundException("Vacina não encontrada.");
    }

    return vacinaMapper.toResponse(vacinaOptional.get());
  }

  public List<VacinaResponse> getAll() {
    List<VacinaEntity> vacinas = vacinaRepository.findAll();
    List<VacinaResponse> responses = new ArrayList<>();

    for (VacinaEntity vacina : vacinas) {
      responses.add(vacinaMapper.toResponse(vacina));
    }

    return responses;
  }

  public VacinaResponse update(UUID id, VacinaRequest vacinaRequest) {
    VacinaEntity vacina = vacinaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vacina não encontrada."));
    PessoaEntity pessoa = pessoaRepository.findById(vacinaRequest.getPessoaId())
        .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada."));

    atualizarCamposVacina(vacina, vacinaRequest, pessoa);

    VacinaEntity vacinaAtualizada = vacinaRepository.save(vacina);
    return vacinaMapper.toResponse(vacinaAtualizada);
  }

  private void atualizarCamposVacina(VacinaEntity vacina, VacinaRequest request, PessoaEntity pessoa) {
    vacina.setNome(request.getNome());
    vacina.setDataAplicacao(request.getDataAplicacao());
    vacina.setPessoa(pessoa);
  }

  public void delete(UUID id) {
    if (!vacinaRepository.existsById(id)) {
      throw new EntityNotFoundException("Vacina não existe.");
    }
    vacinaRepository.deleteById(id);
  }
}
