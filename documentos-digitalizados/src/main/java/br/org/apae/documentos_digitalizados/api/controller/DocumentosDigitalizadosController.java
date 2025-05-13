package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/documento")
public interface DocumentosDigitalizadosController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> salvarDocumento(@RequestPart("documento") @Valid DocumentosDigitalizadosRequestDTO dto,
                                         @RequestPart("anexo") MultipartFile documento) throws Exception;

    @GetMapping
    ResponseEntity<ListagemBucketResponseDTO> listarDocumentos(@RequestBody @Valid ListagemBucketRequestDTO dto);

    @GetMapping("/{pacienteID}")
    ResponseEntity<PacienteDocumentoResponseDTO> buscarPorPaciente(@PathVariable("pacienteID") Long pacienteID);

    @GetMapping("/download")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> buscarDocumentoPorNome(@RequestBody @Valid BuscaDocumentoRequestDTO dto);

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> atualizarDocumento(@RequestPart("documento") @Valid DocumentosDigitalizadosRequestDTO dto,
                                            @RequestPart("anexo") MultipartFile documento);
}
