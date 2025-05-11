package br.org.apae.documentos_medicos.api.dto.requests;

import java.time.LocalDate;

import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import jakarta.validation.constraints.NotBlank;

public class MedicalDocumentRequestDTO {

    @NotBlank(message = "É obrigatório informar o tipo do documento")
    private MedcialDocumentType tipo;
    private LocalDate dataReferencia;
    private String descricao;
    
}
