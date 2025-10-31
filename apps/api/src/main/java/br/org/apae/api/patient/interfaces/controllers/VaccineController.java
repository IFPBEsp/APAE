package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.vaccine.create.CreateVaccineDTO;
import br.org.apae.api.common.dto.vaccine.response.ResponseVaccineDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/vaccines")
@Tag(name = "Vaccines", description = "Endpoints para gerenciamento de vacinas")
public interface VaccineController {

    @Operation(summary = "Cadastrar uma nova vacina", description = "Cria uma nova vacina no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacina cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Já existe uma vacina com este nome")
    })
    @PostMapping
    ResponseEntity<ResponseVaccineDTO> createVaccine(@RequestBody @Valid CreateVaccineDTO vaccineDTO);

    @Operation(summary = "Buscar vacina por ID", description = "Retorna os dados de uma vacina específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<ResponseVaccineDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Listar todas as vacinas", description = "Retorna a lista completa de vacinas cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vacinas retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<List<ResponseVaccineDTO>> findAll();

    @Operation(summary = "Buscar vacina por nome", description = "Retorna os dados de uma vacina específica pelo seu nome único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    @GetMapping("/search/by-name")
    ResponseEntity<ResponseVaccineDTO> findByName(@RequestParam String name);

    @Operation(summary = "Atualizar uma vacina", description = "Atualiza os dados de uma vacina existente a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
            @ApiResponse(responseCode = "409", description = "O nome informado já está em uso por outra vacina")
    })
    @PutMapping("/{id}")
    ResponseEntity<ResponseVaccineDTO> updateVaccine(@PathVariable UUID id, @RequestBody @Valid CreateVaccineDTO vaccineDTO);

    @Operation(summary = "Excluir uma vacina", description = "Remove uma vacina do sistema a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacina excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVaccine(@PathVariable UUID id);
}