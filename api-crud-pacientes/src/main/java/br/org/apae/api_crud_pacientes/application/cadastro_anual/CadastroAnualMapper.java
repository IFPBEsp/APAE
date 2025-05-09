package br.org.apae.api_crud_pacientes.application.cadastro_anual;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class CadastroAnualMapper {

    public CadastroAnual toEntity(CadastroAnualRequest dto, Pessoa pessoa) {
        CadastroAnual cadastro = new CadastroAnual();
        cadastro.setBeneficioDePrestacaoContinuada(dto.getBeneficioDePrestacaoContinuada());
        cadastro.setHistoricosAlergias(dto.getHistoricosAlergias());
        cadastro.setMedicacoesContinuas(dto.getMedicacoesContinuas());
        cadastro.setHistoricoDoencas(dto.getHistoricoDoencas());
        cadastro.setRendaFamiliar(dto.getRendaFamiliar());
        cadastro.setPessoa(pessoa);
        return cadastro;
    }

    public CadastroAnualResponse toDTO(CadastroAnual cadastro) {
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
}