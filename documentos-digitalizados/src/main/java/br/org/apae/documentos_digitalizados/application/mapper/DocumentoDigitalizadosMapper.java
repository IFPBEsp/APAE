package br.org.apae.documentos_digitalizados.application.mapper;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentoDigitalizadosMapper {
    public List<DocumentosDigitalizadosResponseDTO> toDTO(List<DocumentosDigitalizados> documentosDigitalizados) {
        return documentosDigitalizados.stream()
            .map(documento -> new DocumentosDigitalizadosResponseDTO(
                documento.getId(),
                documento.getPacienteId(),
                documento.getEncaminhamento(),
                documento.getLaudoMedico()
            )).collect(Collectors.toList()
        );
    }

    public DocumentosDigitalizadosResponseDTO toDTO(DocumentosDigitalizados documentosDigitalizados) {
        return new DocumentosDigitalizadosResponseDTO(
                documentosDigitalizados.getId(),
                documentosDigitalizados.getPacienteId(),
                documentosDigitalizados.getEncaminhamento(),
                documentosDigitalizados.getLaudoMedico()
        );
    }

    public DocumentosDigitalizados toEntity(DocumentosDigitalizadosRequestDTO documentosDigitalizadosRequestDTO) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();
        documento.setPacienteId(documentosDigitalizadosRequestDTO.pacienteId());

        return documento;
    }

    public DocumentosDigitalizados toEntity(Long id, DocumentosDigitalizadosRequestDTO documentosDigitalizadosRequestDTO) {
        DocumentosDigitalizados documento = new DocumentosDigitalizados();
        documento.setId(id);
        documento.setPacienteId(documentosDigitalizadosRequestDTO.pacienteId());

        return documento;
    }
}
