package br.org.apae.api_crud_pacientes.domain.model.pessoa.VO;

import java.time.LocalDate;

public class Identidade {
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String cpf;
    private String rg;
    private LocalDate dataEmissaoRg;
    private String orgaoEmissorRg;

    public Identidade(String nomeCompleto, LocalDate dataNascimento, String cpf, String rg, LocalDate dataEmissaoRg, String orgaoEmissorRg) {
        validate(nomeCompleto, dataNascimento, cpf, rg, dataEmissaoRg, orgaoEmissorRg);
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.rg = rg;
        this.dataEmissaoRg = dataEmissaoRg;
        this.orgaoEmissorRg = orgaoEmissorRg;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getRg() {
        return rg;
    }

    public LocalDate getDataEmissaoRg() {
        return dataEmissaoRg;
    }

    public String getOrgaoEmissorRg() {
        return orgaoEmissorRg;
    }

    protected  void validate(String nomeCompleto, LocalDate dataNascimento, String cpf, String rg, LocalDate dataEmissaoRg, String orgaoEmissorRg) {
        validateDataNascimento(dataNascimento);
        validateCpf(cpf);
        validateRg(rg);
        validateDataEmissaoRg(dataEmissaoRg);
        validateOrgaoEmissorRg(orgaoEmissorRg);
    }

    private void validateDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida");
        }
    }

    private void validateCpf(String cpf) {
        if (cpf.isBlank()) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }

    private void validateRg(String rg) {
        if (rg.isBlank()) {
            throw new IllegalArgumentException("RG inválido");
        }
    }

    private void validateDataEmissaoRg(LocalDate dataEmissaoRg) {
        if (dataEmissaoRg == null || dataEmissaoRg.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida");
        }
    }

    private void validateOrgaoEmissorRg(String orgaoEmissorRg) {
        if (orgaoEmissorRg.isBlank()) {
            throw new IllegalArgumentException("Orgão Emissor inválido");
        }
    }
}