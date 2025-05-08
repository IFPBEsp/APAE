package br.org.apae.api_crud_pacientes.application.pessoa_responsavel;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa_Responsavel;

public class PessoaResponsavelMapper implements PessoaResponsavelInterface {
    @Override
    public Pessoa_Responsavel toEntity(PessoaResponsavelRequest request) {
        Pessoa_Responsavel pessoa_Responsavel = new Pessoa_Responsavel();

        pessoa_Responsavel.setOnde_Procurar(request.getOnde_Procurar());
        pessoa_Responsavel.setVivo(request.isVivo());
        pessoa_Responsavel.setProfissao(request.getProfissao());
        pessoa_Responsavel.setRg(request.getRg());
        pessoa_Responsavel.setCpf(request.getCpf());
        pessoa_Responsavel.setEmergencia(request.getEmergencia());

        return pessoa_Responsavel;
    }

    @Override
    public PessoaResponsavelResponse toResponse(Pessoa_Responsavel request) {
        PessoaResponsavelResponse response = new PessoaResponsavelResponse();

        response.setId(request.getId());
        response.setOnde_Procurar(request.getOnde_Procurar());
        response.setVivo(request.isVivo());
        response.setProfissao(request.getProfissao());
        response.setRg(request.getRg());
        response.setCpf(request.getCpf());
        response.setEmergencia(request.getEmergencia());

        return response;
    }

}
