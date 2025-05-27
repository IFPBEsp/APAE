package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.request.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.domain.repository.ContatoRepository;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.ContatoMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContatoService {
  private final ContatoRepository contatoRepository;
  private final ContatoMapper mapper;
  private final PessoaService pessoaService;

  public ContatoService(
      ContatoRepository contatoRepository, ContatoMapper mapper, PessoaService pessoaService) {
    this.contatoRepository = contatoRepository;
    this.mapper = mapper;
    this.pessoaService = pessoaService;
  }

  public ContatoResponse getById(UUID id) {
    Contato contato = contatoRepository.findById(id);
    if (contato == null) {
      throw new EntityNotFoundException("Contato não encontrado");
    }
    return mapper.toResponseFromDomain(contato);
  }

  public ContatoResponse create(ContatoRequest request) {
    var pessoa = pessoaService.getById(request.getPessoaId());
    Contato contato = mapper.toDomain(mapper.toEntity(request, pessoa));
    Contato saved = contatoRepository.save(contato);
    return mapper.toResponseFromDomain(saved);
  }

  public Page<ContatoResponse> getAll(Pageable pageable, String endereco) {
    Page<Contato> contatos;
    if (endereco != null && !endereco.isBlank()) {
      contatos = contatoRepository.findByEnderecoIgnoreCase(endereco, pageable);
    } else {
      contatos = contatoRepository.findAll(pageable);
    }
    return contatos.map(mapper::toResponseFromDomain);
  }

  public ContatoResponse update(UUID id, ContatoRequest request) {
    Contato contatoExistente = contatoRepository.findById(id);
    if (contatoExistente == null) {
      throw new EntityNotFoundException("Contato não encontrado");
    }

    contatoExistente.setEnderecoAtivo(request.getEnderecoAtivo());
    contatoExistente.setComprovanteResidencia(request.getComprovanteResidencia());
    contatoExistente.setEndereco(request.getEndereco());
    contatoExistente.setBairro(request.getBairro());
    contatoExistente.setCidade(request.getCidade());
    contatoExistente.setEstado(request.getEstado());
    contatoExistente.setCep(request.getCep());
    contatoExistente.setNaturalidade(request.getNaturalidade());

    Contato contatoAtualizado = contatoRepository.save(contatoExistente);
    return mapper.toResponseFromDomain(contatoAtualizado);
  }

  public void delete(UUID id) {
    Contato contato = contatoRepository.findById(id);
    if (contato == null) {
      throw new EntityNotFoundException("Contato não encontrado.");
    }
    contatoRepository.deleteById(id);
  }
}
