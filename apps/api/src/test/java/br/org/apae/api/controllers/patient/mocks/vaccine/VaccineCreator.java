package br.org.apae.api.controllers.patient.mocks.vaccine;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

import java.util.UUID;

public class VaccineCreator {

    public static CreateVaccineDTO createRequest() {
        return new CreateVaccineDTO("BCG");
    }

    public static CreateVaccineDTO createInvalidRequest() {
        return new CreateVaccineDTO("");
    }

    public static VaccineResponseDTO createResponse() {
        return new VaccineResponseDTO(
                UUID.randomUUID(),
                "BCG"
        );
    }
}
