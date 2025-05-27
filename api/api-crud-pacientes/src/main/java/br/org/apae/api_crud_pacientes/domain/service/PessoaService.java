package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.PessoaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
  private final PessoaRepositoryJpa pessoaRepository;
  private final PessoaMapper pessoaMapper;

  public PessoaService(PessoaRepositoryJpa pessoaRepository, PessoaMapper pessoaMapper) {
    this.pessoaRepository = pessoaRepository;
    this.pessoaMapper = pessoaMapper;
  }

  public PessoaEntity getById(UUID id) {
    Optional<PessoaEntity> optionalPaciente = pessoaRepository.findById(id);
    if (optionalPaciente.isEmpty()) {
      throw new EntityNotFoundException("Pessoa não encontrada");
    }

    PessoaEntity pessoa = optionalPaciente.get();
    return pessoa;
  }

  public PessoaEntity create(PessoaRequest pessoaRequest) {
    PessoaEntity pessoa = pessoaMapper.toEntity(pessoaRequest);
    return pessoaRepository.save(pessoa);
  }

  public Page<PessoaResponse> getAll(Pageable pageable) {
    return pessoaRepository.findAll(pageable).map(pessoaMapper::toResponse);
  }

  public PessoaResponse update(UUID id, PessoaRequest request) {
    PessoaEntity pessoaExistente = pessoaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

    atualizarCamposPessoa(pessoaExistente, request);

    PessoaEntity pessoaAtualizada = pessoaRepository.save(pessoaExistente);
    return pessoaMapper.toResponse(pessoaAtualizada);
  }

  private void atualizarCamposPessoa(PessoaEntity pessoa, PessoaRequest request) {
    pessoa.setNomeCompleto(request.getNomeCompleto());
    pessoa.setCpf(request.getCpf());
    pessoa.setDataNascimento(request.getDataNascimento());
    pessoa.setNumRegistroNasc(request.getNumRegistroNasc());
    pessoa.setFls(request.getFls());
    pessoa.setLivro(request.getLivro());
    pessoa.setCartorio(request.getCartorio());
    pessoa.setRg(request.getRg());
    pessoa.setDataEmissaoRg(request.getDataEmissaoRg());
    pessoa.setOrgaoEmissorRg(request.getOrgaoEmissorRg());
    pessoa.setCns(request.getCns());
    pessoa.setNis(request.getNis());
    pessoa.setDataCadastramento(request.getDataCadastramento());
  }

  public void delete(UUID id) {
    if (!pessoaRepository.existsById(id)) {
      throw new EntityNotFoundException("Pessoa não encontrada.");
    }
    pessoaRepository.deleteById(id);
  }
}
