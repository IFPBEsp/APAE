package br.org.apae.documentos_digitalizados.application.mapper;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.exception.ExtensaoArquivoException;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DocumentoDigitalizadosMapper {

    public DocumentosDigitalizados toEntity(DocumentosDigitalizadosRequestDTO dto, MultipartFile arquivo) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();

        documento.setNomeBucket(dto.pacienteId() + "-" + dto.nomePaciente());
        documento.setPacienteId(dto.pacienteId());
        documento.setNomePaciente(dto.nomePaciente());
        documento.setTipoPaciente(dto.tipoPaciente());
        documento.setTipoDocumento(dto.tipoDocumento());

        String extensao = extrairExtensao(arquivo.getOriginalFilename());
        documento.setDocumento(dto.nomeDocumento() + "-" + UUID.randomUUID() + extensao);

        documento.setDataAtualizacao(LocalDateTime.now());

        return documento;
    }

    private String extrairExtensao(String nomeArquivo) {
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            return nomeArquivo.substring(0, nomeArquivo.lastIndexOf("."));
        }

       throw new ExtensaoArquivoException("Arquivo não contém extensão!");
    }
}
