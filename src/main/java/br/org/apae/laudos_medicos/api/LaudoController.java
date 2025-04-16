package br.org.apae.laudos_medicos.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.application.dtos.responses.LaudoResponseDTO;
import br.org.apae.laudos_medicos.application.services.LaudoService;

@Controller
@RequestMapping("/laudos")
public class LaudoController {

    private final LaudoService laudoService;
    
    public LaudoController(LaudoService laudoService) {
        this.laudoService = laudoService;
    }

    @PostMapping
    public ResponseEntity<LaudoResponseDTO> criarLaudo(@RequestBody @Validated LaudoRequestDTO laudoRequestDTO){
        LaudoResponseDTO responseDTO = laudoService.criarNovoLaudo(laudoRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


//Preciso ainda olhar com mais calma esses métodos

  /*   @GetMapping("/laudos")
    public ResponseEntity<LaudoResponseDTO> buscarLaudo(@PathVariable Long id){
        LaudoResponseDTO responseDTO = laudoService.buscarLaudoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }
*/

/* 
    @GetMapping("/pacientes/{id_paciente}/laudos")
    public ResponseEntity<List<LaudoResponseDTO>> listarLaudosPaciente(@PathVariable Long idPaciente){
        List<LaudoResponseDTO> responseDTO = laudoService.buscarLaudoPorId(idPaciente);
        return ResponseEntity.ok(responseDTO);
    }
*/
   
/* 
    @GetMapping("/pacientes/{idPaciente}/laudos/{id}")
    public ResponseEntity<LaudoResponseDTO> buscarLaudoPacientePorId(@PathVariable Long idPaciente, @PathVariable Long id) {
        LaudoResponseDTO laudo = laudoService.buscarLaudoPacientePorId(id, idPaciente);
        return ResponseEntity.ok(laudo);
    }
*/
    @PutMapping("/pacientes/{idPaciente}/laudos/{id}")
    public ResponseEntity<LaudoResponseDTO> atualizarLaudo(@PathVariable Long id, @RequestBody LaudoRequestDTO laudoRequestDTO) {
        LaudoResponseDTO laudoAtualizado = laudoService.atualizarLaudo(id, laudoRequestDTO);
        return ResponseEntity.ok(laudoAtualizado);
    }

    @DeleteMapping("/pacientes/{idPaciente}/laudos/{id}")
    public ResponseEntity<Void> deletarLaudo(@PathVariable Long id) {
        laudoService.deletarLaudo(id);
        return ResponseEntity.noContent().build();
    }
}
