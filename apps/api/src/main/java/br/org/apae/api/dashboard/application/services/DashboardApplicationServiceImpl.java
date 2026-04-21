package br.org.apae.api.dashboard.application.services;

import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;
import br.org.apae.api.dashboard.application.interfaces.DashboardApplicationService;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardApplicationServiceImpl implements DashboardApplicationService {

    private final PatientRepository patientRepository;
    private final AbsenceRepository absenceRepository;

    public DashboardApplicationServiceImpl(
            PatientRepository patientRepository,
            AbsenceRepository absenceRepository
    ) {
        this.patientRepository = patientRepository;
        this.absenceRepository = absenceRepository;
    }

    @Override
    public DashboardOverviewResponseDTO getOverview(int minAbsences) {
        long totalPatients = patientRepository.count();
        long totalPatientsWithAbsences = absenceRepository.countPatientsWithAbsences(minAbsences);

        return new DashboardOverviewResponseDTO(
                totalPatients,
                totalPatientsWithAbsences
        );
    }
}
