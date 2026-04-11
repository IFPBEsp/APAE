package br.org.apae.api.common.dto.patient.response.disorder;

import java.util.UUID;

import br.org.apae.api.patient.domain.model.Disorder;

public record DisorderResponseDTO(
        UUID id,
        String name,
        Boolean hasPatient
        ) {

                public DisorderResponseDTO(Disorder disorder, Boolean hasPatient) {
                this(
                disorder.getId(),
                disorder.getName(),
                hasPatient
                );
        }

        public DisorderResponseDTO(Disorder disorder){
                this(disorder.getId(), disorder.getName(), true);
        }
}