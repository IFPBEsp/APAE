package br.org.apae.documentos_pessoais_digitalizados.api.dtos.req;

import org.springframework.web.multipart.MultipartFile;

public record PersonalDocumentReqDTO(String patientId, String documentType, MultipartFile file) {
}
