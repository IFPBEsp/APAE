package br.org.apae.api.common.dto.patient.request.annualregistry;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Set;

import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateAnnualRegistryDTO(
    @Size(min = 1, max = 50) String bpc,

    String diseases,

    @Positive BigDecimal familyIncome,

    Year year,

    Set<UpdateDisorderDTO> disorders) {
}