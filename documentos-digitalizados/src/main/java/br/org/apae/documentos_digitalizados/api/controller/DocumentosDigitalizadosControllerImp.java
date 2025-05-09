package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import br.org.apae.documentos_digitalizados.application.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bucket")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {

    private final MinioStorageService minioStorageService;

    @Override
    public ResponseEntity<Void> criarBucket(UUID bucketNome) {
        minioStorageService.criarBucket(bucketNome);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<ListagemBucketResponseDTO>> listarBuckets() {
        return null;
    }

    @Override
    public ResponseEntity<ListagemBucketResponseDTO> listarBucket(UUID bucketNome) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletarBucket(UUID bucketNome) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> verificarBucket(UUID bucketNome) {
        boolean existeBucket = minioStorageService.existeBucket(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).body(existeBucket);
    }
}