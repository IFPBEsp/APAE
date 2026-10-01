package br.org.apae.api.professional.application.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.auth.domain.model.User;
import br.org.apae.api.auth.domain.model.UserRole;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

class HealthProfessionalMapperTest {

    private static final UUID PROFESSIONAL_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final Integer SERVICE_AREA_ID = 1;
    private static final String SERVICE_AREA_NAME = "Fisioterapia";
    private static final String PROFESSIONAL_DOCUMENT = "CREFITO-12345";
    private static final String PROFILE_PHOTO = "http://example.com/photo.jpg";
    private static final String PHONE = "11999999999";
    private static final String EMAIL = "profissional@apae.org.br";
    private static final String CPF = "123.456.789-09";
    private static final String NAME = "Joao da Silva";
    private static final String IDENTITY_DOCUMENT = "123456789";

    private static final String CITY = "Sao Paulo";
    private static final String CEP = "01000-000";
    private static final String STATE = "SP";
    private static final String NEIGHBORHOOD = "Centro";
    private static final String STREET = "Rua A";
    private static final String NUMBER = "123";

    private HealthProfessionalMapper healthProfessionalMapper;

    @BeforeEach
    void setUp() {
        healthProfessionalMapper = new HealthProfessionalMapper(
                new ServiceAreaMapper(),
                new AvailabilityMapper(),
                new AddressMapper());
    }

    private static CreateAddressDTO address() {
        return new CreateAddressDTO(CITY, CEP, STATE, NEIGHBORHOOD, STREET, NUMBER, "Apto 10");
    }

    private static UpdateAddressDTO updateAddress() {
        return new UpdateAddressDTO("Curitiba", "80000-000", "PR", "Batel", "Rua B", "456", "Sala 2");
    }

    private static ServiceAreaResponseDTO serviceAreaResponse() {
        return new ServiceAreaResponseDTO(SERVICE_AREA_ID, SERVICE_AREA_NAME);
    }

    private static User professionalUser() {
        return new User(EMAIL, "senha", CPF, NAME, UserRole.ATENDIMENTO, PHONE, IDENTITY_DOCUMENT);
    }

    private static CreateHealthProfessionalDTO createDto(List<CreateAvailabilityDTO> availabilities) {
        return new CreateHealthProfessionalDTO(
                new CreateServiceAreaDTO("area ignorada do DTO"),
                PHONE,
                PROFESSIONAL_DOCUMENT,
                EMAIL,
                CPF,
                NAME,
                IDENTITY_DOCUMENT,
                address(),
                availabilities,
                PROFILE_PHOTO);
    }

    private static UpdateHealthProfessionalDTO updateDto(List<CreateAvailabilityDTO> availabilities) {
        return new UpdateHealthProfessionalDTO(
                new UpdateServiceAreaDTO("area ignorada do DTO"),
                "11888888888",
                "CRP-99999",
                "novo@apae.org.br",
                "987.654.321-00",
                "Maria Souza",
                "987654321",
                updateAddress(),
                availabilities,
                "http://example.com/nova-foto.jpg");
    }

    @Test
    @DisplayName("toEntity deve mapear documento profissional, foto de perfil e area de atendimento")
    void toEntity_ShouldMapAllFields() {
        HealthProfessional entity = healthProfessionalMapper.toEntity(
                createDto(List.of()),
                serviceAreaResponse(),
                professionalUser());

        assertEquals(PROFESSIONAL_DOCUMENT, entity.getProfessionalDocument());
        assertEquals(PROFILE_PHOTO, entity.getProfilePhoto());
        assertNotNull(entity.getServiceArea());
        assertEquals(SERVICE_AREA_ID, entity.getServiceArea().getId());
        assertEquals(SERVICE_AREA_NAME, entity.getServiceArea().getArea());
        assertNotNull(entity.getUser());
        assertEquals(EMAIL, entity.getEmail());
        assertEquals(NAME, entity.getName());
        assertEquals(CPF, entity.getCpf());
        assertEquals(PHONE, entity.getPhoneNumber());
        assertEquals(IDENTITY_DOCUMENT, entity.getIdentityDocument());
    }

    @Test
    @DisplayName("toEntity deve converter cada disponibilidade e associar a entidade profissional")
    void toEntity_ShouldConvertEveryAvailability() {
        HealthProfessional entity = healthProfessionalMapper.toEntity(
                createDto(List.of(
                        new CreateAvailabilityDTO("SEGUNDA", "MANHA"),
                        new CreateAvailabilityDTO("TERCA", "TARDE"))),
                serviceAreaResponse(),
                professionalUser());

        List<Availability> availabilities = entity.getAvailabilities();
        assertEquals(2, availabilities.size());
        assertEquals(Day.SEGUNDA, availabilities.get(0).getDay());
        assertEquals(Shift.MANHA, availabilities.get(0).getShift());
        assertEquals(Day.TERCA, availabilities.get(1).getDay());
        assertEquals(Shift.TARDE, availabilities.get(1).getShift());
        assertSame(entity, availabilities.get(0).getProfessional());
        assertSame(entity, availabilities.get(1).getProfessional());
    }

    @Test
    @DisplayName("toEntity deve deixar a lista de disponibilidades vazia quando o DTO traz null")
    void toEntity_ShouldSkipAvailabilities_WhenListIsNull() {
        HealthProfessional entity = healthProfessionalMapper.toEntity(
                createDto(null),
                serviceAreaResponse(),
                professionalUser());

        assertNotNull(entity.getAvailabilities());
        assertTrue(entity.getAvailabilities().isEmpty());
    }

    @Test
    @DisplayName("toEntity deve deixar a lista de disponibilidades vazia quando o DTO traz lista vazia")
    void toEntity_ShouldKeepAvailabilitiesEmpty_WhenListIsEmpty() {
        HealthProfessional entity = healthProfessionalMapper.toEntity(
                createDto(List.of()),
                serviceAreaResponse(),
                professionalUser());

        assertTrue(entity.getAvailabilities().isEmpty());
    }

    @Test
    @DisplayName("toEntity deve gravar no usuario o endereco derivado do DTO")
    void toEntity_ShouldUpdateUserAddress() {
        User user = professionalUser();
        assertNull(user.getAddress());

        HealthProfessional entity = healthProfessionalMapper.toEntity(createDto(List.of()), serviceAreaResponse(), user);

        assertSame(entity.getAddress(), user.getAddress());
        assertEquals(CITY, user.getAddress().getCity());
        assertEquals(CEP, user.getAddress().getCep());
        assertEquals(STATE, user.getAddress().getState());
        assertEquals(NEIGHBORHOOD, user.getAddress().getNeighborhood());
        assertEquals(STREET, user.getAddress().getStreet());
        assertEquals(NUMBER, user.getAddress().getNumber());
        assertEquals("Apto 10", user.getAddress().getComplement());
    }

    @Test
    @DisplayName("toEntity deve propagar a excecao do AvailabilityMapper para dia invalido")
    void toEntity_ShouldThrowIllegalArgumentException_WhenAvailabilityDayIsInvalid() {
        CreateHealthProfessionalDTO dto = createDto(List.of(new CreateAvailabilityDTO("FUNDO", "MANHA")));

        assertThrows(IllegalArgumentException.class,
                () -> healthProfessionalMapper.toEntity(dto, serviceAreaResponse(), professionalUser()));
    }

    @Test
    @DisplayName("toEntity ignora a area aninhada no DTO e usa apenas o parametro serviceAreaDto")
    void toEntity_ShouldIgnoreNestedServiceAreaAndUseServiceAreaResponseParam() {
        HealthProfessional entity = healthProfessionalMapper.toEntity(
                createDto(List.of()),
                serviceAreaResponse(),
                professionalUser());

        assertEquals(SERVICE_AREA_NAME, entity.getServiceArea().getArea());
        assertFalse("area ignorada do DTO".equals(entity.getServiceArea().getArea()));
    }

    @Test
    @DisplayName("updateEntityFromDto deve atualizar todos os campos escalares")
    void updateEntityFromDto_ShouldUpdateAllScalarFields() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);

        HealthProfessional result = healthProfessionalMapper.updateEntityFromDto(
                professional, updateDto(null), new ServiceAreaResponseDTO(2, "Psicologia"));

        assertEquals("Maria Souza", result.getName());
        assertEquals("novo@apae.org.br", result.getEmail());
        assertEquals("987.654.321-00", result.getCpf());
        assertEquals("11888888888", result.getPhoneNumber());
        assertEquals("987654321", result.getIdentityDocument());
        assertEquals("CRP-99999", result.getProfessionalDocument());
        assertEquals("http://example.com/nova-foto.jpg", result.getProfilePhoto());
        assertEquals(Integer.valueOf(2), result.getServiceArea().getId());
        assertEquals("Psicologia", result.getServiceArea().getArea());
    }

    @Test
    @DisplayName("updateEntityFromDto deve substituir a lista de disponibilidades mantendo a mesma instancia")
    void updateEntityFromDto_ShouldReplaceAvailabilitiesAndKeepSameInstance() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);
        professional.addAvailability(new Availability(Day.SEXTA, Shift.MANHA, professional));

        HealthProfessional result = healthProfessionalMapper.updateEntityFromDto(
                professional,
                updateDto(List.of(new CreateAvailabilityDTO("QUARTA", "TARDE"))),
                new ServiceAreaResponseDTO(2, "Psicologia"));

        assertSame(professional, result);
        assertEquals(1, result.getAvailabilities().size());
        assertEquals(Day.QUARTA, result.getAvailabilities().get(0).getDay());
        assertEquals(Shift.TARDE, result.getAvailabilities().get(0).getShift());
        assertSame(professional, result.getAvailabilities().get(0).getProfessional());
    }

    @Test
    @DisplayName("updateEntityFromDto deve preservar as disponibilidades quando o DTO traz null")
    void updateEntityFromDto_ShouldKeepAvailabilities_WhenListIsNull() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);
        professional.addAvailability(new Availability(Day.SEXTA, Shift.MANHA, professional));

        HealthProfessional result = healthProfessionalMapper.updateEntityFromDto(
                professional, updateDto(null), new ServiceAreaResponseDTO(2, "Psicologia"));

        assertEquals(1, result.getAvailabilities().size());
        assertEquals(Day.SEXTA, result.getAvailabilities().get(0).getDay());
    }

    @Test
    @DisplayName("updateEntityFromDto deve criar o endereco quando o profissional ainda nao tem um")
    void updateEntityFromDto_ShouldCreateAddress_WhenProfessionalHasNoAddress() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);
        assertNull(professional.getAddress());

        HealthProfessional result = healthProfessionalMapper.updateEntityFromDto(
                professional, updateDto(null), new ServiceAreaResponseDTO(2, "Psicologia"));

        assertNotNull(result.getAddress());
        assertEquals("Curitiba", result.getAddress().getCity());
        assertEquals("80000-000", result.getAddress().getCep());
        assertEquals("PR", result.getAddress().getState());
        assertEquals("Batel", result.getAddress().getNeighborhood());
        assertEquals("Rua B", result.getAddress().getStreet());
        assertEquals("456", result.getAddress().getNumber());
        assertEquals("Sala 2", result.getAddress().getComplement());
    }

    @Test
    @DisplayName("updateEntityFromDto deve preservar id e situacao ativa")
    void updateEntityFromDto_ShouldKeepIdAndAtivo() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);

        HealthProfessional result = healthProfessionalMapper.updateEntityFromDto(
                professional, updateDto(null), new ServiceAreaResponseDTO(2, "Psicologia"));

        assertEquals(PROFESSIONAL_ID, result.getId());
        assertTrue(result.getAtivo());
    }

    @Test
    @DisplayName("toResponseDTO deve mapear todos os campos da entidade")
    void toResponseDTO_ShouldMapAllFields() {
        HealthProfessional professional = new HealthProfessional(
                PROFESSIONAL_ID, professionalUser(), new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME),
                PROFESSIONAL_DOCUMENT);
        professional.setProfilePhoto(PROFILE_PHOTO);
        professional.addAvailability(new Availability(Day.SEGUNDA, Shift.MANHA, professional));
        professional.addAvailability(new Availability(Day.TERCA, Shift.TARDE, professional));
        professional.getUser().updateAddress(new AddressMapper().toEntity(address()));

        HealthProfessionalResponseDTO dto = healthProfessionalMapper.toResponseDTO(professional);

        assertEquals(PROFESSIONAL_ID, dto.id());
        assertEquals(professional.getUserId(), dto.userId());
        assertEquals(NAME, dto.name());
        assertEquals(EMAIL, dto.email());
        assertEquals(CPF, dto.cpf());
        assertEquals(PROFESSIONAL_DOCUMENT, dto.professionalDocument());
        assertEquals(IDENTITY_DOCUMENT, dto.identityDocument());
        assertEquals(PHONE, dto.phoneNumber());
        assertEquals(SERVICE_AREA_NAME, dto.healthSector());
        assertTrue(dto.ativo());
        assertEquals(PROFILE_PHOTO, dto.profilePhoto());
        assertNotNull(dto.serviceArea());
        assertEquals(SERVICE_AREA_ID, dto.serviceArea().id());
        assertEquals(SERVICE_AREA_NAME, dto.serviceArea().area());
        assertNotNull(dto.address());
        assertEquals(professional.getAddress().getId(), dto.address().id());
        assertEquals(CITY, dto.address().city());
        assertEquals(CEP, dto.address().cep());
        assertEquals(STATE, dto.address().state());
        assertEquals(NEIGHBORHOOD, dto.address().neighborhood());
        assertEquals(STREET, dto.address().street());
        assertEquals(NUMBER, dto.address().number());
        assertEquals("Apto 10", dto.address().complement());
        assertEquals(2, dto.availabilities().size());
        assertEquals("SEGUNDA", dto.availabilities().get(0).day());
        assertEquals("MANHA", dto.availabilities().get(0).shift());
        assertEquals("TERCA", dto.availabilities().get(1).day());
        assertEquals("TARDE", dto.availabilities().get(1).shift());
    }

    @Test
    @DisplayName("toResponseDTO deve devolver lista vazia quando a entidade devolve disponibilidades nulas")
    void toResponseDTO_ShouldReturnEmptyAvailabilities_WhenEntityReturnsNull() {
        HealthProfessional professional = mock(HealthProfessional.class);
        when(professional.getServiceArea()).thenReturn(new ServiceArea(SERVICE_AREA_ID, SERVICE_AREA_NAME));
        when(professional.getAvailabilities()).thenReturn(null);

        HealthProfessionalResponseDTO dto = healthProfessionalMapper.toResponseDTO(professional);

        assertNotNull(dto.availabilities());
        assertTrue(dto.availabilities().isEmpty());
        assertEquals(SERVICE_AREA_NAME, dto.healthSector());
    }

    @Test
    @DisplayName("toResponseDTO falha quando o profissional nao tem area de atendimento, pre-condicao do mapper")
    void toResponseDTO_ShouldThrowNullPointerException_WhenServiceAreaIsNull() {
        HealthProfessional professional = mock(HealthProfessional.class);
        when(professional.getServiceArea()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> healthProfessionalMapper.toResponseDTO(professional));
    }
}
