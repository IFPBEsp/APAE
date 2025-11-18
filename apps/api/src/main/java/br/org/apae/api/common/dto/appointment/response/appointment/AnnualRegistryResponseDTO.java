package br.org.apae.api.common.dto.appointment.response.appointment;

import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.UUID;

public record AnnualRegistryResponseDTO(
    UUID id,
    String bpc,
    String diseases,
    BigDecimal familyIncome,
    Year year,
    PatientResponseDTO patient,
    List<DisorderResponseDTO> disorders
) { }