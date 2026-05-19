package br.org.apae.api.common.dto.patient.response.vaccine;

import java.util.UUID;

import br.org.apae.api.patient.domain.model.Vaccine;

public record VaccineResponseDTO(
    UUID id,
    String name,
    Boolean hasPatient) {
  public VaccineResponseDTO(Vaccine vaccine, Boolean hasPatient) {
    this(
        vaccine.getId(),
        vaccine.getName(),
        hasPatient);
  }
}