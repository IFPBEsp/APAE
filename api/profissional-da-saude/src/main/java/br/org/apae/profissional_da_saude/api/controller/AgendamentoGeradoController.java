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

@RestController
@RequestMapping("/agendamentos-gerados")
public class AgendamentoGeradoController {

    private final AgendamentoGeradoService service;

    @Autowired
    public AgendamentoGeradoController(AgendamentoGeradoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoGerado>> getAgendamentosGerados(
        @RequestParam(required = false) UUID idProfissional,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        @RequestParam(required = false) UUID idPaciente,
        @RequestParam(required = false) Boolean status) {

        List<AgendamentoGerado> list = service.getFiltered(idProfissional, data, idPaciente, status);
        return ResponseEntity.ok(list);
    }


    
}