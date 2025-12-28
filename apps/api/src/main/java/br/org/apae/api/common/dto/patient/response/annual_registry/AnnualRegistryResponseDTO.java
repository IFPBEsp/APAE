package br.org.apae.api.common.dto.patient.response.annual_registry;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;

public record AnnualRegistryResponseDTO(
                UUID id,
                String bpc,
                String diseases,
                BigDecimal familyIncome,
                Integer year,
                UUID patientId,
                Set<DisorderResponseDTO> disorders,
                Set<ServiceAreaResponseDTO> serviceAreas) {

        public AnnualRegistryResponseDTO(AnnualRegistry entity, Set<DisorderResponseDTO> disorderDtos, Set<ServiceAreaResponseDTO> serviceAreas) {
                this(
                                entity.getId(),
                                entity.getBpc(),
                                entity.getDiseases(),
                                entity.getFamilyIncome(),
                                entity.getYear(),
                                entity.getPatientId(),
                                disorderDtos,
                                serviceAreas);
        }
}
