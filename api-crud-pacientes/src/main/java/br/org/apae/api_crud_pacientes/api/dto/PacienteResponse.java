package br.org.apae.api_crud_pacientes.api.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import br.org.apae.api_crud_pacientes.domain.model.Pessoa;

public class PacienteResponse {

    private UUID id;
    private  String nome_completo;
    private String cpf;
    private List<String> contatos;
    private LocalDate data_nascimento;

    public PacienteResponse(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome_completo = pessoa.getNome_completo();
        this.cpf = pessoa.getCpf();
        // this.contatos = paciente.getContatos();
        this.data_nascimento = pessoa.getData_nascimento();
    }

    public UUID getId() {
        return id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public List<String> getContatos() {
        return contatos;
    }

    public String getCpf() {
        return cpf;
    }


    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }
}
