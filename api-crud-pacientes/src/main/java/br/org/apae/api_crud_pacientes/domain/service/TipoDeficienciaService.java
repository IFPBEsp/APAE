package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.application.tipo_deficiencia.TipoDeficienciaMapper;
import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;
import br.org.apae.api_crud_pacientes.domain.repository.TipoDeficienciaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TipoDeficienciaService {
    private final TipoDeficienciaRepository tipoDeficienciaRepository;

    public TipoDeficienciaService(TipoDeficienciaRepository tipoDeficienciaRepository) {
        this.tipoDeficienciaRepository = tipoDeficienciaRepository;
    }

    public TipoDeficienciaResponse getById(UUID id) {
        Optional<TipoDeficiencia> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        if (optionalTipoDeficiencia.isEmpty()) {
            throw new EntityNotFoundException("Tipo de Deficiência não encontrado");
        }

        TipoDeficiencia tipoDeficiencia = optionalTipoDeficiencia.get();
        return new TipoDeficienciaMapper().toResponse(tipoDeficiencia);
    }

    public TipoDeficienciaResponse create(TipoDeficienciaRequest request) {
        TipoDeficienciaMapper mapper = new TipoDeficienciaMapper();
        TipoDeficiencia tipoDeficiencia = mapper.toEntity(request);
        TipoDeficiencia tipoDeficienciaSalvo = tipoDeficienciaRepository.save(tipoDeficiencia);
        return mapper.toResponse(tipoDeficienciaSalvo);
    }

    public Page<TipoDeficienciaResponse> getAll(Pageable pageable, String descricao) {
        TipoDeficienciaMapper mapper = new TipoDeficienciaMapper();

        if (descricao != null) {
            return tipoDeficienciaRepository.findByDescricaoContainingIgnoreCase(descricao, pageable)
                    .map(mapper::toResponse);
        } else {
            return tipoDeficienciaRepository.findAll(pageable)
                    .map(mapper::toResponse);
        }
    }

    public TipoDeficienciaResponse update(UUID id, TipoDeficienciaRequest request) {
        Optional<TipoDeficiencia> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        TipoDeficiencia tipoDeficienciaExistente;

        if (optionalTipoDeficiencia.isPresent()) {
            tipoDeficienciaExistente = optionalTipoDeficiencia.get();

            // Atualiza os campos necessários
            tipoDeficienciaExistente.setDescricao(request.getDescricao());

            TipoDeficiencia tipoDeficienciaAtualizado = tipoDeficienciaRepository.save(tipoDeficienciaExistente);
            return new TipoDeficienciaMapper().toResponse(tipoDeficienciaAtualizado);

        } else {
            throw new EntityNotFoundException("Tipo de Deficiência não encontrado");
        }
    }

    public void delete(UUID id) {
        if (!tipoDeficienciaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de Deficiência não encontrado.");
        }
        tipoDeficienciaRepository.deleteById(id);
    }
}
