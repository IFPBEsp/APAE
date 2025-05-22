package br.org.apae.api_crud_pacientes.application.pessoa_responsavel;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;
import org.springframework.stereotype.Component;

@Component
public class PessoaResponsavelMapper implements PessoaResponsavelInterface {
    @Override
    public PessoaResponsavel toEntity(PessoaResponsavelRequest request, Pessoa pessoa) {
        PessoaResponsavel pessoa_Responsavel = new PessoaResponsavel();

        pessoa_Responsavel.setOnde_Procurar(request.getOnde_Procurar());
        pessoa_Responsavel.setVivo(request.isVivo());
        pessoa_Responsavel.setProfissao(request.getProfissao());
        pessoa_Responsavel.setRg(request.getRg());
        pessoa_Responsavel.setCpf(request.getCpf());
        pessoa_Responsavel.setEmergencia(request.getEmergencia());
        pessoa_Responsavel.setPessoa(pessoa);
        if (request.getTipoResponsavel() != null && !request.getTipoResponsavel().isEmpty()) {
            pessoa_Responsavel.setTipoResponsavel(
                PessoaResponsavel.tipo_responsavel.valueOf(request.getTipoResponsavel())
            );
        } else {
            pessoa_Responsavel.setTipoResponsavel(null);
        }

        return pessoa_Responsavel;
    }

    @Override
    public PessoaResponsavelResponse toResponse(PessoaResponsavel request) {
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
