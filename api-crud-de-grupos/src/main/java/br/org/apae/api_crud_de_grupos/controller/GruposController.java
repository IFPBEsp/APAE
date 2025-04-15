package br.org.apae.api_crud_de_grupos.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import br.org.apae.api_crud_de_grupos.model.Grupo;
import br.org.apae.api_crud_de_grupos.repository.APIGruposRepository;

@RestController
public class GruposController {

    @Autowired
    private APIGruposRepository APIGruposRepository;

    @GetMapping("/grupos")
    public List<Grupo> getTodosOsGrupos() {
        return APIGruposRepository.findAll();
    }
    
    @PostMapping("/grupos")
    public ResponseEntity<Grupo> criarGrupo(@RequestBody Grupo grupo) {
        try {
            Grupo grupoSalvo = APIGruposRepository.save(grupo);
            return ResponseEntity.ok(grupoSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/grupos/{id}")
    public Optional<Grupo> getGrupoPorID(UUID id) {
        return APIGruposRepository.findById(id);
    }

    @PutMapping("/grupos/{id}")
    public ResponseEntity<Grupo> atualizarGrupo(@PathVariable UUID id, @RequestBody Grupo grupoAtualizado) {
        Optional<Grupo> grupoExistente = APIGruposRepository.findById(id);
        if (grupoExistente.isPresent()) {
            Grupo grupo = grupoExistente.get();
            grupo.setNomeDoGrupo(grupoAtualizado.getNomeDoGrupo());
            grupo.setDescricao(grupoAtualizado.getDescricao());
            grupo.setListaDePacientesId(grupoAtualizado.getListaDePacientesId());
            grupo.setListaDeProfissionaisDeSaudeId(grupoAtualizado.getListaDeProfissionaisDeSaudeId());
            
            Grupo grupoSalvo = APIGruposRepository.save(grupo);
            return ResponseEntity.ok(grupoSalvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/grupos/{id}")
    public void deletarPorID(UUID id) {
        APIGruposRepository.deleteById(id);
    }
}
