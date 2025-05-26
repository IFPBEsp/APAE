package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import org.springframework.stereotype.Component;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponse;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.PessoaMapperInterface;

@Component
public class PessoaMapper implements PessoaMapperInterface<PessoaRequest, PessoaResponse, PessoaEntity, Pessoa> {
    @Override
    public PessoaEntity toEntity(PessoaRequest request) {
        PessoaEntity pessoaEntity = new PessoaEntity();

        pessoaEntity.setNomeCompleto(request.getNome_completo());
        pessoaEntity.setDataNascimento(request.getData_nascimento());
        pessoaEntity.setNumRegistroNasc(request.getNum_registro_nasc());
        pessoaEntity.setFls(request.getFls());
        pessoaEntity.setLivro(request.getLivro());
        pessoaEntity.setCartorio(request.getCartorio());
        pessoaEntity.setCpf(request.getCpf());
        pessoaEntity.setRg(request.getRg());
        pessoaEntity.setDataEmissaoRg(request.getData_emissao_rg());
        pessoaEntity.setOrgaoEmissorRg(request.getOrgao_emissor_rg());
        pessoaEntity.setCns(request.getCns());
        pessoaEntity.setNis(request.getNis());
        pessoaEntity.setDataCadastramento(request.getData_cadastramento());

        return pessoaEntity;
    }

    @Override
    public PessoaResponse toResponse(PessoaEntity pessoa) {
        PessoaResponse response = new PessoaResponse();

        response.setId(pessoa.getId());
        response.setNome_completo(pessoa.getNomeCompleto());
        response.setData_nascimento(pessoa.getDataNascimento());
        response.setNum_registro_nasc(pessoa.getNumRegistroNasc());
        response.setFls(pessoa.getFls());
        response.setLivro(pessoa.getLivro());
        response.setCartorio(pessoa.getCartorio());
        response.setCpf(pessoa.getCpf());
        response.setRg(pessoa.getRg());
        response.setData_emissao_rg(pessoa.getDataEmissaoRg());
        response.setOrgao_emissor_rg(pessoa.getOrgaoEmissorRg());
        response.setCns(pessoa.getCns());
        response.setNis(pessoa.getNis());
        response.setData_cadastramento(pessoa.getDataCadastramento());

        return response;
    }

    @Override
    public Pessoa toDomain(PessoaEntity entity) {
        Pessoa pessoa = new Pessoa();

        pessoa.setId(entity.getId());
        pessoa.setNomeCompleto(entity.getNomeCompleto());
        pessoa.setDataNascimento(entity.getDataNascimento());
        pessoa.setNumRegistroNasc(entity.getNumRegistroNasc());
        pessoa.setFls(entity.getFls());
        pessoa.setLivro(entity.getLivro());
        pessoa.setCartorio(entity.getCartorio());
        pessoa.setCpf(entity.getCpf());
        pessoa.setRg(entity.getRg());
        pessoa.setDataEmissaoRg(entity.getDataEmissaoRg());
        pessoa.setOrgaoEmissorRg(entity.getOrgaoEmissorRg());
        pessoa.setCns(entity.getCns());
        pessoa.setNis(entity.getNis());
        pessoa.setDataCadastramento(entity.getDataCadastramento());

        return pessoa;
    }

    @Override
    public PessoaEntity toEntityFromDomain(Pessoa domain) {
        PessoaEntity pessoaEntity = new PessoaEntity();

        pessoaEntity.setId(domain.getId());
        pessoaEntity.setNomeCompleto(domain.getNomeCompleto());
        pessoaEntity.setDataNascimento(domain.getDataNascimento());
        pessoaEntity.setNumRegistroNasc(domain.getNumRegistroNasc());
        pessoaEntity.setFls(domain.getFls());
        pessoaEntity.setLivro(domain.getLivro());
        pessoaEntity.setCartorio(domain.getCartorio());
        pessoaEntity.setCpf(domain.getCpf());
        pessoaEntity.setRg(domain.getRg());
        pessoaEntity.setDataEmissaoRg(domain.getDataEmissaoRg());
        pessoaEntity.setOrgaoEmissorRg(domain.getOrgaoEmissorRg());
        pessoaEntity.setCns(domain.getCns());
        pessoaEntity.setNis(domain.getNis());
        pessoaEntity.setDataCadastramento(domain.getDataCadastramento());

        return pessoaEntity;
    }
}
