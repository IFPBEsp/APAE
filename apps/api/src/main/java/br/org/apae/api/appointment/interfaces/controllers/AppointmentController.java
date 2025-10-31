package br.org.apae.api.appointment.interfaces.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.appointment.request.appointment.CreateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.request.appointment.UpdateAppointmentDTO;
import br.org.apae.api.common.dto.appointment.response.appointment.AppointmentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/appointments")
public interface AppointmentController {

  @Operation(summary = "Criar um novo agendamento", description = "Cadastra um novo agendamento no sistema.", responses = {
      @ApiResponse(responseCode = "200", description = "Agendamento criado com sucesso."),
      @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @PostMapping
  ResponseEntity<Void> create(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do agendamento a ser criado.", required = true, content = @Content(schema = @Schema(implementation = CreateAppointmentDTO.class))) @RequestBody @Valid CreateAppointmentDTO dto);

  @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos cadastrados. É possível filtrar por data ou por data e hora.", parameters = {
      @Parameter(name = "date", description = "Data do agendamento (opcional).", example = "2025-10-12"),
      @Parameter(name = "time", description = "Hora do agendamento (opcional).", example = "14:30:00"),
      @Parameter(name = "pageable", description = "Informações de paginação (objeto Pageable do Spring Data).")
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Agendamentos retornados com sucesso.", content = @Content(schema = @Schema(implementation = AppointmentResponseDTO.class))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @GetMapping
  ResponseEntity<Page<AppointmentResponseDTO>> getAll(
      @RequestParam(required = false) LocalDate date,
      @RequestParam(required = false) LocalTime time,
      Pageable pageable);

  @Operation(summary = "Buscar agendamento por ID", description = "Retorna os detalhes de um agendamento específico a partir do seu identificador único (UUID).", parameters = @Parameter(name = "id", description = "UUID do agendamento.", required = true), responses = {
      @ApiResponse(responseCode = "200", description = "Agendamento encontrado.", content = @Content(schema = @Schema(implementation = AppointmentResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<AppointmentResponseDTO> get(@PathVariable UUID id);

  @Operation(summary = "Atualizar um agendamento existente", description = "Atualiza as informações de um agendamento já cadastrado no sistema.", parameters = @Parameter(name = "id", description = "UUID do agendamento a ser atualizado.", required = true), responses = {
      @ApiResponse(responseCode = "200", description = "Agendamento atualizado com sucesso.", content = @Content(schema = @Schema(implementation = AppointmentResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @PutMapping("/{id}")
  ResponseEntity<AppointmentResponseDTO> update(
      @PathVariable UUID id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do agendamento a serem atualizados.", required = true, content = @Content(schema = @Schema(implementation = UpdateAppointmentDTO.class))) @RequestBody @Valid UpdateAppointmentDTO dto);

  @Operation(summary = "Excluir um agendamento", description = "Remove um agendamento do sistema a partir do seu identificador único (UUID).", parameters = @Parameter(name = "id", description = "UUID do agendamento a ser excluído.", required = true), responses = {
      @ApiResponse(responseCode = "204", description = "Agendamento excluído com sucesso."),
      @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable UUID id);
}
