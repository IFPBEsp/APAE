package br.org.apae.api.common.dto.patient.response.annual_registry;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;

public record AnnualRegistryResponseDTO(
        UUID id,
        String bpc,
        String diseases,
        String continuousMedication,
        BigDecimal familyIncome,
        Integer year,
        Set<DisorderResponseDTO> disorders
) {
        public AnnualRegistryResponseDTO(AnnualRegistry annualRegistry, Set<DisorderResponseDTO> disorders) {
                this(
                        annualRegistry.getId(),
                        annualRegistry.getBpc(),
                        annualRegistry.getDiseases(),
                        annualRegistry.getContinuousMedication(),
                        annualRegistry.getFamilyIncome(),
                        annualRegistry.getYear(),
                        disorders
                );
        }
}