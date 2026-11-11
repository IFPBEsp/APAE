package br.org.apae.api.common.exceptions.handler;

import java.util.UUID;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerResponseStatusTest.ProbeController.class)
@Import(GlobalExceptionHandlerResponseStatusTest.ProbeController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
@Tag("unit")
@Tag("controller")
@TestPropertySource(properties = {
    "spring.mvc.throw-exception-if-no-handler-found=true"
})

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

        @GetMapping(URI + "/uuid/{id}")
        public String uuid(@PathVariable UUID id) {
            return "ok";
        }

        @GetMapping(URI + "/parametro")
        public String parametro(@RequestParam String nome) {
            return "ok";
        }

        @GetMapping(URI + "/somente-get")
        public String somenteGet() {
            return "ok";
        }

        @PostMapping(URI + "/multipart")
        public String multipart(@RequestPart MultipartFile arquivo) {
            return "ok";
        }

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
    @DisplayName("Retorna 404 quando a rota não existe")
    void deveRetornar404QuandoRotaNaoExistir() throws Exception {

        mockMvc.perform(get("/rota-que-nao-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Recurso não encontrado."));
    }

    @Test
    @DisplayName("Retorna 400 quando uma parte obrigatória da requisição está ausente")
    void deveRetornar400QuandoParteObrigatoriaEstiverAusente() throws Exception {

        mockMvc.perform(multipart(URI + "/multipart"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parâmetro obrigatório ausente."))
                .andExpect(jsonPath("$.path").value(URI + "/multipart"));
    }

    @Test
    @DisplayName("Retorna 405 quando o método HTTP não é suportado")
    void deveRetornar405QuandoMetodoNaoForSuportado() throws Exception {
        mockMvc.perform(put(URI + "/somente-get"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").value("Método HTTP não suportado."))
                .andExpect(jsonPath("$.path").value(URI + "/somente-get"));
    }

    @Test
    @DisplayName("Retorna 400 quando o parâmetro UUID é inválido")
    void deveRetornar400QuandoUuidForInvalido() throws Exception {
        mockMvc.perform(get(URI + "/uuid/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Tipo de parâmetro inválido."))
                .andExpect(jsonPath("$.path").value(URI + "/uuid/abc"));
    }

    @Test
    @DisplayName("Retorna 400 quando um parâmetro obrigatório não é informado")
    void deveRetornar400QuandoParametroObrigatorioEstiverAusente() throws Exception {
        mockMvc.perform(get(URI + "/parametro"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Parâmetro obrigatório ausente."));
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
