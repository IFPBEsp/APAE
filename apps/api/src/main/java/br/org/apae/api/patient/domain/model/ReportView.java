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
@Table(name = "view_relatorios_escolares")
public class ReportView {

    @Id
    @Column(name = "relatorio_id")
    private UUID id;

    @Column(name = "aluno_id")
    private UUID alunoId;

    @Column(name = "aluno_nome")
    private String alunoNome;

    @Column(name = "professor_id")
    private UUID professorId;

    @Column(name = "professor_nome")
    private String professorNome;

    @Column(name = "turma_id")
    private UUID turmaId;

    @Column(name = "turma_nome")
    private String turmaNome;

    @Column(name = "atividades")
    private String atividades;

    @Column(name = "habilidades")
    private String habilidades;

    @Column(name = "estrategias")
    private String estrategias;

    @Column(name = "recursos")
    private String recursos;

     @Column(name = "created_at")
     private LocalDateTime createdAt;

    public ReportView(){
        
    }

    public UUID getId(){
        return id;
    }

    public UUID getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome(){
        return alunoNome;
    }

    public UUID getProfessorId() {
        return professorId;
    }

    public String getProfessorNome(){
        return professorNome;
    }

     public UUID getTurmaId() {
        return turmaId;
    }

    public String getTurmaNome(){
        return turmaNome;
    }

    public String getAtividades() {
        return atividades;
    }

    public String getHabilidades(){
        return habilidades;
    }

    public String getEstrategias(){
        return estrategias;
    }

    public String getRecursos(){
        return recursos;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}