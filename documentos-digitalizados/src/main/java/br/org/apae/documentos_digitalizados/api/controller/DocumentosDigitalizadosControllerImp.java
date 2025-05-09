package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.BucketResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import br.org.apae.documentos_digitalizados.application.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ListagemBucketResponseDTO> listarBuckets() {
        ListagemBucketResponseDTO dto = minioStorageService.listarBuckets();

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Override
    public ResponseEntity<BucketResponseDTO> listarBucket(UUID bucketNome) {
        BucketResponseDTO dto = minioStorageService.listarBucketPorNome(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Override
    public ResponseEntity<Void> deletarBucket(UUID bucketNome) {
        minioStorageService.deletarBucket(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Boolean> verificarBucket(UUID bucketNome) {
        boolean existeBucket = minioStorageService.existeBucket(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).body(existeBucket);
    }
}