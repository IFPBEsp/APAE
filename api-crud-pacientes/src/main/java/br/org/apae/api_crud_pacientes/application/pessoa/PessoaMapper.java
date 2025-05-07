package br.org.apae.api_crud_pacientes.application.pessoa;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public class PessoaMapper implements PessoaMapperInterface {
    @Override
    public Pessoa toEntity(PessoaRequest request) {
        Pessoa pessoa = new Pessoa();

        pessoa.setNome_completo(request.getNome_completo());
        pessoa.setData_nascimento(request.getData_nascimento());
        pessoa.setNum_registro_nasc(request.getNum_registro_nasc());
        pessoa.setFls(request.getFls());
        pessoa.setLivro(request.getLivro());
        pessoa.setCartorio(request.getCartorio());
        pessoa.setCpf(request.getCpf());
        pessoa.setRg(request.getRg());
        pessoa.setData_emissao_rg(request.getData_emissao_rg());
        pessoa.setOrgao_emissor_rg(request.getOrgao_emissor_rg());
        pessoa.setCns(request.getCns());
        pessoa.setNis(request.getNis());
        pessoa.setData_cadastramento(request.getData_cadastramento());

        return pessoa;
    }

    @Override
    public PessoaResponse toResponse(Pessoa pessoa) {
        PessoaResponse response = new PessoaResponse();

        response.setId(pessoa.getId());
        response.setNome_completo(pessoa.getNome_completo());
        response.setData_nascimento(pessoa.getData_nascimento());
        response.setNum_registro_nasc(pessoa.getNum_registro_nasc());
        response.setFls(pessoa.getFls());
        response.setLivro(pessoa.getLivro());
        response.setCartorio(pessoa.getCartorio());
        response.setCpf(pessoa.getCpf());
        response.setRg(pessoa.getRg());
        response.setData_emissao_rg(pessoa.getData_emissao_rg());
        response.setOrgao_emissor_rg(pessoa.getOrgao_emissor_rg());
        response.setCns(pessoa.getCns());
        response.setNis(pessoa.getNis());
        response.setData_cadastramento(pessoa.getData_cadastramento());

        return response;
    }
}
