package br.org.apae.documentos_digitalizados.application.mapper;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import br.org.apae.documentos_digitalizados.application.exception.ExtensaoArquivoException;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DocumentoDigitalizadosMapper {

    public DocumentosDigitalizados toEntity(DocumentosDigitalizadosRequestDTO dto) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();

        documento.setNomeBucket(dto.pacienteId() + "-" + dto.nomePaciente());
        documento.setPacienteId(dto.pacienteId());
        documento.setNomePaciente(dto.nomePaciente());
        documento.setTipoPaciente(dto.tipoPaciente());
        documento.setTipoDocumento(dto.tipoDocumento());
        documento.setDataAtualizacao(LocalDateTime.now());

        return documento;
    }

    public ListagemBucketResponseDTO toListagem(List<String> listagem) {
        return new ListagemBucketResponseDTO(new ArrayList<>(listagem));
    }

    public PacienteDocumentoResponseDTO toPaciente(DocumentosDigitalizados documento) {
        return new PacienteDocumentoResponseDTO(
                documento.getPacienteId(),
                documento.getNomePaciente(),
                documento.getNomeBucket(),
                documento.getTipoPaciente(),
                documento.getDataAtualizacao());
    }

    public String toCaminho(String tipoDocumento, String nomeDocumento) {
        return subBucket(tipoDocumento) + nomeDocumento;
    }

    public String nomeDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile arquivo) {
        String extensao = extrairExtensao(arquivo.getOriginalFilename());
        return dto.nomeDocumento() + "-" + UUID.randomUUID() + extensao;
    }

    public String nomeBucket(Long id, String nome) {
        return id + "-" + nome;
    }

    public String subBucket(String tipoDocumento) {
        switch (tipoDocumento) {
            case "pessoal" -> {
                return "documentos-pessoal/";
            }
            case "escolar" -> {
                return "documentos-escolar/";
            }
            case "medico" -> {
                return "documentos-medico/";
            }
            default -> {
                return "";
            }
        }
    }

    private String extrairExtensao(String nomeArquivo) {
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            return nomeArquivo.substring(0, nomeArquivo.lastIndexOf("."));
        }

       throw new ExtensaoArquivoException("Arquivo não contém extensão!");
    }
}
