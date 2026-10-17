package br.org.apae.api.servicearea.application.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

class ServiceAreaMapperTest {

    private static final Integer AREA_ID = 7;
    private static final String AREA_NAME = "Fisioterapia";

    private ServiceAreaMapper serviceAreaMapper;

    @BeforeEach
    void setUp() {
        serviceAreaMapper = new ServiceAreaMapper();
    }

    @Test
    @DisplayName("toEntity deve mapear a area e deixar o id nulo, gerado apenas pelo banco")
    void toEntity_ShouldMapArea_WhenGivenCreateDTO() {
        CreateServiceAreaDTO dto = new CreateServiceAreaDTO(AREA_NAME);

        ServiceArea entity = serviceAreaMapper.toEntity(dto);

        assertEquals(AREA_NAME, entity.getArea());
        assertNull(entity.getId());
    }

    @Test
    @DisplayName("toEntityFromResponse deve mapear id e area")
    void toEntityFromResponse_ShouldMapIdAndArea() {
        ServiceAreaResponseDTO dto = new ServiceAreaResponseDTO(AREA_ID, AREA_NAME);

        ServiceArea entity = serviceAreaMapper.toEntityFromResponse(dto);

        assertEquals(AREA_ID, entity.getId());
        assertEquals(AREA_NAME, entity.getArea());
    }

    @Test
    @DisplayName("toEntityFromResponse deve mapear area mesmo quando o id e nulo")
    void toEntityFromResponse_ShouldMapArea_WhenIdIsNull() {
        ServiceAreaResponseDTO dto = new ServiceAreaResponseDTO(null, AREA_NAME);

        ServiceArea entity = serviceAreaMapper.toEntityFromResponse(dto);

        assertNull(entity.getId());
        assertEquals(AREA_NAME, entity.getArea());
    }

    @Test
    @DisplayName("updateEntityFromDto deve alterar a area e devolver a mesma instancia")
    void updateEntityFromDto_ShouldUpdateAreaAndKeepSameInstance() {
        ServiceArea entity = new ServiceArea(AREA_ID, "Fisioterapia");
        UpdateServiceAreaDTO dto = new UpdateServiceAreaDTO("Psicologia");

        ServiceArea result = serviceAreaMapper.updateEntityFromDto(entity, dto);

        assertSame(entity, result);
        assertEquals(AREA_ID, result.getId());
        assertEquals("Psicologia", result.getArea());
    }

    @Test
    @DisplayName("toEntitySetFromResponse deve converter todos os itens com id e area proprios")
    void toEntitySetFromResponse_ShouldMapAllItems() {
        Set<ServiceAreaResponseDTO> dtos = Set.of(
                new ServiceAreaResponseDTO(1, "Fisioterapia"),
                new ServiceAreaResponseDTO(2, "Psicologia")
        );

        Set<ServiceArea> entities = serviceAreaMapper.toEntitySetFromResponse(dtos);

        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> Integer.valueOf(1).equals(e.getId()) && "Fisioterapia".equals(e.getArea())));
        assertTrue(entities.stream().anyMatch(e -> Integer.valueOf(2).equals(e.getId()) && "Psicologia".equals(e.getArea())));
    }

    @Test
    @DisplayName("toEntitySetFromResponse deve devolver conjunto vazio para entrada vazia")
    void toEntitySetFromResponse_ShouldReturnEmptySet_WhenInputIsEmpty() {
        Set<ServiceArea> entities = serviceAreaMapper.toEntitySetFromResponse(Set.of());

        assertTrue(entities.isEmpty());
    }

    @Test
    @DisplayName("toResponseDTO deve mapear todos os campos da entidade")
    void toResponseDTO_ShouldMapAllFields() {
        ServiceArea entity = new ServiceArea(AREA_ID, AREA_NAME);

        ServiceAreaResponseDTO dto = serviceAreaMapper.toResponseDTO(entity);

        assertEquals(AREA_ID, dto.id());
        assertEquals(AREA_NAME, dto.area());
    }

    @Test
    @DisplayName("toResponseDTO deve propagar id nulo para entidade ainda nao persistida")
    void toResponseDTO_ShouldMapNullId_WhenEntityHasNoId() {
        ServiceArea entity = new ServiceArea(AREA_NAME);

        ServiceAreaResponseDTO dto = serviceAreaMapper.toResponseDTO(entity);

        assertNull(dto.id());
        assertEquals(AREA_NAME, dto.area());
    }
}
