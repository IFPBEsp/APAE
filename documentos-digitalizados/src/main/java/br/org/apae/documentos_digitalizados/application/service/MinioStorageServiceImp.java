package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.api.dto.BucketResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import br.org.apae.documentos_digitalizados.domain.exception.*;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Override
    public void criarBucket(UUID bucketNome) {
        boolean existe = existeBucket(bucketNome.toString());

        if (!existe) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketNome.toString()).build());
                criarPasta(bucketNome.toString(), "documentos-pessoal/");
                criarPasta(bucketNome.toString(), "documentos-medico/");
                criarPasta(bucketNome.toString(), "documentos-escolar/");
            } catch (Exception e) {
                throw new CriacaoBucketException("Erro na criação de bucket no minIO!\n" + e.getMessage());
            }
        }
    }

    @Override
    public ListagemBucketResponseDTO listarBuckets() {
        try {
            List<String> buckets = minioClient.listBuckets()
                    .stream()
                    .map(Bucket::name)
                    .toList();

            return new ListagemBucketResponseDTO(buckets);
        } catch (Exception e) {
            throw new ListagemBucketException("Erro ao listar nomes dos buckets!\n" + e.getMessage());
        }
    }

    @Override
    public BucketResponseDTO listarBucketPorNome(String bucketNome) {
        if (existeBucket(bucketNome)) {
            return new BucketResponseDTO(bucketNome);
        }

        throw new ExisteBucketException("Não existe o bucket '" + bucketNome + "'!");
    }

    @Override
    public void deletarBucket(String bucketNome) {
        if (!existeBucket(bucketNome)) {
            throw new ExisteBucketException("O bucket: '" + bucketNome + "' não existe!");
        }

        esvaziarBucket(bucketNome);
        try {
            minioClient.removeBucket(
                    RemoveBucketArgs.builder()
                            .bucket(bucketNome)
                            .build()
            );
        } catch (Exception e) {
            throw new ListagemBucketException("Erro ao deletar o bucket: " + bucketNome + "!\n" + e.getMessage());
        }
    }

    @Override
    public boolean existeBucket(String bucketNome) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketNome).build());
        } catch (Exception e) {
            throw new ExisteBucketException("Erro na busca de bucket no minIO!\n" + e.getMessage());
        }
    }

    private void criarPasta(String bucketNome, String nomePasta){
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(nomePasta)
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .build()
            );
        } catch (Exception e){
            throw new DiretorioException("Falha ao criar diretório!\n" + e.getMessage());
        }
    }

    private void esvaziarBucket(String bucketNome){
        Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketNome)
                        .recursive(true)
                        .build()
        );

        try {
            for (Result<Item> result : objects) {
                Item item = result.get();
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketNome)
                                .object(item.objectName())
                                .build()
                );
            }
        } catch (Exception e){
            throw new DeletarObjetosException("Erro ao deletar os dados do bucket!\n" + e.getMessage());
        }
    }
}
