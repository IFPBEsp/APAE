package br.org.apae.api.dashboard.interfaces.controllers;

import br.org.apae.api.common.dto.dashboard.response.DashboardOverviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Dashboard", description = "Endpoints de visão geral do sistema")
@RequestMapping("/dashboard")
public interface DashboardController {

    @Operation(
            summary = "Visão geral do dashboard",
            description = "Retorna KPIs principais do sistema"
    )
    @GetMapping("/overview")
    ResponseEntity<DashboardOverviewResponseDTO> getOverview(
            @RequestParam(defaultValue = "3") int minAbsences
    );
}
