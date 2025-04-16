package br.org.apae.laudos_medicos.domain.entities;

import br.org.apae.laudos_medicos.domain.exceptions.DescricaoInvalidaException;
import br.org.apae.laudos_medicos.domain.exceptions.IdMedicoNaoEncontradoException;
import br.org.apae.laudos_medicos.domain.exceptions.IdPacienteNaoEncontradoException;

import java.time.LocalDate;

public class Laudo {
    private Long id;
    private Long idPaciente;
    private Long idMedico;
    private LocalDate dataEmissao;
    private String descricao;

    public Laudo() {
    }

    public Laudo(Long id, Long idPaciente, Long idMedico, LocalDate dataEmissao, String descricao) {
        if (idMedico == null) throw new IdMedicoNaoEncontradoException();
        if (idPaciente == null) throw  new IdPacienteNaoEncontradoException();
        if (descricao == null || descricao.trim().isEmpty()) throw  new DescricaoInvalidaException();

        this.id = id;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.dataEmissao = dataEmissao;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
