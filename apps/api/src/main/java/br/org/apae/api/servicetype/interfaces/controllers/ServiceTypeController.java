package br.org.apae.api.servicetype.interfaces.controllers;

import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping({ "/service-types", "/service-areas" })
public interface ServiceTypeController {

    @Operation(summary = "Cadastrar tipo de atendimento", description = "Cria um novo tipo de atendimento no sistema.", responses = {
            @ApiResponse(responseCode = "201", description = "Tipo de atendimento criado com sucesso", content = @Content(schema = @Schema(implementation = ServiceTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tipo de atendimento já existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<ServiceTypeResponseDTO> createServiceType(CreateServiceTypeDTO dto);

    @Operation(summary = "Listar tipos de atendimento", description = "Retorna uma lista de todos os tipos de atendimento cadastrados.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceTypeResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<List<ServiceTypeResponseDTO>> getAllServiceTypes();

    @Operation(summary = "Excluir tipo de atendimento", description = "Remove um tipo de atendimento pelo seu identificador.", responses = {
            @ApiResponse(responseCode = "204", description = "Tipo de atendimento excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo de atendimento não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteServiceType(@PathVariable Integer id);

    @Operation(summary = "Buscar tipo de atendimento por ID", description = "Obtém os dados de um tipo de atendimento específico através do seu identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Tipo de atendimento encontrado", content = @Content(schema = @Schema(implementation = ServiceTypeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de atendimento não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<ServiceTypeResponseDTO> findByIdServiceType(@PathVariable Integer id);

    @Operation(summary = "Atualizar tipo de atendimento", description = "Atualiza as informações de um tipo de atendimento existente.", responses = {
            @ApiResponse(responseCode = "200", description = "Tipo de atendimento atualizado com sucesso", content = @Content(schema = @Schema(implementation = ServiceTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo de atendimento não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Tipo de atendimento já existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<ServiceTypeResponseDTO> updateServiceType(@PathVariable Integer id, UpdateServiceTypeDTO dto);
}

