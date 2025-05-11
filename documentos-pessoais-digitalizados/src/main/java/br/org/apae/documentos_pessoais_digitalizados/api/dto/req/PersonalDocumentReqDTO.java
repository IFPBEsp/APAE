package br.org.apae.documentos_pessoais_digitalizados.api.dto.req;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PersonalDocumentReqDTO(
        @NotEmpty(message = "Deve conter pelo menos um documento.")
        List<PersonalDocumentFileReqDTO> documents
    ) {
}
