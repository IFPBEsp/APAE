package br.org.apae.documentos_medicos.api.dto.requests;

import java.time.LocalDate;

import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicalDocumentRequestDTO {

    @NotBlank(message = "É obrigatório informar o tipo do documento")
    private MedcialDocumentType tipo;

    @NotNull(message = "É obrigatório informar a data de referência do documento")
    private LocalDate dataReferencia;

    @NotBlank(message = "É obrigatório informar a descrição do documento")
    private String descricao;

    public MedcialDocumentType getTipo() {
        return tipo;
    }

    public void setTipo(MedcialDocumentType tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
}
