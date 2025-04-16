package br.org.apae.laudos_medicos.application.dtos.responses;

import java.time.LocalDate;

public class LaudoResponseDTO {
       private Long id;
    private Long idPaciente;
    private LocalDate dataEmissao;
    private String descricao;
    
    public LaudoResponseDTO(Long id, Long idPaciente, LocalDate dataEmissao, String descricao) {
        this.id = id;
        this.idPaciente = idPaciente;
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
