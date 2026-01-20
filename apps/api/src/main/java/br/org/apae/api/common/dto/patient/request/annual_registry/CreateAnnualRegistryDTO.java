package br.org.apae.api.common.dto.patient.request.annual_registry;

import java.math.BigDecimal;
import java.util.Set;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateAnnualRegistryDTO(
                @NotNull @Size(min = 1, max = 50) String bpc,

                @NotNull String diseases,

                String continuousMedication,

                @NotNull @Positive BigDecimal familyIncome,

                @NotNull Integer year,

                Set<CreateDisorderDTO> disorders,

                @NotNull
                Set<CreateServiceAreaDTO> serviceArea) {
}
