package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.model.patient.BirthRecord;
import br.org.apae.api.patient.domain.model.patient.Identification;
import br.org.apae.api.patient.domain.model.patient.PersonalInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientMapperTest {

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private VaccineMapper vaccineMapper;

    @InjectMocks
    private PatientMapper patientMapper;

    private Address dummyAddress;
    private Set<Vaccine> dummyVaccines;
    private Set<VaccineResponseDTO> vaccineDtos;
    private UUID patientId;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        dummyAddress = new Address("Cidade", "12345-678", "SP", "Bairro", "Rua A", "123", "Comple");
        dummyVaccines = Set.of(new Vaccine(UUID.randomUUID(), "BCG"));
        vaccineDtos = Set.of(new VaccineResponseDTO(UUID.randomUUID(), "BCG", true));
    }

    @Test
    void toEntity_ShouldMapAllFields_WhenGivenValidDTO() {
        CreateAddressDTO addressDto = new CreateAddressDTO("Cidade", "12345-678", "SP", "Bairro", "Rua A", "123", "Comple");
        CreatePatientDTO dto = new CreatePatientDTO(
                "Paciente Teste", "Brasileira", LocalDate.of(2010, 1, 1), "999999999",
                "123456", "Cartório X", "12", "34", "11.111.111-1", LocalDate.of(2010, 1, 5), "SSP",
                "111.111.111-11", "12345", "67890", LocalDate.of(2020, 1, 1), "Nenhuma", true,
                addressDto, null, null, Set.of(), null
        );

        when(addressMapper.toEntity(addressDto)).thenReturn(dummyAddress);
        when(vaccineMapper.toEntitySetFromResponse(vaccineDtos)).thenReturn(dummyVaccines);

        Patient entity = patientMapper.toEntity(dto, vaccineDtos);

        assertNotNull(entity);
        assertEquals(dummyAddress, entity.getAddress());
        assertEquals(dummyVaccines, entity.getVaccines());
        assertEquals(dto.fullName(), entity.getFullName());
        assertEquals(dto.nationality(), entity.getBirthplace());
        assertEquals(dto.birthDate(), entity.getBirthDate());
        assertEquals(dto.contact(), entity.getContact());
        assertEquals(dto.allergies(), entity.getAllergies());
        assertEquals(dto.isStudent(), entity.isStudent());
        assertEquals(dto.birthCertificateNumber(), entity.getBirthCertificateNumber());
        assertEquals(dto.registryOffice(), entity.getRegistryOffice());
        assertEquals(dto.fls(), entity.getFls());
        assertEquals(dto.book(), entity.getBook());
        assertEquals(dto.registrationDate(), entity.getRegistrationDate());
        assertEquals(dto.rg(), entity.getRg());
        assertEquals(dto.cpf(), entity.getCpf());
        assertEquals(dto.cns(), entity.getCns());
        assertEquals(dto.nis(), entity.getNis());
        assertEquals(dto.issueDate(), entity.getIssueDate());
        assertEquals(dto.issuingAgency(), entity.getIssuingAgency());
    }

    @Test
    void toEntity_ShouldThrow_WhenMandatoryFieldsAreNull() {
        CreatePatientDTO dto = new CreatePatientDTO(
                null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, false,
                null, null, null, null, null
        );

        when(addressMapper.toEntity(null)).thenReturn(null);
        when(vaccineMapper.toEntitySetFromResponse(null)).thenReturn(Set.of());

        assertThrows(Exception.class, () -> patientMapper.toEntity(dto, null));
    }

    @Test
    void updateEntityFromDto_ShouldMapAllFields() {
        Patient existingPatient = new Patient(
                patientId,
                new PersonalInfo("Old Name", "BR", LocalDate.now(), "000", "None", false),
                new BirthRecord("000", "0", "0", "0", LocalDate.of(2000, 1, 1)),
                new Identification("0", "0", "0", "0", LocalDate.now(), "SSP"),
                dummyAddress, Set.of()
        );

        CreateAddressDTO addressDto = new CreateAddressDTO("Cidade 2", "87654-321", "RJ", "Bairro 2", "Rua B", "321", "C");
        UpdateGuardianDTO guardianDto = new UpdateGuardianDTO("G", "1", "Mãe", null);

        UpdatePatientDTO dto = new UpdatePatientDTO(
                "New Name", "BR", LocalDate.now(), "111", "111", "1", "1", "1",
                "1", LocalDate.now(), "SSP", "1", "1", "1", "None", true,
                addressDto, guardianDto, List.of(), Set.of()
        );

        when(addressMapper.toEntity(addressDto)).thenReturn(dummyAddress);
        when(vaccineMapper.toEntitySetFromResponse(vaccineDtos)).thenReturn(dummyVaccines);

        Patient updated = patientMapper.updateEntityFromDto(existingPatient, dto, vaccineDtos);

        assertNotNull(updated);
        assertEquals(existingPatient.getId(), updated.getId());
        assertEquals(existingPatient.getRegistrationDate(), updated.getRegistrationDate());
        assertEquals(dto.fullName(), updated.getFullName());
        assertEquals(dto.birthCertificateNumber(), updated.getBirthCertificateNumber());
    }

    @Test
    void toSummaryResponseDTO_ShouldMapCorrectly() {
        Patient patient = new Patient(
                patientId,
                new PersonalInfo("Paciente", "BR", LocalDate.now(), "123", "None", true),
                new BirthRecord("1", "1", "1", "1", LocalDate.now()),
                new Identification("1", "1", "1", "1", LocalDate.now(), "SSP"),
                dummyAddress, dummyVaccines
        );
        String photoUrl = "http://foto.com";

        PatientSummaryResponseDTO dto = patientMapper.toSummaryResponseDTO(patient, photoUrl);

        assertNotNull(dto);
        assertEquals(patient.getId(), dto.id());
        assertEquals(patient.getFullName(), dto.fullName());
        assertEquals(photoUrl, dto.photoUrl());
        assertNotNull(dto.address());
    }

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        Patient patient = new Patient(
                patientId,
                new PersonalInfo("Paciente", "BR", LocalDate.now(), "123", "None", true),
                new BirthRecord("1", "1", "1", "1", LocalDate.now()),
                new Identification("1", "1", "1", "1", LocalDate.now(), "SSP"),
                dummyAddress, dummyVaccines
        );

        String photoUrl = "http://foto.com";
        GuardianResponseDTO guardianDto = mock(GuardianResponseDTO.class);
        List<ParentResponseDTO> parentsDto = List.of();

        PatientResponseDTO dto = patientMapper.toResponseDTO(patient, guardianDto, parentsDto, photoUrl);

        assertNotNull(dto);
        assertEquals(patient.getId(), dto.id());
        assertEquals(patient.getFullName(), dto.fullName());
        assertEquals(photoUrl, dto.photoUrl());
        assertNotNull(dto.address());
        assertEquals(guardianDto, dto.guardian());
        assertEquals(parentsDto, dto.parents());
        assertEquals(1, dto.vaccineNames().size());
    }
}
