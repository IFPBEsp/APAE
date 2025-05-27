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

  public Page<PessoaResponse> getAll(Pageable pageable, String cpf, String nome) {

    if (cpf != null && nome != null) {
      return pessoaRepository
          .findByCpfContainingAndNomeCompletoContainingIgnoreCase(cpf, nome, pageable)
          .map(pessoaMapper::toResponse);
    } else if (cpf != null) {
      return pessoaRepository.findByCpfContaining(cpf, pageable).map(pessoaMapper::toResponse);
    } else if (nome != null) {
      return pessoaRepository
          .findByNomeCompletoContainingIgnoreCase(nome, pageable)
          .map(pessoaMapper::toResponse);
    } else {
      return pessoaRepository.findAll(pageable).map(pessoaMapper::toResponse);
    }
  }

  public PessoaResponse update(UUID id, PessoaRequest request) {
    Optional<PessoaEntity> optionalPessoa = pessoaRepository.findById(id);
    PessoaEntity pessoaExistente;

    if (optionalPessoa.isPresent()) {
      pessoaExistente = optionalPessoa.get();

      // Atualiza os campos necessários
      pessoaExistente.setNomeCompleto(request.getNomeCompleto());
      pessoaExistente.setCpf(request.getCpf());
      pessoaExistente.setDataNascimento(request.getDataNascimento());
      pessoaExistente.setNumRegistroNasc(request.getNumRegistroNasc());
      pessoaExistente.setFls(request.getFls());
      pessoaExistente.setLivro(request.getLivro());
      pessoaExistente.setCartorio(request.getCartorio());
      pessoaExistente.setRg(request.getRg());
      pessoaExistente.setDataEmissaoRg(request.getDataEmissaoRg());
      pessoaExistente.setOrgaoEmissorRg(request.getOrgaoEmissorRg());
      pessoaExistente.setCns(request.getCns());
      pessoaExistente.setNis(request.getNis());
      pessoaExistente.setDataCadastramento(request.getDataCadastramento());

      PessoaEntity pessoaAtualizada = pessoaRepository.save(pessoaExistente);
      return pessoaMapper.toResponse(pessoaAtualizada);

    } else {
      throw new EntityNotFoundException("Pessoa não encontrada");
    }
  }

  public void delete(UUID id) {
    if (!pessoaRepository.existsById(id)) {
      throw new EntityNotFoundException("Pessoa não encontrada.");
    }
    pessoaRepository.deleteById(id);
  }
}
