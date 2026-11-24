package br.org.apae.api.common.dto.patient.request.annualregistry;

import java.math.BigDecimal;
import java.util.Set;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ReplaceAnnualRegistryDTO(
        @NotNull @Size(min = 1, max = 50)
        String bpc,

        @NotNull
        String diseases,

        @NotNull @Positive
        BigDecimal familyIncome,

       
        String continuousMedication,

        @NotNull
        Set<CreateDisorderDTO> disorders,

        @NotNull
        Set<CreateServiceAreaDTO> serviceAreas
) {}