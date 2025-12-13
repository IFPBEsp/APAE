package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

        private final AddressMapper addressMapper;
        private final VaccineMapper vaccineMapper;

        public PatientMapper(AddressMapper addressMapper,
                        VaccineMapper vaccineMapper) {
                this.addressMapper = addressMapper;
                this.vaccineMapper = vaccineMapper;
        }

        public Patient toEntity(CreatePatientDTO dto, AddressResponseDTO addressDto,
                        Set<VaccineResponseDTO> vaccineDtos) {

                Address address = addressMapper.toEntityFromResponse(addressDto);
                Set<Vaccine> vaccines = vaccineMapper.toEntitySetFromResponse(vaccineDtos);

                PersonalInfo personalInfo = new PersonalInfo(
                                dto.fullName(),
                                dto.nationality(),
                                dto.birthDate(),
                                dto.contact(),
                                dto.allergies(),
                                dto.isStudent());

                BirthRecord birthRecord = new BirthRecord(
                                dto.birthCertificateNumber(),
                                dto.registryOffice(),
                                dto.fls(),
                                dto.book(),
                                dto.registrationDate());

                Identification identification = new Identification(
                                dto.rg(),
                                dto.cpf(),
                                dto.cns(),
                                dto.nis(),
                                dto.issueDate(),
                                dto.issuingAgency());

                return new Patient(personalInfo, birthRecord, identification, address, vaccines);
        }

        public Patient updateEntityFromDto(Patient patient, UpdatePatientDTO dto, AddressResponseDTO addressDto,
                        Set<VaccineResponseDTO> vaccinesDto) {

                Address address = addressMapper.toEntityFromResponse(addressDto);
                Set<Vaccine> vaccines = vaccineMapper.toEntitySetFromResponse(vaccinesDto);

                PersonalInfo personalInfo = new PersonalInfo(
                                dto.fullName(),
                                dto.nationality(),
                                dto.birthDate(),
                                dto.contact(),
                                dto.allergies(),
                                dto.isStudent());

                BirthRecord birthRecord = new BirthRecord(
                                dto.birthCertificateNumber(),
                                dto.registryOffice(),
                                dto.fls(),
                                dto.book(),
                                dto.registrationDate());

                Identification identification = new Identification(
                                dto.rg(),
                                dto.cpf(),
                                dto.cns(),
                                dto.nis(),
                                dto.issueDate(),
                                dto.issuingAgency());

                return new Patient(patient.getId(), personalInfo, birthRecord, identification, address, vaccines);
        }

        public PatientSummaryResponseDTO toSummaryResponseDTO(Patient patient, String photoUrl) {
                AddressResponseDTO addressResponseDTO = new AddressResponseDTO(patient.getAddress());

                return new PatientSummaryResponseDTO(patient, addressResponseDTO,  photoUrl);
        }

        public PatientResponseDTO toResponseDTO(Patient patient, GuardianResponseDTO guardianResponseDTO,
                        List<ParentResponseDTO> parentResponseDTOs, String photoUrl) {
                AddressResponseDTO addressResponseDTO = new AddressResponseDTO(patient.getAddress());
                Set<VaccineResponseDTO> vaccineResponseDTOs = patient.getVaccines().stream()
                                .map(VaccineResponseDTO::new)
                                .collect(Collectors.toSet());

                return new PatientResponseDTO(patient, addressResponseDTO, guardianResponseDTO, parentResponseDTOs,
                                vaccineResponseDTOs, photoUrl);
        }
}
