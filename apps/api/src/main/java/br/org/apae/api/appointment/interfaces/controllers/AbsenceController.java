package br.org.apae.api.appointment.interfaces.controllers;

import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Faltas (Absence)", description = "Endpoints para registro e consulta de faltas em agendamentos gerados.")
@RequestMapping("/absences")
public interface AbsenceController {

    @Operation(summary = "Registrar nova falta", description = "Registra uma falta vinculada a um Agendamento Gerado existente. A falta deve ser única por agendamento.")
    @PostMapping
    ResponseEntity<AbsenceResponseDTO> register(@RequestBody @Valid CreateAbsenceDTO dto);

    @Operation(summary = "Listar faltas com filtros", description = "Lista todas as faltas registradas com opções de filtros dinâmicos e paginação.")
    @GetMapping
    ResponseEntity<Page<AbsenceResponseDTO>> findAll(
            @RequestParam(required = false) @Schema(description = "ID do Agendamento Gerado (GeneratedAppointment) para filtro.")
            UUID generatedId,
            @RequestParam(required = false) @Schema(description = "ID do Paciente (Patient) para filtro.")
            UUID patientId,
            @RequestParam(required = false) @Schema(description = "ID do Profissional (Professional) para filtro.")
            UUID professionalId,
            Pageable pageable
    );
}