package br.org.apae.api.common.dto.dashboard.response;

public record DashboardOverviewResponseDTO(
        long totalPatients,
        long totalAppointments,
        long totalPatientsWithAbsences
) {}
