package br.org.apae.api.patient.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "view_avaliacoes_escolares")
public class AssessmentView {

    @Id
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

    @Column(name = "aluno_id")
    private Long pacienteId;

    @Column(name = "professor_nome")
    private String professorNome;

    public AssessmentView(){

    }

    public Long getId(){
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public Long getPacienteId(){
        return pacienteId;
    }

    public String getProfessorNome() {
        return professorNome;
    }


}
