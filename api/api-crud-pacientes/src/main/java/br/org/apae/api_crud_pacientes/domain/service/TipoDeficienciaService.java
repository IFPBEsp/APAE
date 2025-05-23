package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.application.tipo_deficiencia.TipoDeficienciaMapper;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
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
    private final TipoDeficienciaMapper tipoDeficienciaMapper;
    private final PessoaService pessoaService;

    public TipoDeficienciaService(TipoDeficienciaRepository tipoDeficienciaRepository, TipoDeficienciaMapper tipoDeficienciaMapper, PessoaService pessoaService) {
        this.tipoDeficienciaRepository = tipoDeficienciaRepository;
        this.tipoDeficienciaMapper = tipoDeficienciaMapper;
        this.pessoaService = pessoaService;
    }

    public TipoDeficienciaResponse getById(UUID id) {
        Optional<TipoDeficiencia> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        if (optionalTipoDeficiencia.isEmpty()) {
            throw new EntityNotFoundException("Tipo de Deficiência não encontrado");
        }

        TipoDeficiencia tipoDeficiencia = optionalTipoDeficiencia.get();
        return tipoDeficienciaMapper.toResponse(tipoDeficiencia);
    }

    public TipoDeficienciaResponse create(TipoDeficienciaRequest request) {
        Pessoa pessoaExistente = pessoaService.getById(request.getPessoaId());
        TipoDeficiencia tipoDeficiencia = tipoDeficienciaMapper.toEntity(request, pessoaExistente);
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
        Optional<TipoDeficiencia> optionalTipoDeficiencia = tipoDeficienciaRepository.findById(id);
        TipoDeficiencia tipoDeficienciaExistente;

        if (optionalTipoDeficiencia.isPresent()) {
            tipoDeficienciaExistente = optionalTipoDeficiencia.get();

            // Atualiza os campos necessários
            tipoDeficienciaExistente.setDescricao(request.getDescricao());

            TipoDeficiencia tipoDeficienciaAtualizado = tipoDeficienciaRepository.save(tipoDeficienciaExistente);
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
