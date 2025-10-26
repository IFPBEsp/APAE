package br.org.apae.profissional_da_saude.api.controller;

import br.org.apae.profissional_da_saude.application.service.AgendamentoGeradoService;

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
        @Parameter(description = "ID do profissional para filtragem") @RequestParam(required = false) UUID idProfissional,
        @Parameter(description = "Data inicial para filtragem (formato: yyyy-MM-dd)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        @Parameter(description = "ID do paciente para filtro") @RequestParam(required = false) UUID idPaciente,
        @Parameter(description = "Status para filtragem") @RequestParam(required = false) Boolean status) {

        List<AgendamentoGerado> list = service.getFiltered(idProfissional, data, idPaciente, status);
        return ResponseEntity.ok(list);
    }


    
}