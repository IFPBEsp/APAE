package br.org.apae.api.patient.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "AvaliacoesView")
public class AssessmentView {

    @Id
    private Long id;

    @Column(name = "descricao_avaliacao")
    private String descricaoAvaliacao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

    @Column(name = "aluno_id")
    private Long pacienteId;

    @Column(name = "nome_professor")
    private String nomeProfessor;

    public AssessmentView(){

    }

    public Long getId(){
        return id;
    }

    public String getDescricaoAvaliacao() {
        return descricaoAvaliacao;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public Long getPacienteId(){
        return pacienteId;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }


}
