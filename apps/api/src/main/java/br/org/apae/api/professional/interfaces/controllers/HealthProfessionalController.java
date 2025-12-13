package br.org.apae.api.professional.interfaces.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequestMapping("/professionals")
public interface HealthProfessionalController {
  @Operation(summary = "Cadastrar profissional de saúde", description = "Cria um novo profissional de saúde no sistema.", responses = {
      @ApiResponse(responseCode = "201", description = "Profissional criado com sucesso", content = @Content(schema = @Schema(implementation = HealthProfessionalResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
  })
  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  ResponseEntity<HealthProfessionalResponseDTO> createHealthProfessional(@RequestPart("professional") CreateHealthProfessionalDTO dto,
    @ModelAttribute @Valid CreateProfessionalDocumentsDTO documentsDTO);

  @Operation(summary = "Listar profissionais de saúde", description = "Retorna uma lista paginada de todos os profissionais de saúde cadastrados.", responses = {
      @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
  })
  @GetMapping
  ResponseEntity<Page<HealthProfessionalResponseDTO>> getAllHealthProfessional(Pageable pageable);

  @Operation(summary = "Excluir profissional de saúde", description = "Remove um profissional de saúde pelo seu identificador (UUID).", responses = {
      @ApiResponse(responseCode = "204", description = "Profissional excluído com sucesso", content = @Content),
      @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteHealthProfessional(@PathVariable UUID id);

  @Operation(summary = "Buscar profissional de saúde por ID", description = "Obtém os dados de um profissional de saúde específico através do seu identificador (UUID).", responses = {
      @ApiResponse(responseCode = "200", description = "Profissional encontrado", content = @Content(schema = @Schema(implementation = HealthProfessionalResponseDTO.class))),
      @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
  })
  @GetMapping("/{id}")
  ResponseEntity<HealthProfessionalResponseDTO> findByIdHealthProfessional(@PathVariable UUID id);

  @Operation(summary = "Atualizar profissional de saúde", description = "Atualiza as informações de um profissional de saúde existente.", responses = {
      @ApiResponse(responseCode = "200", description = "Profissional atualizado com sucesso", content = @Content(schema = @Schema(implementation = HealthProfessionalResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
      @ApiResponse(responseCode = "404", description = "Profissional não encontrado", content = @Content),
      @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
  })
  @PutMapping("/{id}")
  ResponseEntity<HealthProfessionalResponseDTO> updateHealthProfessional(@PathVariable UUID id,
      @Valid @RequestBody UpdateHealthProfessionalDTO dto);
}
