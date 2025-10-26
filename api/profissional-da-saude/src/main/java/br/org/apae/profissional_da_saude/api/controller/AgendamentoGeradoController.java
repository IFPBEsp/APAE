package br.org.apae.profissional_da_saude.api.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.profissional_da_saude.application.service.AgendamentoGeradoService;
import br.org.apae.profissional_da_saude.domain.model.AgendamentoGerado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/agendamentos-gerados")
public class AgendamentoGeradoController {

    private final AgendamentoGeradoService service;

    @Autowired
    public AgendamentoGeradoController(AgendamentoGeradoService service) {
        this.service = service;
    }

    @Operation(summary = "Retorna agendamentos gerados com filtros opcionais")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetro inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<AgendamentoGerado>> getAgendamentosGerados(
        @Parameter(description = "ID do profissional") 
        @RequestParam(required = false) UUID idProfissional,

        @Parameter(description = "Data do agendamento") 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,

        @Parameter(description = "ID do paciente") 
        @RequestParam(required = false) UUID idPaciente,

        @Parameter(description = "Status do agendamento (true = ativo, false = inativo)") 
        @RequestParam(required = false) Boolean status) {

        List<AgendamentoGerado> list = service.getFiltered(idProfissional, data, idPaciente, status);
        return ResponseEntity.ok(list);
    }


    
}