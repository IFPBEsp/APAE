package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.annualregistry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annualregistry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnualRegistryMapperTest {

    @Mock
    private DisorderMapper disorderMapper;

    @Mock
    private ServiceAreaMapper serviceAreaMapper;

    @InjectMocks
    private AnnualRegistryMapper annualRegistryMapper;

    private UUID patientId;
    private Set<Disorder> dummyDisorders;
    private Set<ServiceArea> dummyServiceAreas;
    private Set<DisorderResponseDTO> disorderDtos;
    private Set<ServiceAreaResponseDTO> serviceAreaDtos;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        dummyDisorders = Set.of(new Disorder(UUID.randomUUID(), "Autismo"));
        dummyServiceAreas = Set.of(new ServiceArea(1, "Fisioterapia"));
        disorderDtos = Set.of(new DisorderResponseDTO(UUID.randomUUID(), "Autismo", false));
        serviceAreaDtos = Set.of(new ServiceAreaResponseDTO(1, "Fisioterapia"));
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateAnnualRegistryDTO dto = new CreateAnnualRegistryDTO(
                "BPC Sim", "Asma", "Ritalina", new BigDecimal("2000.00"),
                Year.of(2024), Set.of(), Set.of()
        );

        when(disorderMapper.toEntitySetFromResponse(disorderDtos)).thenReturn(dummyDisorders);
        when(serviceAreaMapper.toEntitySetFromResponse(serviceAreaDtos)).thenReturn(dummyServiceAreas);

        AnnualRegistry entity = annualRegistryMapper.toEntity(dto, disorderDtos, serviceAreaDtos, patientId);

        assertNotNull(entity);
        assertEquals(dto.bpc(), entity.getBpc());
        assertEquals(dto.diseases(), entity.getDiseases());
        assertEquals(dto.continuousMedication(), entity.getContinuousMedication());
        assertEquals(dto.familyIncome(), entity.getFamilyIncome());
        assertEquals(dto.year().getValue(), entity.getYear());
        assertEquals(patientId, entity.getPatientId());
        assertEquals(dummyDisorders, entity.getDisorders());
        assertEquals(dummyServiceAreas, entity.getServiceAreas());
    }

    @Test
    void toEntity_ShouldHandleNullOptionalFields() {
        CreateAnnualRegistryDTO dto = new CreateAnnualRegistryDTO(
                "BPC Não", "Nenhuma", null, new BigDecimal("1000.00"),
                Year.of(2024), Set.of(), Set.of()
        );

        when(disorderMapper.toEntitySetFromResponse(disorderDtos)).thenReturn(dummyDisorders);
        when(serviceAreaMapper.toEntitySetFromResponse(serviceAreaDtos)).thenReturn(dummyServiceAreas);

        AnnualRegistry entity = annualRegistryMapper.toEntity(dto, disorderDtos, serviceAreaDtos, patientId);

        assertNotNull(entity);
        assertNull(entity.getContinuousMedication());
        assertEquals(dto.bpc(), entity.getBpc());
        assertEquals(dto.diseases(), entity.getDiseases());
    }

    @Test
    void updateEntityFromDto_ShouldUpdateOnlyYear() {
        AnnualRegistry entity = new AnnualRegistry(
                "BPC Sim", "Asma", "Ritalina", new BigDecimal("2000.00"),
                2023, patientId, Set.of(), Set.of()
        );
        UpdateAnnualRegistryDTO dto = new UpdateAnnualRegistryDTO(
                "BPC Sim", "Asma", new BigDecimal("2000.00"), Year.of(2024), Set.of()
        );

        AnnualRegistry updated = annualRegistryMapper.updateEntityFromDto(entity, dto);

        assertEquals(2024, updated.getYear());
        assertEquals("BPC Sim", updated.getBpc());
    }

    @Test
    void replaceEntityFromDto_ShouldReplaceAllFields() {
        AnnualRegistry entity = new AnnualRegistry(
                "BPC Antigo", "Old", "Old med", new BigDecimal("1000.00"),
                2023, patientId, Set.of(), Set.of()
        );
        ReplaceAnnualRegistryDTO dto = new ReplaceAnnualRegistryDTO(
                "BPC Novo", "Nova doença", new BigDecimal("3000.00"),
                "Novo med", Set.of(), Set.of()
        );

        when(disorderMapper.toEntitySetFromResponse(disorderDtos)).thenReturn(dummyDisorders);
        when(serviceAreaMapper.toEntitySetFromResponse(serviceAreaDtos)).thenReturn(dummyServiceAreas);

        AnnualRegistry replaced = annualRegistryMapper.replaceEntityFromDto(entity, dto, disorderDtos, serviceAreaDtos);

        assertEquals(dto.bpc(), replaced.getBpc());
        assertEquals(dto.diseases(), replaced.getDiseases());
        assertEquals(dto.continuousMedication(), replaced.getContinuousMedication());
        assertEquals(dto.familyIncome(), replaced.getFamilyIncome());
        assertEquals(dummyDisorders, replaced.getDisorders());
        assertEquals(dummyServiceAreas, replaced.getServiceAreas());
    }

    @Test
    void toResponseDTO_ShouldMapAllFields_WhenCollectionsAreNotNull() {
        UUID id = UUID.randomUUID();
        AnnualRegistry entity = new AnnualRegistry(
                id, "BPC Sim", "Nenhuma", "Nenhum", new BigDecimal("2000.00"),
                2024, patientId, dummyDisorders, dummyServiceAreas
        );

        AnnualRegistryResponseDTO response = annualRegistryMapper.toResponseDTO(entity);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(entity.getBpc(), response.bpc());
        assertEquals(entity.getYear(), response.year());
        assertEquals(1, response.disorders().size());
        assertEquals(1, response.serviceAreas().size());
    }

    @Test
    void toResponseDTO_ShouldHandleEmptyCollections() {
        AnnualRegistry entity = new AnnualRegistry(
                "BPC Sim", "Nenhuma", "Nenhum", new BigDecimal("2000.00"),
                2024, patientId, Set.of(), Set.of()
        );

        AnnualRegistryResponseDTO response = annualRegistryMapper.toResponseDTO(entity);

        assertNotNull(response);
        assertTrue(response.disorders().isEmpty());
        assertTrue(response.serviceAreas().isEmpty());
    }
}
