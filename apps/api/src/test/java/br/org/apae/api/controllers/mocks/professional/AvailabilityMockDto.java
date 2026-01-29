package br.org.apae.api.controllers.mocks.professional;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;

public final class AvailabilityMockDto {

    private AvailabilityMockDto() {}

    public static final UUID PROFESSIONAL_ID_1 =
        UUID.fromString("11111111-1111-1111-1111-111111111111");

    public static final UUID AVAILABILITY_ID_1 =
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    public static final UUID AVAILABILITY_ID_2 =
        UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    public static CreateAvailabilityDTO createAvailabilityRequestMorning() {
        return new CreateAvailabilityDTO("SEGUNDA", "MANHA");
    }

    public static CreateAvailabilityDTO createAvailabilityRequestAfternoon() {
        return new CreateAvailabilityDTO("TERCA", "TARDE");
    }

    public static UpdateAvailabilityDTO updateAvailabilityRequest() {
        return new UpdateAvailabilityDTO("QUARTA", "TARDE");
    }

    public static AvailabilityResponseDTO availabilityResponseMorning() {
        return new AvailabilityResponseDTO(
            AVAILABILITY_ID_1,
            "SEGUNDA",
            "MANHA"
        );
    }

    public static AvailabilityResponseDTO availabilityResponseAfternoon() {
        return new AvailabilityResponseDTO(
            AVAILABILITY_ID_2,
            "TERCA",
            "TARDE"
        );
    }

    public static AvailabilityResponseDTO availabilityResponseUpdated() {
        return new AvailabilityResponseDTO(
            AVAILABILITY_ID_1,
            "QUARTA",
            "TARDE"
        );
    }

    public static List<AvailabilityResponseDTO> availabilityResponseList() {
        return List.of(
            availabilityResponseMorning(),
            availabilityResponseAfternoon()
        );
    }
}
