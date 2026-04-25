package br.org.apae.api.patient.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @Column(name = "avaliacao_id")
    private UUID id;

    @Column(name = "aluno_id")
    private UUID alunoId;

    @Column(name = "aluno_nome")
    private String alunoNome;

    @Column(name = "professor_id")
    private UUID professorId;

    @Column(name = "professor_nome")
    private String professorNome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao;

    public AssessmentView(){

    }

    public UUID getId(){
        return id;
    }

    public UUID getAlunoId(){
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public UUID getProfessorId() {
        return professorId;
    }

    public String getProfessorNome() {
        return professorNome;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

}
