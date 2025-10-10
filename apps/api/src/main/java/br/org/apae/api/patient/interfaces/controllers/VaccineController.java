package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.vaccine.create.VaccineCreateDTO;
import br.org.apae.api.common.dto.vaccine.response.VaccineResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<VaccineResponseDTO> createVaccine(@RequestBody @Valid VaccineCreateDTO vaccineDTO);

    @Operation(summary = "Buscar vacina por ID", description = "Retorna os dados de uma vacina específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<VaccineResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Listar todas as vacinas", description = "Retorna uma página de vacinas. Suporta paginação e ordenação via parâmetros de URL (ex: ?page=0&size=10&sort=name,asc).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de vacinas retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<Page<VaccineResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Atualizar uma vacina", description = "Atualiza os dados de uma vacina existente a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada"),
            @ApiResponse(responseCode = "409", description = "O nome informado já está em uso por outra vacina")
    })
    @PutMapping("/{id}")
    ResponseEntity<VaccineResponseDTO> updateVaccine(@PathVariable Long id, @RequestBody @Valid VaccineCreateDTO vaccineDTO);

    @Operation(summary = "Excluir uma vacina", description = "Remove uma vacina do sistema a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacina excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vacina não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteVaccine(@PathVariable Long id);
}