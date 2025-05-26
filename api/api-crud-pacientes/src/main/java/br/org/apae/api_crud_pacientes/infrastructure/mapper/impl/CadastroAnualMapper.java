package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl;

import org.springframework.stereotype.Component;

import br.org.apae.api_crud_pacientes.api.dtos.request.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.infrastructure.entity.CadastroAnualEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.GenericMapperInterface;

@Component
public class CadastroAnualMapper implements GenericMapperInterface<CadastroAnualRequest, CadastroAnualResponse, CadastroAnualEntity, PessoaEntity, CadastroAnual> {

    private final PessoaMapper pessoaMapper;

    public CadastroAnualMapper(PessoaMapper pessoaMapper) {
        this.pessoaMapper = pessoaMapper;
    }

    @Override
    public CadastroAnualEntity toEntity(CadastroAnualRequest request, PessoaEntity pessoa) {
        CadastroAnualEntity cadastro = new CadastroAnualEntity();
        cadastro.setBeneficioDePrestacaoContinuada(request.getBeneficioDePrestacaoContinuada());
        cadastro.setHistoricosAlergias(request.getHistoricosAlergias());
        cadastro.setMedicacoesContinuas(request.getMedicacoesContinuas());
        cadastro.setHistoricoDoencas(request.getHistoricoDoencas());
        cadastro.setRendaFamiliar(request.getRendaFamiliar());
        cadastro.setPessoa(pessoa);
        return cadastro;
    }

    @Override
    public CadastroAnualResponse toResponse(CadastroAnualEntity cadastro) {
        CadastroAnualResponse dto = new CadastroAnualResponse();
        dto.setId(cadastro.getId());
        dto.setBeneficioDePrestacaoContinuada(cadastro.getBeneficioDePrestacaoContinuada());
        dto.setHistoricosAlergias(cadastro.getHistoricosAlergias());
        dto.setMedicacoesContinuas(cadastro.getMedicacoesContinuas());
        dto.setHistoricoDoencas(cadastro.getHistoricoDoencas());
        dto.setRendaFamiliar(cadastro.getRendaFamiliar());
        dto.setPessoaId(cadastro.getPessoa().getId());
        return dto;
    }

    @Override
    public CadastroAnual toDomain(CadastroAnualEntity entity) {
        CadastroAnual domain = new CadastroAnual();
        domain.setId(entity.getId());
        domain.setBeneficioDePrestacaoContinuada(entity.getBeneficioDePrestacaoContinuada());
        domain.setHistoricosAlergias(entity.getHistoricosAlergias());
        domain.setMedicacoesContinuas(entity.getMedicacoesContinuas());
        domain.setHistoricoDoencas(entity.getHistoricoDoencas());
        domain.setRendaFamiliar(entity.getRendaFamiliar());
        domain.setPessoa(pessoaMapper.toDomain(entity.getPessoa()));
        return domain;
    }

    @Override
    public CadastroAnualResponse toResponseFromDomain(CadastroAnual domain) {
        CadastroAnualResponse dto = new CadastroAnualResponse();
        dto.setId(domain.getId());
        dto.setBeneficioDePrestacaoContinuada(domain.getBeneficioDePrestacaoContinuada());
        dto.setHistoricosAlergias(domain.getHistoricosAlergias());
        dto.setMedicacoesContinuas(domain.getMedicacoesContinuas());
        dto.setHistoricoDoencas(domain.getHistoricoDoencas());
        dto.setRendaFamiliar(domain.getRendaFamiliar());
        dto.setPessoaId(domain.getPessoa().getId());
        return dto;
    }

    @Override
    public CadastroAnualEntity toEntityFromDomain(CadastroAnual domain) {
        CadastroAnualEntity entity = new CadastroAnualEntity();
        entity.setId(domain.getId());
        entity.setBeneficioDePrestacaoContinuada(domain.getBeneficioDePrestacaoContinuada());
        entity.setHistoricosAlergias(domain.getHistoricosAlergias());
        entity.setMedicacoesContinuas(domain.getMedicacoesContinuas());
        entity.setHistoricoDoencas(domain.getHistoricoDoencas());
        entity.setRendaFamiliar(domain.getRendaFamiliar());
        entity.setPessoa(pessoaMapper.toEntityFromDomain(domain.getPessoa()));
        return entity;
    }
}