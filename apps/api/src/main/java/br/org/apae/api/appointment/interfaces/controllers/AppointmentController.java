package br.org.apae.api.appointment.interfaces.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.appointment.request.appointment.*;
import br.org.apae.api.common.dto.appointment.response.appointment.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/appointments")
public interface AppointmentController {

  @Operation(summary = "Criar um novo agendamento (regra de recorrência)", description = "Cadastra uma nova regra de agendamento recorrente.")
  @PostMapping
  ResponseEntity<Void> create(@RequestBody @Valid CreateAppointmentDTO dto);

  @Operation(summary = "Listar agendamentos (regras)", description = "Lista todas as regras de agendamento. Pode filtrar por data/hora.")
  @GetMapping
  ResponseEntity<Page<AppointmentResponseDTO>> getAll(
          @RequestParam(required = false) LocalDate date,
          @RequestParam(required = false) LocalTime time,
          Pageable pageable);

  @Operation(summary = "Buscar regra por ID")
  @GetMapping("/{id}")
  ResponseEntity<AppointmentResponseDTO> get(@PathVariable UUID id);

  // REMOVIDO: update antigo

  @Operation(summary = "Atualizar regra de agendamento (nova versão com histórico)",
          description = "Desativa a regra atual e cria uma nova com nova frequência/horário. Gera novos agendamentos futuros.")
  @PatchMapping("/{id}/rule")
  ResponseEntity<AppointmentResponseDTO> updateRule(
          @PathVariable UUID id,
          @RequestBody @Valid UpdateAppointmentRuleDTO dto);

  @Operation(summary = "Excluir regra de agendamento")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable UUID id);


  @Operation(summary = "Reagendar um agendamento gerado")
  @PatchMapping("/generated/{id}/reschedule")
  ResponseEntity<GeneratedAppointmentResponseDTO> reschedule(
          @PathVariable UUID id,
          @RequestBody @Valid RescheduleGeneratedAppointmentDTO dto);

  @Operation(summary = "Marcar agendamento gerado como realizado")
  @PatchMapping("/generated/{id}/performed")
  ResponseEntity<GeneratedAppointmentResponseDTO> markAsPerformed(@PathVariable UUID id);

  @Operation(summary = "Cancelar agendamento gerado")
  @PatchMapping("/generated/{id}/cancel")
  ResponseEntity<GeneratedAppointmentResponseDTO> cancel(
          @PathVariable UUID id,
          @RequestBody @Valid CancelGeneratedAppointmentDTO dto);

  @Operation(summary = "Listar agendamentos gerados de um paciente em um período")
  @GetMapping("/patient/{patientId}")
  ResponseEntity<Page<GeneratedAppointmentResponseDTO>> listByPatient(
          @PathVariable UUID patientId,
          @RequestParam LocalDate start,
          @RequestParam LocalDate end,
          Pageable pageable);

  @Operation(summary = "Lista agendamentos de hoje")
  @GetMapping("/today")
  ResponseEntity<Page<TodayAppointmentsResponseDTO>> listTodayAppointment(Pageable pageable);

  @Operation(summary = "Buscar agendamento gerado por ID")
  @GetMapping("/today/{id}")
  ResponseEntity<TodayAppointmentsResponseDTO> getTodayAppointmentById(@PathVariable UUID id);
}