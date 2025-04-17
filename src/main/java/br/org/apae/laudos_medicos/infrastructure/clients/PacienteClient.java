package br.org.apae.laudos_medicos.infrastructure.clients;

import br.org.apae.laudos_medicos.infrastructure.clients.dtos.MedicoResponseDTO;
import br.org.apae.laudos_medicos.infrastructure.clients.dtos.PacienteResponseDTO;
import br.org.apae.laudos_medicos.infrastructure.clients.exceptions.IdMedicoInvalidoException;
import br.org.apae.laudos_medicos.infrastructure.clients.exceptions.IdPacienteInvalidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

public class PacienteClient {

    private final RestClient restClient;

    public PacienteClient(RestClient.Builder restClient, @Value("${api.medicos.url}") String url) {
        this.restClient = restClient
                .baseUrl(url)
                .build();
    }


    public PacienteResponseDTO buscarPacientePorId(Long idPaciente) {
        return this.restClient.get()
                .uri("/pacientes/{id}", idPaciente)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new IdPacienteInvalidoException("Id do paciente não existe ou não encontrado.");
                })
                .body(PacienteResponseDTO.class);
    }
}
