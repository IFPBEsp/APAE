package br.org.apae.api_crud_pacientes.domain.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.api_crud_pacientes.api.dtos.request.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.TipoDeficienciaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.TipoDeficienciaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.TipoDeficienciaRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TipoDeficienciaService {
    private final TipoDeficienciaRepositoryJpa tipoDeficienciaRepository;
    private final TipoDeficienciaMapper tipoDeficienciaMapper;
    private final PessoaService pessoaService;

    public TipoDeficienciaService(TipoDeficienciaRepositoryJpa tipoDeficienciaRepository, TipoDeficienciaMapper tipoDeficienciaMapper, PessoaService pessoaService) {
        this.tipoDeficienciaRepository = tipoDeficienciaRepository;
        this.tipoDeficienciaMapper = tipoDeficienciaMapper;
        this.pessoaService = pessoaService;
    }

    public TipoDeficienciaResponse getById(UUID id) {
        Optional<TipoDeficienciaEntity> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        if (optionalTipoDeficiencia.isEmpty()) {
            throw new EntityNotFoundException("Tipo de Deficiência não encontrado");
        }

        TipoDeficienciaEntity tipoDeficiencia = optionalTipoDeficiencia.get();
        return tipoDeficienciaMapper.toResponse(tipoDeficiencia);
    }

    public TipoDeficienciaResponse create(TipoDeficienciaRequest request) {
        PessoaEntity pessoaExistente = pessoaService.getById(request.getPessoaId());
        TipoDeficienciaEntity tipoDeficiencia = tipoDeficienciaMapper.toEntity(request, pessoaExistente);
        return tipoDeficienciaMapper.toResponse(tipoDeficienciaRepository.save(tipoDeficiencia));
    }

    public Page<TipoDeficienciaResponse> getAll(Pageable pageable, String descricao) {

        if (descricao != null) {
            return tipoDeficienciaRepository.findByDescricaoContainingIgnoreCase(descricao, pageable)
                    .map(tipoDeficienciaMapper::toResponse);
        } else {
            return tipoDeficienciaRepository.findAll(pageable)
                    .map(tipoDeficienciaMapper::toResponse);
        }
    }

    public TipoDeficienciaResponse update(UUID id, TipoDeficienciaRequest request) {
        Optional<TipoDeficienciaEntity> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        TipoDeficienciaEntity tipoDeficienciaExistente;

        if (optionalTipoDeficiencia.isPresent()) {
            tipoDeficienciaExistente = optionalTipoDeficiencia.get();

            // Atualiza os campos necessários
            tipoDeficienciaExistente.setDescricao(request.getDescricao());

            TipoDeficienciaEntity tipoDeficienciaAtualizado = tipoDeficienciaRepository.save(tipoDeficienciaExistente);
            return tipoDeficienciaMapper.toResponse(tipoDeficienciaAtualizado);

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
