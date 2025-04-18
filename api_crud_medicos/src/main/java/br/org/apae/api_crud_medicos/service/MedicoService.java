package br.org.apae.api_crud_medicos.service;

import br.org.apae.api_crud_medicos.controller.dto.request.MedicoRequestDTO;
import br.org.apae.api_crud_medicos.controller.dto.response.MedicoResponseDTO;

import java.util.List;

public interface MedicoService {
    MedicoResponseDTO criar(MedicoRequestDTO dto);
    MedicoResponseDTO buscarPorId(Long id);
    List<MedicoResponseDTO> listarTodos();
    MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto);
    void excluir(Long id);


}
