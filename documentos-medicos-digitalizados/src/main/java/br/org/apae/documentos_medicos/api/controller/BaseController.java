package main.java.br.org.apae.documentos_medicos.api.controller;

public interface BaseController  {
  /*
    * POST (/api/pacientes/{pacienteId}/documentos-medicos) - Anexar documentos médicos a um paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos) - Listar todos os documentos médicos associados a 1 paciente.
    * GET (/api/pacientes/{pacienteId}/documentos-medicos?tipo=medico) - Filtrar por tipo de documento?
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/historico?tipo={tipo})- Visualizar histórico de um tipo de documento especifico da parte médica(Seja exames, laúdos ou encaminhamentos).
    * GET (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Visualizar todos os documentos médicos do paciente;
    * PUT (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId}) - Atualizar documento
    * PATCH (/api/pacientes/{pacienteId}/documentos-medicos/{documentoId})- Remover documento
    
    */
   void anexarDocumentoMedico();
   void listarDocumentosMedicosPaciente();
   void filtrarPorTipoDocumento();
   void historicoTipoDocumento();
   void visualizarDocumentosMedicosPaciente();
   void atualizarDocumento();
   void desativarDocumento();
   
    
}
