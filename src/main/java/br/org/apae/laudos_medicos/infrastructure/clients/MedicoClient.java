package br.org.apae.laudos_medicos.infrastructure.clients;

import br.org.apae.laudos_medicos.infrastructure.clients.dtos.MedicoResponseDTO;
import br.org.apae.laudos_medicos.infrastructure.clients.exceptions.IdMedicoInvalidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MedicoClient {

    private final RestClient restClient;

    public MedicoClient(RestClient.Builder restClient, @Value("${api.medicos.url}") String url) {
        this.restClient = restClient
                .baseUrl(url)
                .build();
    }

    public MedicoResponseDTO buscarMedicoPorId(Long idMedico) {
        return this.restClient.get()
                .uri("/medicos/{id}", idMedico)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new IdMedicoInvalidoException("Id do médico não existe ou não encontrado.");
                })
                .body(MedicoResponseDTO.class);
    }
}
