package br.org.apae.api.patient.application.internal;

import br.org.apae.api.appointment.application.interfaces.AppointmentApplicationService;
import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.appointment.mapper.AbsenceMapper;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.GuardianApplicationService;
import br.org.apae.api.patient.application.interfaces.ParentApplicationService;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.application.mappers.PatientMapper;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.projection.PatientWithAbsenceProjection;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("patient")
@Tag("unit")
class PatientApplicationServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PatientDomainService patientDomainService;

    @Mock
    private GuardianApplicationService guardianService;

    @Mock
    private VaccineApplicationService vaccineService;

    @Mock
    private ParentApplicationService parentService;

    @Mock
    private AnnualRegistryApplicationService annualRegistryService;

    @Mock
    private PatientDocumentsService documentService;

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private AbsenceMapper absenceMapper;

    @Mock
    private AppointmentApplicationService appointmentService;

    @InjectMocks
    private PatientApplicationServiceImpl patientApplicationService;

    @ParameterizedTest
    @CsvSource({
            "Fulano da Silva, fulano da silva",
            "FULANO, fulano",
            "'  Fulano  ', fulano"
    })
    void deveNormalizarNomeParaMinusculasAntesDeConsultar(String nomeDigitado, String nomeEsperado) {
        Pageable pageable = PageRequest.of(0, 6);
        when(patientRepository.findPatientsWithAbsences(3, nomeEsperado, pageable))
                .thenReturn(Page.<PatientWithAbsenceProjection>empty());

        Page<?> resultado = patientApplicationService.findPatientsWithAbsences(3, nomeDigitado, pageable);

        verify(patientRepository).findPatientsWithAbsences(3, nomeEsperado, pageable);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveConsultarComNomeVazioQuandoNomeForNulo() {
        Pageable pageable = PageRequest.of(0, 6);
        when(patientRepository.findPatientsWithAbsences(3, "", pageable))
                .thenReturn(Page.<PatientWithAbsenceProjection>empty());

        Page<?> resultado = patientApplicationService.findPatientsWithAbsences(3, null, pageable);

        verify(patientRepository).findPatientsWithAbsences(3, "", pageable);
        assertTrue(resultado.isEmpty());
    }
}
