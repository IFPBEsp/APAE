package br.org.apae.api.appointment.interfaces.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.appointment.request.consultation_history.CreateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.request.consultation_history.UpdateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.response.consultation_history.ConsultationHistoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/api/consultation-histories")
public interface ConsultationHistoryController {

  @Operation(summary = "Criar um novo histórico de consulta", description = "Registra um novo histórico de consulta para um agendamento. "
      +
      "Caso a consulta não tenha sido realizada, é necessário informar uma justificativa.", responses = {
          @ApiResponse(responseCode = "200", description = "Histórico de consulta criado com sucesso."),
          @ApiResponse(responseCode = "400", description = "Dados inválidos ou justificativa ausente para consulta não realizada.", content = @Content),
          @ApiResponse(responseCode = "409", description = "Já existe um histórico de consulta para o mesmo agendamento, data e hora.", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
      })
  @PostMapping
  ResponseEntity<Void> create(
      @RequestBody @Valid CreateConsultationHistoryDTO dto);

  @Operation(summary = "Listar todos os históricos de consulta", description = "Recupera uma lista paginada de todos os históricos de consulta registrados no sistema.", parameters = {
      @Parameter(name = "page", description = "Número da página a ser retornada (inicia em 0).", required = false),
      @Parameter(name = "size", description = "Quantidade de itens por página.", required = false),
      @Parameter(name = "sort", description = "Critério de ordenação (ex: data,asc).", required = false)
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Lista de históricos de consulta retornada com sucesso.", content = @Content(schema = @Schema(implementation = ConsultationHistoryResponseDTO.class))),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @GetMapping
  ResponseEntity<Page<ConsultationHistoryResponseDTO>> findAll(Pageable pageable);

  @Operation(summary = "Buscar um histórico de consulta por ID", description = "Recupera as informações detalhadas de um histórico de consulta a partir de seu identificador único (UUID).", parameters = @Parameter(name = "id", description = "UUID do histórico de consulta a ser buscado.", required = true), responses = {
      @ApiResponse(responseCode = "200", description = "Histórico de consulta encontrado com sucesso.", content = @Content(schema = @Schema(implementation = ConsultationHistoryResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Histórico de consulta não encontrado.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<ConsultationHistoryResponseDTO> findById(@PathVariable UUID id);

  @Operation(summary = "Atualizar um histórico de consulta", description = "Atualiza as informações de um histórico de consulta existente. "
      +
      "Caso a consulta não tenha sido realizada, uma justificativa é obrigatória.", parameters = @Parameter(name = "id", description = "UUID do histórico de consulta a ser atualizado.", required = true), responses = {
          @ApiResponse(responseCode = "200", description = "Histórico de consulta atualizado com sucesso.", content = @Content(schema = @Schema(implementation = ConsultationHistoryResponseDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos ou justificativa ausente.", content = @Content),
          @ApiResponse(responseCode = "404", description = "Histórico de consulta não encontrado.", content = @Content),
          @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
      })
  @PutMapping("/{id}")
  ResponseEntity<ConsultationHistoryResponseDTO> update(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateConsultationHistoryDTO dto);

  @Operation(summary = "Excluir um histórico de consulta", description = "Remove um histórico de consulta do sistema com base em seu identificador (UUID).", parameters = @Parameter(name = "id", description = "UUID do histórico de consulta a ser removido.", required = true), responses = {
      @ApiResponse(responseCode = "204", description = "Histórico de consulta excluído com sucesso."),
      @ApiResponse(responseCode = "404", description = "Histórico de consulta não encontrado.", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno do servidor.", content = @Content)
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> delete(@PathVariable UUID id);
}
