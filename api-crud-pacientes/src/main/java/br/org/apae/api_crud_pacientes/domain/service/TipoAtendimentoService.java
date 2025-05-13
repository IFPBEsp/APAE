package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.application.tipo_atendimento.TipoAtendimentoMapper;
import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;
import br.org.apae.api_crud_pacientes.domain.repository.TipoAtendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TipoAtendimentoService {
    private final TipoAtendimentoRepository tipoAtendimentoRepository;

    public TipoAtendimentoService(TipoAtendimentoRepository tipoAtendimentoRepository) {
        this.tipoAtendimentoRepository = tipoAtendimentoRepository;
    }

    public TipoAtendimentoResponse getById(UUID id) {
        Optional<TipoAtendimento> optionalTipoAtendimento = tipoAtendimentoRepository.findById(id);
        if (optionalTipoAtendimento.isEmpty()) {
            throw new EntityNotFoundException("Tipo de Atendimento não encontrado");
        }

        TipoAtendimento tipoAtendimento = optionalTipoAtendimento.get();
        return new TipoAtendimentoMapper().toResponse(tipoAtendimento);
    }

    public TipoAtendimentoResponse create(TipoAtendimentoRequest request) {
        TipoAtendimentoMapper mapper = new TipoAtendimentoMapper();
        TipoAtendimento tipoAtendimento = mapper.toEntity(request);
        TipoAtendimento tipoAtendimentoSalvo = tipoAtendimentoRepository.save(tipoAtendimento);
        return mapper.toResponse(tipoAtendimentoSalvo);
    }

    public Page<TipoAtendimentoResponse> getAll(Pageable pageable, String descricao) {
        TipoAtendimentoMapper mapper = new TipoAtendimentoMapper();

        if (descricao != null) {
            return tipoAtendimentoRepository.findByDescricaoContainingIgnoreCase(descricao, pageable)
                    .map(mapper::toResponse);
        } else {
            return tipoAtendimentoRepository.findAll(pageable)
                    .map(mapper::toResponse);
        }
    }

    public TipoAtendimentoResponse update(UUID id, TipoAtendimentoRequest request) {
        Optional<TipoAtendimento> optionalTipoAtendimento = tipoAtendimentoRepository.findById(id);
        TipoAtendimento tipoAtendimentoExistente;

        if (optionalTipoAtendimento.isPresent()) {
            tipoAtendimentoExistente = optionalTipoAtendimento.get();

            // Atualiza os campos necessários
            tipoAtendimentoExistente.setDescricao(request.getDescricao());

            TipoAtendimento tipoAtendimentoAtualizado = tipoAtendimentoRepository.save(tipoAtendimentoExistente);
            return new TipoAtendimentoMapper().toResponse(tipoAtendimentoAtualizado);

        } else {
            throw new EntityNotFoundException("Tipo de Atendimento não encontrado");
        }
    }

    public void delete(UUID id) {
        if (!tipoAtendimentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de Atendimento não encontrado.");
        }
        tipoAtendimentoRepository.deleteById(id);
    }
}