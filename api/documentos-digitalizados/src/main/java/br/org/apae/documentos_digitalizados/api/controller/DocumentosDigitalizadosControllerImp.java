package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.BucketResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import br.org.apae.documentos_digitalizados.application.service.MinioStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Cria a estrutura do bucket para um paciênte", description = "Recebe por parâmetro o UUID do paciênte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bucket do paciênte criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Bucket do paciênte já existe"),
            @ApiResponse(responseCode = "500", description = "Bucket do paciênte não foi criado por erro do minIO")
    })
    @Override
    public ResponseEntity<Void> criarBucket(UUID bucketNome) {
        minioStorageService.criarBucket(bucketNome);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Lista todos os buckets existentes", description = "Recebe nenhum parâmetro e retorna todos os buckets criados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @Override
    public ResponseEntity<ListagemBucketResponseDTO> listarBuckets() {
        ListagemBucketResponseDTO dto = minioStorageService.listarBuckets();

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(summary = "Busca por bucket", description = "Recebe como parâmetro o UUID do paciênte e retorna o bucket se existir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca executada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bucket não existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @Override
    public ResponseEntity<BucketResponseDTO> listarBucket(UUID bucketNome) {
        BucketResponseDTO dto = minioStorageService.listarBucketPorNome(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(summary = "Remoção de bucket", description = "Recebe como parâmetro o UUID do paciênte e remove o bucket caso exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Remoção executada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bucket não existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @Override
    public ResponseEntity<Void> deletarBucket(UUID bucketNome) {
        minioStorageService.deletarBucket(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Verificação de bucket", description = "Recebe como parâmetro o UUID do paciênte, verifica e retorna true caso exista e false caso não exista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do minIO")
    })
    @Override
    public ResponseEntity<Boolean> verificarBucket(UUID bucketNome) {
        boolean existeBucket = minioStorageService.existeBucket(bucketNome.toString());

        return ResponseEntity.status(HttpStatus.OK).body(existeBucket);
    }
}