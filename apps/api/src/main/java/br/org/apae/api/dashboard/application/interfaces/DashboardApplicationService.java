package br.org.apae.api.dashboard.application.interfaces;

import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;

public interface DashboardApplicationService {
    DashboardOverviewResponseDTO getOverview(int minAbsences);
}
