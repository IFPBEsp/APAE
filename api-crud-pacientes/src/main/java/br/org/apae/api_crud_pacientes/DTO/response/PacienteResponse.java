package br.org.apae.api_crud_pacientes.DTO.response;

import br.org.apae.api_crud_pacientes.model.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PacienteResponse {

    private UUID id;
    private  String nome_completo;
    private String cpf;
    private List<String> contatos;
    private LocalDate data_nascimento;

    public PacienteResponse(Paciente paciente) {
        this.id = paciente.getId();
        this.nome_completo = paciente.getNome_completo();
        this.cpf = paciente.getCpf();
        this.contatos = paciente.getContatos();
        this.data_nascimento = paciente.getData_nascimento();
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
