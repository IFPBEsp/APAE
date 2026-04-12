package br.org.apae.api.controllers.dashboard;

import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;
import br.org.apae.api.dashboard.application.interfaces.DashboardApplicationService;
import br.org.apae.api.dashboard.interfaces.controllers.DashboardController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class DashboardControllerImpl implements DashboardController {

    private final DashboardApplicationService dashboardService;

    public DashboardControllerImpl(DashboardApplicationService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    public ResponseEntity<DashboardOverviewResponseDTO> getOverview(@RequestParam(defaultValue = "3") int minAbsences) {
        return ResponseEntity.ok(dashboardService.getOverview(minAbsences));
    }
}
