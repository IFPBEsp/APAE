package br.org.apae.api.professional.application.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

class AvailabilityMapperTest {

    private AvailabilityMapper availabilityMapper;
    private HealthProfessional professional;

    @BeforeEach
    void setUp() {
        availabilityMapper = new AvailabilityMapper();
        professional = new HealthProfessional(null, new ServiceArea("Fisioterapia"), "CREFITO-123");
    }

    @Test
    @DisplayName("toEntity de criacao deve converter dia e turno e associar o profissional")
    void toEntity_ShouldMapDayShiftAndProfessional_WhenGivenCreateDTO() {
        CreateAvailabilityDTO dto = new CreateAvailabilityDTO("SEGUNDA", "MANHA");

        Availability entity = availabilityMapper.toEntity(dto, professional);

        assertEquals(Day.SEGUNDA, entity.getDay());
        assertEquals(Shift.MANHA, entity.getShift());
        assertSame(professional, entity.getProfessional());
        assertNull(entity.getId());
    }

    @Test
    @DisplayName("toEntity de criacao deve normalizar valores minusculos")
    void toEntity_ShouldNormalizeLowercaseValues() {
        CreateAvailabilityDTO dto = new CreateAvailabilityDTO("segunda", "manha");

        Availability entity = availabilityMapper.toEntity(dto, professional);

        assertEquals(Day.SEGUNDA, entity.getDay());
        assertEquals(Shift.MANHA, entity.getShift());
    }

    @Test
    @DisplayName("toEntity de atualizacao deve aplicar a mesma conversao do DTO de criacao")
    void toEntityFromUpdate_ShouldMapDayShiftAndProfessional() {
        UpdateAvailabilityDTO dto = new UpdateAvailabilityDTO("TERCA", "TARDE");

        Availability entity = availabilityMapper.toEntity(dto, professional);

        assertEquals(Day.TERCA, entity.getDay());
        assertEquals(Shift.TARDE, entity.getShift());
        assertSame(professional, entity.getProfessional());
    }

    @Test
    @DisplayName("toEntity deve recusar dia fora do dominio dos enums")
    void toEntity_ShouldThrowIllegalArgumentException_WhenDayIsInvalid() {
        CreateAvailabilityDTO dto = new CreateAvailabilityDTO("FUNDO", "MANHA");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> availabilityMapper.toEntity(dto, professional));

        assertEquals("Dia ou Turno inválido: FUNDO / MANHA", exception.getMessage());
    }

    @Test
    @DisplayName("toEntity deve recusar turno fora do dominio dos enums")
    void toEntity_ShouldThrowIllegalArgumentException_WhenShiftIsInvalid() {
        CreateAvailabilityDTO dto = new CreateAvailabilityDTO("SEGUNDA", "MADRUGADA");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> availabilityMapper.toEntity(dto, professional));

        assertEquals("Dia ou Turno inválido: SEGUNDA / MADRUGADA", exception.getMessage());
    }

    @Test
    @DisplayName("toEntity nao protege o NullPointerException quando o dia e nulo")
    void toEntity_ShouldThrowNullPointerException_WhenDayIsNull() {
        CreateAvailabilityDTO dto = new CreateAvailabilityDTO(null, "manha");

        assertThrows(NullPointerException.class, () -> availabilityMapper.toEntity(dto, professional));
    }

    @Test
    @DisplayName("toResponseDTO deve converter dia e turno pelo nome do enum")
    void toResponseDTO_ShouldMapDayAndShift() {
        Availability entity = new Availability(Day.QUARTA, Shift.TARDE, professional);

        AvailabilityResponseDTO dto = availabilityMapper.toResponseDTO(entity);

        assertEquals("QUARTA", dto.day());
        assertEquals("TARDE", dto.shift());
    }

    @Test
    @DisplayName("toResponseDTO sempre devolve id nulo porque a entidade nao expoe setter de id")
    void toResponseDTO_ShouldAlwaysReturnNullId_BecauseEntityHasNoIdSetter() {
        Availability entity = new Availability(Day.SEXTA, Shift.MANHA, professional);

        AvailabilityResponseDTO dto = availabilityMapper.toResponseDTO(entity);

        assertNull(entity.getId());
        assertNull(dto.id());
    }
}
