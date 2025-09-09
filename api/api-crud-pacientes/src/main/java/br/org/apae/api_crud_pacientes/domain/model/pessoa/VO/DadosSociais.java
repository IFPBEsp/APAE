package br.org.apae.api_crud_pacientes.domain.model.pessoa.VO;

import java.time.LocalDate;

public class DadosSociais {
    private String cns;
    private String nis;
    private LocalDate dataCadastramento;

    public DadosSociais(String cns, String nis, LocalDate dataCadastramento) {
        this.cns = cns;
        this.nis = nis;
        this.dataCadastramento = dataCadastramento;
        validate();
    }

    public String getCns() {
        return cns;
    }

    public String getNis() {
        return nis;
    }

    public LocalDate getDataCadastramento() {
        return dataCadastramento;
    }

    protected void validate() {
        if (cns == null || cns.isBlank()) {
            throw new IllegalArgumentException("CNS inválido");
        }
        if (nis == null || nis.isBlank()) {
            throw new IllegalArgumentException("NIS inválido");
        }
        if (dataCadastramento != null && dataCadastramento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de cadastramento não pode estar no futuro");
        }
    }
}
