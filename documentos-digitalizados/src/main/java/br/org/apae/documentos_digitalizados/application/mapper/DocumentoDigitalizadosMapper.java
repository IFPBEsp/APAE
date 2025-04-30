package br.org.apae.documentos_digitalizados.application.mapper;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.exception.ExtensaoArquivoException;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class DocumentoDigitalizadosMapper {

    public DocumentosDigitalizados toEntity(DocumentosDigitalizadosRequestDTO dto, MultipartFile arquivo) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();
        documento.setPacienteId(dto.pacienteId());
        documento.setTipoDocumento(dto.tipoDocumento());

        String extensao = extrairExtensao(arquivo.getOriginalFilename());

        documento.setDocumento(dto.nomeDocumento() + "-" + UUID.randomUUID() + extensao);

        return documento;
    }

    public DocumentosDigitalizados toEntity(Long id, DocumentosDigitalizadosRequestDTO documentosDigitalizadosRequestDTO) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();
        documento.setId(id);
        documento.setPacienteId(documentosDigitalizadosRequestDTO.pacienteId());

        return documento;
    }

    private String extrairExtensao(String nomeArquivo) {
        if (nomeArquivo != null && !nomeArquivo.isEmpty()) {
            return nomeArquivo.substring(0, nomeArquivo.lastIndexOf("."));
        }

       throw new ExtensaoArquivoException("Arquivo não contém extensão!");
    }
}
