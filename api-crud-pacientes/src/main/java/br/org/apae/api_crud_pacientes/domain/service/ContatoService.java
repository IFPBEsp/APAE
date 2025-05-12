package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoResponse;
import br.org.apae.api_crud_pacientes.application.contato.ContatoMapper;
import br.org.apae.api_crud_pacientes.domain.model.Contato;
import br.org.apae.api_crud_pacientes.domain.repository.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContatoService {
    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public ContatoResponse getById(UUID id) {
        Optional<Contato> optionalContato = contatoRepository.findById(id);
        if (optionalContato.isEmpty()) {
            throw new EntityNotFoundException("Contato não encontrado");
        }

        Contato contato = optionalContato.get();
        return new ContatoMapper().toResponse(contato);
    }

    public ContatoResponse create(ContatoRequest request) {
        ContatoMapper mapper = new ContatoMapper();
        Contato contato = mapper.toEntity(request);
        Contato contatoSalvo = contatoRepository.save(contato);
        return mapper.toResponse(contatoSalvo);
    }

    public Page<ContatoResponse> getAll(Pageable pageable, String endereco) {
        ContatoMapper mapper = new ContatoMapper();

        if (endereco != null) {
            return contatoRepository.findByEnderecoIgnoreCase(endereco, pageable)
                    .map(mapper::toResponse);
        } else {
            return contatoRepository.findAll(pageable)
                    .map(mapper::toResponse);
        }
    }

    public ContatoResponse update(UUID id, ContatoRequest request) {
        Optional<Contato> optionalContato = contatoRepository.findById(id);
        Contato contatoExistente;

        if (optionalContato.isPresent()) {
            contatoExistente = optionalContato.get();

            // Atualiza os campos necessários
            contatoExistente.setEndereco_ativo(request.getEndereco_ativo());
            contatoExistente.setComprovante_residencia(request.getComprovante_residencia());
            contatoExistente.setEndereco(request.getEndereco());
            contatoExistente.setBairro(request.getBairro());
            contatoExistente.setCidade(request.getCidade());
            contatoExistente.setEstado(request.getEstado());
            contatoExistente.setCep(request.getCep());
            contatoExistente.setNaturalidade(request.getNaturalidade());

            Contato contatoAtualizado = contatoRepository.save(contatoExistente);
            return new ContatoMapper().toResponse(contatoAtualizado);

        } else {
            throw new EntityNotFoundException("Contato não encontrado");
        }
    }

    public void delete(UUID id) {
        if (!contatoRepository.existsById(id)) {
            throw new EntityNotFoundException("Contato não encontrado.");
        }
        contatoRepository.deleteById(id);
    }
}
