package br.org.apae.api.dashboard.application.services;

import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;
import br.org.apae.api.dashboard.application.interfaces.DashboardApplicationService;
import br.org.apae.api.appointment.domain.repository.AppointmentRepository;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardApplicationServiceImpl implements DashboardApplicationService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DashboardApplicationServiceImpl(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository
    ) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public DashboardOverviewResponseDTO getOverview() {

        long totalPatients = patientRepository.count();

        long totalAppointments = appointmentRepository.countByIsActiveTrue();

        return new DashboardOverviewResponseDTO(
                totalPatients,
                totalAppointments
        );
    }
}
