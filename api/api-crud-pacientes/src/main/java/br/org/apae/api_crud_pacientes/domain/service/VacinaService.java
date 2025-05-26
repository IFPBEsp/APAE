
package br.org.apae.api_crud_pacientes.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.api_crud_pacientes.api.dtos.request.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.VacinaResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.VacinaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.VacinaMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaRepositoryJpa;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.VacinaRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VacinaService {

    private final VacinaRepositoryJpa vacinaRepository;
    private final PessoaRepositoryJpa pessoaRepository;
    private final VacinaMapper vacinaMapper;
    private final PessoaService pessoaService;

    public VacinaService(VacinaRepositoryJpa vacinaRepository, PessoaRepositoryJpa pessoaRepository, VacinaMapper vacinaMapper, PessoaService pessoaService) {
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

        if(vacinaOptional.isEmpty()) {
            throw new EntityNotFoundException("Vacina não encontrada.");
        }

        return vacinaMapper.toResponse(vacinaOptional.get());

    }

    public List<VacinaResponse> getAll(){
        List<VacinaEntity> vacinas = vacinaRepository.findAll();
        List<VacinaResponse> responses = new ArrayList<>();

        for(VacinaEntity vacina : vacinas) {
            responses.add(vacinaMapper.toResponse(vacina));
        }

        return responses;
    }

    public VacinaResponse update(UUID id, VacinaRequest vacinaRequest) {
        Optional<VacinaEntity> vacinaOptional = vacinaRepository.findById(id);
        Optional<PessoaEntity> pessoaOptional = pessoaRepository.findById(vacinaRequest.getPessoaId());

        if(pessoaOptional.isEmpty() || vacinaOptional.isEmpty()){
            throw new EntityNotFoundException("Vacina não encontrada.");
        }

        VacinaEntity vacina = vacinaOptional.get();

        vacina.setNome(vacinaRequest.getNome());
        vacina.setDataAplicacao(vacinaRequest.getDataAplicacao());
        vacina.setPessoa(pessoaOptional.get());

        VacinaEntity vacinaAtualizada = vacinaRepository.save(vacina);
        return vacinaMapper.toResponse(vacinaAtualizada);

    }

    public void delete(UUID id){
        if(!vacinaRepository.existsById(id)) {
            throw new EntityNotFoundException("Vacina não existe.");
        }
        vacinaRepository.deleteById(id);
    }
}
