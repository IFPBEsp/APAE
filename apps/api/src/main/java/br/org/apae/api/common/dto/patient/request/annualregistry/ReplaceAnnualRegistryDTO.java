package br.org.apae.api.common.dto.patient.request.annualregistry;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record ReplaceAnnualRegistryDTO(
        @NotNull @Size(min = 1, max = 50)
        String bpc,

        @NotNull
        String diseases,

        @NotNull @Positive
        BigDecimal familyIncome,

        @NotNull
        String continuousMedication,

        @NotNull
        Set<CreateDisorderDTO> disorders,

        @NotNull
        Set<CreateServiceTypeDTO> serviceTypes
) {}
