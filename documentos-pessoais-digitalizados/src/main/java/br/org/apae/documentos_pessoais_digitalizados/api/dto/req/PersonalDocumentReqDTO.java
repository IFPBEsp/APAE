package br.org.apae.documentos_pessoais_digitalizados.api.dto.req;

import java.util.List;
import java.util.UUID;

public record PersonalDocumentReqDTO(UUID patient, List<PersonalDocumentFileReqDTO> documents) {
}
