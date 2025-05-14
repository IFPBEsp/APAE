package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaResponse;
import br.org.apae.api_crud_pacientes.application.vacina.VacinaMapperInterface;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.Vacina;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaRepository;
import br.org.apae.api_crud_pacientes.domain.repository.VacinaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VacinaService {

    private VacinaRepository vacinaRepository;
    private PessoaRepository pessoaRepository;
    private VacinaMapperInterface vacinaMapper;

    public VacinaService(VacinaRepository vacinaRepository, PessoaRepository pessoaRepository, VacinaMapperInterface vacinaMapper) {
        this.vacinaRepository = vacinaRepository;
        this.pessoaRepository = pessoaRepository;
        this.vacinaMapper = vacinaMapper;
    }

    public Vacina create(VacinaRequest vacinaRequest, Pessoa pessoa) {
        Vacina vacina = vacinaMapper.toEntity(vacinaRequest, pessoa);
        return vacinaRepository.save(vacina);
    }

    public VacinaResponse getById(UUID id) {
        Optional<Vacina> vacinaOptional = vacinaRepository.findById(id);

        if(vacinaOptional.isEmpty()) {
            throw new EntityNotFoundException("Vacina não encontrada.");
        }

        return vacinaMapper.toResponse(vacinaOptional.get());

    }

    public List<VacinaResponse> getAll(){
        List<Vacina> vacinas = vacinaRepository.findAll();
        List<VacinaResponse> responses = new ArrayList<>();

        for(Vacina vacina : vacinas) {
            responses.add(vacinaMapper.toResponse(vacina));
        }

        return responses;
    }

    public VacinaResponse update(UUID id, VacinaRequest vacinaRequest) {
        Optional<Vacina> vacinaOptional = vacinaRepository.findById(id);
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(vacinaRequest.getPessoaId());

        if(pessoaOptional.isEmpty() || vacinaOptional.isEmpty()){
            throw new EntityNotFoundException("Vacina não encontrada.");
        }

        Vacina vacina = vacinaOptional.get();

        vacina.setNome(vacinaRequest.getNome());
        vacina.setDataAplicacao(vacinaRequest.getDataAplicacao());
        vacina.setPessoa(pessoaOptional.get());

        Vacina vacinaAtualizada = vacinaRepository.save(vacina);
        return vacinaMapper.toResponse(vacinaAtualizada);

    }

    public void delete(UUID id){
        if(!vacinaRepository.existsById(id)) {
            throw new EntityNotFoundException("Vacina não existe.");
        }
        vacinaRepository.deleteById(id);
    }
}
