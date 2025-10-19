package br.org.apae.api.common.dto.patient.response.annual_registry;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.model.Patient;

public record AnnualRegistryResponseDTO(
        UUID id,
        String bpc,
        String diseases,
        BigDecimal familyIncome,
        Year year,
        PatientResponseDTO patientId,
        Set<DisorderResponseDTO> disorders) {

    public AnnualRegistryResponseDTO(
            UUID id,
            String bpc,
            String diseases,
            BigDecimal familyIncome,
            Year year,
            Patient patient,
            Set<Disorder> disorders) {
        this(
                id,
                bpc,
                diseases,
                familyIncome,
                year,
                patient != null ? new PatientResponseDTO(patient) : null,
                disorders != null
                        ? disorders.stream()
                                .map(DisorderResponseDTO::new)
                                .collect(Collectors.toSet())
                        : Set.of());
    }

    public AnnualRegistryResponseDTO(AnnualRegistry entity) {
        this(
                entity.getId(),
                entity.getBpc(),
                entity.getDiseases(),
                entity.getFamilyIncome(),
                entity.getYear(),
                entity.getPatient(),
                entity.getDisorders());
    }
}