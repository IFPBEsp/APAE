package br.org.apae.api.common.exceptions.handler;

import br.org.apae.api.auth.infrastructure.security.JwtProvider;
import br.org.apae.api.auth.infrastructure.security.SecurityFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerResponseStatusTest.ProbeController.class)
@Import(GlobalExceptionHandlerResponseStatusTest.ProbeController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@Tag("unit")
@Tag("controller")
class GlobalExceptionHandlerResponseStatusTest {

    private static final String URI = "/handler-probe";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private SecurityFilter securityFilter;

    @RestController
    public static class ProbeController {

        @GetMapping(URI + "/regra-negocio")
        public String regraNegocio() {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Este paciente já está agendado para este dia com este profissional.");
        }

        @GetMapping(URI + "/nao-encontrado")
        public String naoEncontrado() {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado");
        }

        @GetMapping(URI + "/sem-reason")
        public String semReason() {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        @GetMapping(URI + "/erro-generico")
        public String erroGenerico() {
            throw new IllegalStateException("falha inesperada");
        }

        @GetMapping(URI + "/erro-servidor")
        public String erroServidor() {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Falha na conexão com o banco host=10.0.0.5 porta=5432");
        }

        @GetMapping(URI + "/status-numerico")
        public String statusNumerico() {
            throw new ResponseStatusException(422, "Dados inconsistentes", null);
        }

        @GetMapping(URI + "/status-desconhecido")
        public String statusDesconhecido() {
            throw new ResponseStatusException(599, "código interno 599 não mapeado", null);
        }
    }

    @Test
    @DisplayName("Preserva status 400 e a mensagem da regra de negócio violada")
    void devePreservarStatusEMensagemDeRegraDeNegocio() throws Exception {
        mockMvc.perform(get(URI + "/regra-negocio"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Este paciente já está agendado para este dia com este profissional."))
                .andExpect(jsonPath("$.path").value(URI + "/regra-negocio"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message", not(containsString("CorrelationId"))));
    }

    @Test
    @DisplayName("Preserva status 404 e a mensagem de recurso não encontrado")
    void devePreservarStatus404EMensagem() throws Exception {
        mockMvc.perform(get(URI + "/nao-encontrado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Documento não encontrado"));
    }

    @Test
    @DisplayName("Usa a reason phrase do status quando a exceção não tem motivo")
    void deveUsarReasonPhraseQuandoNaoHaReason() throws Exception {
        mockMvc.perform(get(URI + "/sem-reason"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request"));
    }

    @Test
    @DisplayName("Mantém o handler genérico: erro inesperado continua 500 com CorrelationId")
    void deveManterHandlerGenericoParaErroInesperado() throws Exception {
        mockMvc.perform(get(URI + "/erro-generico"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message", containsString("Ocorreu um erro interno. Informe o código ")))
                .andExpect(jsonPath("$.message", containsString(" ao suporte.")));
    }

    @Test
    @DisplayName("Trata status 5xx como genérico: não expõe a reason e devolve correlationId")
    void deveMascararReasonDeErroDeServidor() throws Exception {
        mockMvc.perform(get(URI + "/erro-servidor"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value(
                        matchesPattern("Ocorreu um erro interno\\. Informe o código [0-9a-fA-F-]{36} ao suporte\\.")))
                .andExpect(jsonPath("$.message", not(containsString("10.0.0.5"))))
                .andExpect(jsonPath("$.path").value(URI + "/erro-servidor"));
    }

    @Test
    @DisplayName("Resolve status criado só com código numérico em vez de converter para 500")
    void devePreservarStatusCriadoComCodigoNumerico() throws Exception {
        mockMvc.perform(get(URI + "/status-numerico"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").value("Dados inconsistentes"));
    }

    @Test
    @DisplayName("Código numérico fora do catálogo de status vira 500 genérico")
    void deveUsarErroGenericoParaCodigoForaDoCatalogo() throws Exception {
        mockMvc.perform(get(URI + "/status-desconhecido"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message", containsString("Ocorreu um erro interno. Informe o código ")))
                .andExpect(jsonPath("$.message", not(containsString("não mapeado"))));
    }
}
