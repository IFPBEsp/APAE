package br.org.apae.api.patient.domain.model;

import java.time.LocalDateTime;

import com.google.errorprone.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "RelatoriosView")
public class ReportView {
     @Id
     private Long id;

     private String habilidades;
     private String estrategias;
     private String recursos;

     @Column(name = "created_at")
     private LocalDateTime createdAt;

    @Column(name = "aluno_id")
    private Long pacienteId;

    @Column(name = "professor_nome")
    private String nomeProfessor;

    @Column(name = "turma_descricao")
    private String turmaDescricao;

    public ReportView(){
        
    }

    public Long getId(){
        return id;
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

    public Long getPacienteId() {
        return pacienteId;
    }

    public String getNomeProfessor(){
        return nomeProfessor;
    }

    public String getTurmaDescricao(){
        return turmaDescricao;
    }
}