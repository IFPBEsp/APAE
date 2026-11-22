package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.model.patient.BirthRecord;
import br.org.apae.api.patient.domain.model.patient.Identification;
import br.org.apae.api.patient.domain.model.patient.PersonalInfo;
import br.org.apae.api.patient.domain.repository.projection.PatientWithAbsenceProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.default_schema=public",
        "spring.datasource.url=jdbc:h2:mem:patient-like-search;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
@Tag("patient")
class PatientLikeSearchIntegrationTest {

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository.save(patient("Ana", "11111111111", "Campina Grande"));
        patientRepository.save(patient("Bruno", "22222222222", "João Pessoa"));
        patientRepository.flush();
    }

    @Nested
    @DisplayName("PatientSpecification")
    class PatientSpecificationTests {

        @Test
        @DisplayName("Busca por % não deve retornar todos os pacientes")
        void shouldNotTreatPercentAsWildcard() {
            Page<Patient> result = findByFilter("name", "%");

            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Busca por _ não deve retornar todos os pacientes")
        void shouldNotTreatUnderscoreAsWildcard() {
            Page<Patient> result = findByFilter("name", "_");

            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Busca parcial por nome deve continuar funcionando")
        void shouldKeepPartialNameSearchWorking() {
            Page<Patient> result = findByFilter("name", "an");

            assertThat(result.getContent())
                    .extracting(Patient::getFullName)
                    .containsExactly("Ana");
        }

        @Test
        @DisplayName("Busca por cidade deve escapar os curingas do LIKE")
        void shouldEscapeWildcardsInCityFilter() {
            Page<Patient> result = findByFilter("city", "%");

            assertThat(result.getTotalElements()).isZero();
        }

        private Page<Patient> findByFilter(String key, String value) {
            return patientRepository.findAll(
                    PatientSpecification.filterBy(Map.of(key, value)),
                    PageRequest.of(0, 10)
            );
        }
    }

    @Nested
    @DisplayName("PatientRepository.findPatientsWithAbsences")
    class FindPatientsWithAbsencesTests {

        @Test
        @DisplayName("Busca por % não deve retornar todos os pacientes")
        void shouldNotTreatPercentAsWildcard() {
            Page<PatientWithAbsenceProjection> result = findPatientsWithAbsences("%");

            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Busca por _ não deve retornar todos os pacientes")
        void shouldNotTreatUnderscoreAsWildcard() {
            Page<PatientWithAbsenceProjection> result = findPatientsWithAbsences("_");

            assertThat(result.getTotalElements()).isZero();
        }

        @Test
        @DisplayName("Busca parcial por nome deve continuar funcionando")
        void shouldKeepPartialNameSearchWorking() {
            Page<PatientWithAbsenceProjection> result = findPatientsWithAbsences("an");

            assertThat(result.getContent())
                    .extracting(row -> row.getPatient().getFullName())
                    .containsExactly("Ana");
        }

        private Page<PatientWithAbsenceProjection> findPatientsWithAbsences(String name) {
            return patientRepository.findPatientsWithAbsences(0, name, PageRequest.of(0, 10));
        }
    }

    private Patient patient(String fullName, String cpf, String city) {
        PersonalInfo personalInfo = new PersonalInfo(
                fullName,
                "Campina Grande",
                LocalDate.of(2010, 1, 1),
                "8399999999",
                "Nenhuma",
                true
        );
        BirthRecord birthRecord = new BirthRecord(
                "123456789",
                "Cartório X",
                "10",
                "A",
                LocalDate.of(2015, 1, 1)
        );
        Identification identification = new Identification(
                null,
                cpf,
                "123456789",
                null,
                null,
                null
        );
        Address address = new Address(
                city,
                "58000-000",
                "PB",
                "Centro",
                "Rua X",
                "123",
                null
        );

        return new Patient(personalInfo, birthRecord, identification, address, Set.of());
    }
}
