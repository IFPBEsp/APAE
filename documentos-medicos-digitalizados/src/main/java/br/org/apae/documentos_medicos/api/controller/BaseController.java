package br.org.apae.documentos_medicos.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public interface BaseController<T> {
  /*
    * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos médicos associados a 1 paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo=medico) - Filtrar por tipo de documento?
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo})- Visualizar histórico de um tipo de documento especifico da parte médica(Seja exames, laúdos ou encaminhamentos).
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar todos os documentos médicos do paciente;
    * PUT (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Atualizar documento
    * PATCH (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId})- Desativar documento
    
    */
   ResponseEntity<T> anexarDocumentoMedico(UUID pacienteId, T documentoAnexado);
   ResponseEntity<List<T>> listarDocumentosMedicosPaciente(UUID pacienteId);
   ResponseEntity<List<T>> filtrarPorTipoDocumento(UUID pacienteId, MedcialDocumentType tipoDocumento);
   ResponseEntity<List<T>> historicoTipoDocumento(UUID pacienteId, MedcialDocumentType tipoDocumento);
   ResponseEntity<List<T>> visualizarDocumentosMedicosPaciente(UUID pacienteID, UUID documentoId);
   ResponseEntity<T> atualizarDocumento(UUID pacienteId, UUID documentoId, T documentoAtualizado);
   ResponseEntity<Void> desativarDocumento(UUID pacienteId, UUID documentoId);
   

}