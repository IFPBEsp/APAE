package br.org.apae.api.common.dto.patient.request.annual_registry;

import java.math.BigDecimal;
import java.util.Set;

import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateAnnualRegistryDTO(
    @Size(min = 1, max = 50) String bpc,

    String diseases,

    @Positive BigDecimal familyIncome,

    Integer year,

    Set<UpdateDisorderDTO> disorders) {
}