package br.org.apae.documentos_digitalizados.api.dto;

import java.time.LocalDate;
import java.util.UUID;

import br.org.apae.documentos_digitalizados.domain.DocumentCategory;
import br.org.apae.documentos_digitalizados.domain.DocumentType;

public record DocumentObjectRequestDTO(
  String patientId,
  Integer year,
  String documentCategory,
  String documentType
) {

  public void validateDocument() {
    validatePatientId();
    validateDocumentsByCategoryAndType();
    validateYear();
  }

  private void validatePatientId() {
    if (patientId == null || patientId.isBlank()) {
      throw new IllegalArgumentException("O ID do paciente é obrigatório.");
    }

    if (UUID.fromString(patientId) == null) {
      throw new IllegalArgumentException("O ID do paciente deve ser um UUID válido.");
    }
  }

  private void validateDocumentsByCategoryAndType() {
    if (DocumentCategory.valueOf(documentCategory.toUpperCase()) == null) {
      throw new IllegalArgumentException("Categoria de documento inválida: " + documentCategory);
    }

    if (DocumentType.valueOf(documentType.toUpperCase()).equals(null) || 
        DocumentType.valueOf(documentType.toUpperCase()).getCategory() != 
        DocumentCategory.valueOf(documentCategory.toUpperCase())
    ) {
      throw new IllegalArgumentException("Tipo ou categoria de documento inválido(s). tipo: " + documentType + 
        ", categoria: " + documentCategory);
    }
  }

  private void validateYear() {
    if (year == null) {
      throw new IllegalArgumentException("O ano é obrigatório.");
    }

    if (year > LocalDate.now().getYear()) {
      throw new IllegalArgumentException("Ano inválido: " + year);
    }
  }
}
