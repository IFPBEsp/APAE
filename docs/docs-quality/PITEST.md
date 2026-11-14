# Relatório de Avaliação de Testes de Mutação (PITest) — API Geral

## 1. Visão Geral e Configuração

A avaliação de testes de mutação foi conduzida utilizando o **PITest (`pitest-maven` v1.17.4)** integrado ao **`pitest-junit5-plugin` (v1.2.1)** sob o profile Maven isolado `mutation` em `apps/api/pom.xml`, com o grupo de mutadores `DEFAULTS`.

### Tempo de Execução e Métricas Globais
- **Tempo total do PITest:** `14 segundos` (1s de análise de cobertura e dependências + 12s de execução de mutantes).
- **Tempo total do build Maven (`test-compile` + `mutationCoverage`):** `16.008 segundos`.
- **Total de mutantes gerados:** `269` (`107` mortos, `22` sobreviventes, `140` sem cobertura).
- **Força dos Testes (*Test Strength*):** `83%`.

### Testes Excluídos de `targetTests` e Justificativa
Foram incluídos em `targetTests` apenas os **8 testes estritamente unitários** correspondentes às classes avaliadas (`UserServiceTest`, `SecurityFilterTest`, `CPFTest`, `EmailOrCPFTest`, `ValidationUtilsTest`, `ValidFileFormatTest`, `ValidFileSizeTest`, `NotEmptyFilesTest`).

Ficaram de fora:
- **`AppointmentControllerTest` e `AbsenceControllerImplTest`:** utilizam contexto Spring / MockMvc (`@WebMvcTest`) e testam apenas a camada de apresentação (controllers), mockando os Application Services (`AppointmentApplicationServiceImpl` e `AbsenceApplicationServiceImpl`). Incluí-los aumentaria drasticamente o tempo de execução por mutante devido ao overhead do Spring sem exercitar o código real das classes de serviço alvo.

---

## 2. Resultados por Classe

| Classe | Cobertura de Linha | Mutation Coverage | Test Strength | Mutantes Sem Cobertura (`NO_COVERAGE`) |
|---|---|---|---|---|
| `AuthApplicationServiceImpl` | 0% (0/58) | 0% (0/16) | 100% (0/0) | 16 |
| `UserService` | 46% (12/26) | 44% (4/9) | 100% (4/4) | 5 |
| `JwtProvider` | 0% (0/19) | 0% (0/3) | 100% (0/0) | 3 |
| `SecurityFilter` | 98% (40/41) | 86% (19/22) | 86% (19/22) | 0 |
| `AppointmentApplicationServiceImpl` | 0% (0/242) | 0% (0/87) | 100% (0/0) | 87 |
| `AbsenceApplicationServiceImpl` | 0% (0/54) | 0% (0/21) | 100% (0/0) | 21 |
| `CPF` | 100% (32/32) | 94% (30/32) | 94% (30/32) | 0 |
| `EmailOrCPF` | 71% (20/28) | 50% (11/22) | 79% (11/14) | 8 |
| `ValidationUtils` | 75% (6/8) | 100% (3/3) | 100% (3/3) | 0 |
| `NotEmptyFiles` | 100% (6/6) | 91% (10/11) | 91% (10/11) | 0 |
| `ValidFileFormat` | 94% (34/36) | 68% (17/25) | 68% (17/25) | 0 |
| `ValidFileSize` | 96% (27/28) | 72% (13/18) | 72% (13/18) | 0 |
| **Total / Consolidado** | **31% (177/578)** | **40% (107/269)** | **83% (107/129)** | **140** |

*(Nota: Em `UserService`, os métodos `findUserByUsername` e `findUserByEmail` não possuem testes, gerando 5 mutantes `NO_COVERAGE`. Em `EmailOrCPF`, os métodos boilerplate de `Annotation` das classes anônimas internas geram 8 mutantes `NO_COVERAGE`).*

---

## 3. Classes Críticas Sem Teste Unitário

As seguintes classes críticas de negócio e infraestrutura possuem **0% de cobertura unitária** e acumulam **127 dos 140 mutantes sem cobertura**:

1. **`AuthApplicationServiceImpl`** (`br.org.apae.api.auth.application.internal`) — Fluxos críticos de login, geração/hash de token, recuperação e redefinição de senha (`resetPassword`, `requestPasswordRecovery`).
2. **`JwtProvider`** (`br.org.apae.api.auth.infrastructure.security`) — Geração, assinatura, expiração e validação de tokens JWT.
3. **`AppointmentApplicationServiceImpl`** (`br.org.apae.api.appointment.application.internal`) — Regras de negócio centrais de agendamento de consultas (242 linhas e 87 mutantes sem nenhum teste unitário).
4. **`AbsenceApplicationServiceImpl`** (`br.org.apae.api.appointment.application.internal`) — Regras de negócio de registro e controle de faltas/ausências (54 linhas e 21 mutantes sem cobertura).

---

## 4. Análise dos Mutantes Sobreviventes Relevantes

Ao todo, **22 mutantes sobreviveram** nas classes cobertas por testes. Abaixo estão agrupados cada mutante relevante, o teste responsável que deveria tê-lo matado e o diagnóstico da falha:

### 4.1. `SecurityFilter` (`SecurityFilterTest`) — 3 sobreviventes
1. **Linha 103 (`doFilterInternal`) — `VoidMethodCallMutator` (`removed call to SecurityContext::setAuthentication`):**
   - **Teste responsável:** `SecurityFilterTest` (testes de sucesso de autenticação com token válido).
   - **Motivo:** O teste utiliza mock ou verifica apenas o prosseguimento da `FilterChain`, **sem fazer asserção direta no `SecurityContextHolder.getContext().getAuthentication()`** após a execução do filtro para garantir que o usuário autenticado foi efetivamente registrado no contexto do Spring Security.
2. **Linha 105 (`doFilterInternal`) — `VoidMethodCallMutator` (`removed call to SecurityContextHolder::clearContext`):**
   - **Teste responsável:** `SecurityFilterTest.AuthFailureTests#shouldCallEntryPointAndClearContextForTokenVerificationException`.
   - **Motivo:** Apesar do nome do teste sugerir que verifica a limpeza do contexto, o `SecurityContextHolder` já inicia vazio no teste. Como o teste não popula uma autenticação prévia antes de simular a falha, remover a chamada `clearContext()` ainda resulta em um contexto `null`, permitindo que o mutante sobreviva.
3. **Linha 71 (`getRequestPathWithinServlet`) — `RemoveConditionalMutator_EQUAL_ELSE`:**
   - **Teste responsável:** `SecurityFilterTest.ShouldNotFilterTests`.
   - **Motivo:** Caso de borda não testado na extração de caminho do `HttpServletRequest` (quando `servletPath` ou `pathInfo` assumem combinações específicas de vazio/nulo).

### 4.2. `CPF` (`CPFTest`) — 2 sobreviventes
1. **Linha 54 (`calculateSecondDigit`) — `ConditionalsBoundaryMutator` (`changed conditional boundary` de `< 2` para `<= 2` ou similar no cálculo do resto de divisão por 11):**
   - **Teste responsável:** `CPFTest#shouldAcceptValidCpf` / `shouldRejectCpfWithWrongCheckDigits`.
   - **Motivo:** Caso de borda não testado. Nenhum dos CPFs da massa de testes produz resto exatamente no limite da condição (`resto == 2` no segundo dígito verificador), fazendo com que a troca de `<` por `<=` produza o mesmo resultado para os CPFs testados.
2. **Linha 58 (`validateFirstDigit`) — `BooleanTrueReturnValsMutator` (`replaced boolean return with true`):**
   - **Teste responsável:** `CPFTest#shouldRejectCpfWithWrongCheckDigits`.
   - **Motivo:** Os CPFs inválidos testados possuem o **segundo** dígito verificador incorreto (ou ambos incorretos). Não há um caso de teste com o **primeiro dígito errado e o segundo dígito matematicamente válido**, de modo que `validateSecondDigit` retorna `false` e mascara a sabotagem em `validateFirstDigit`.

### 4.3. `EmailOrCPF` (`EmailOrCPFTest`) — 3 sobreviventes
1. **Linha 66 (`checkDigits` na instância anônima de `CPF`) — `BooleanTrueReturnValsMutator`:**
   - **Teste responsável:** `EmailOrCPFTest`.
   - **Motivo:** Ausência de teste exercitando a anotação `@EmailOrCPF(checkDigits = false)` com um CPF de dígitos verificadores inválidos.
2. **Linha 103 (`initialize`) — `VoidMethodCallMutator` (`removed call to EmailValidator::initialize`):**
   - **Teste responsável:** `EmailOrCPFTest#shouldAcceptValidEmail`.
   - **Motivo:** A implementação padrão de `EmailValidator.initialize()` do Hibernate Validator é um no-op para a configuração padrão, tornando o mutante quase equivalente na prática sem customização de regex/flags no `EmailValidator` base.
3. **Linha 108 (`isValid`) — `RemoveConditionalMutator_EQUAL_ELSE` (verificação de `value == null || value.isEmpty()`):**
   - **Teste responsável:** `EmailOrCPFTest#shouldAcceptNullOrEmptyValue`.
   - **Motivo:** Condição redundante ou caso de string vazia (`""`) vs `null` não validado separadamente para forçar os dois ramos do curto-circuito booleano.

### 4.4. `ValidFileFormat` (`ValidFileFormatTest`) — 8 sobreviventes
1. **Linhas 51 e 62 (`isValidFile`) — `VoidMethodCallMutator` (`removed call to ConstraintValidatorContext::disableDefaultConstraintViolation`):**
   - **Teste responsável:** `ValidFileFormatTest#singleShouldRejectNotAllowedContentType` e `singleShouldRejectNotAllowedExtension`.
   - **Motivo:** O mock de `ConstraintValidatorContext` nos testes não verifica (`verify(context).disableDefaultConstraintViolation()`) se a violação padrão foi desabilitada antes de construir a mensagem customizada.
2. **Linha 76 (`getFileExtension`) — `MathMutator` (troca de `-` por `+` em `filename.length() - 1`) e `RemoveConditionalMutator_EQUAL_ELSE`:**
   - **Teste responsável:** `ValidFileFormatTest`.
   - **Motivo:** Caso de borda não testado: arquivo cujo nome termina exatamente com um ponto final (ex.: `"arquivo."`), onde `lastIndexOf('.') == filename.length() - 1`.
3. **Linhas 43, 50 e 86 (`isValidFile` e `ListValidator::isValid`) — `RemoveConditionalMutator_EQUAL_ELSE` (4 mutantes):**
   - **Teste responsável:** `ValidFileFormatTest#singleShouldAcceptNullOrEmptyFile` e `shouldAcceptNullOrEmptyList`.
   - **Motivo:** Testes não separam explicitamente todas as permutações de curto-circuito (`file == null` vs `file.isEmpty()`, `contentType == null` vs `contentType.isBlank()`, `files == null` vs `files.isEmpty()`).

### 4.5. `ValidFileSize` (`ValidFileSizeTest`) — 5 sobreviventes
1. **Linhas 41 e 42 (`isValidFile`) — `MathMutator` (`Replaced double division with multiplication` no cálculo do tamanho em MB para a mensagem de erro):**
   - **Teste responsável:** `ValidFileSizeTest#singleShouldRespectCustomMaxSizeFromAnnotation`.
   - **Motivo:** Asserção ausente sobre o texto da mensagem de erro gerada. O teste verifica apenas que `isValid` retornou `false`, mas **não inspeciona a string passada para `context.buildConstraintViolationWithTemplate(...)`**, permitindo que o cálculo de bytes para MB (`1024.0 * 1024.0`) seja sabotado sem quebrar o teste.
2. **Linha 40 (`isValidFile`) — `VoidMethodCallMutator` (`removed call to disableDefaultConstraintViolation`):**
   - **Teste responsável:** `ValidFileSizeTest#singleShouldRespectCustomMaxSizeFromAnnotation`.
   - **Motivo:** Falta de `verify(context).disableDefaultConstraintViolation()` no mock do contexto.
3. **Linhas 34 e 64 (`isValidFile` e `ListValidator::isValid`) — `RemoveConditionalMutator_EQUAL_ELSE` (2 mutantes):**
   - **Teste responsável:** `ValidFileSizeTest#singleShouldAcceptNullOrEmptyFile` e `shouldAcceptNullOrEmptyList`.
   - **Motivo:** Quando um arquivo vazio (`size == 0`) passa direto pelo `if (file == null || file.isEmpty())` sabotado, ele cai na verificação `file.getSize() > maxSize` (`0 > maxSize`), que também resulta em válido (`true`). Isso torna a checagem `file.isEmpty()` ou `list.isEmpty()` comportamentalmente equivalente a `true` para tamanhos positivos.

### 4.6. `NotEmptyFiles` (`NotEmptyFilesTest`) — 1 sobrevivente
1. **Linha 30 (`ListValidator::isValid`) — `RemoveConditionalMutator_EQUAL_ELSE` (em `files == null || files.isEmpty()`):**
   - **Teste responsável:** `NotEmptyFilesTest#shouldRejectEmptyList`.
   - **Motivo:** Mutante equivalente: se a verificação `files.isEmpty()` é removida, o código executa `files.stream().anyMatch(...)` em uma lista vazia, que retorna `false` do mesmo jeito.

---

## 5. Recomendações e Próximos Passos

### 5.1. Vale manter o PIT na rotina do time?
**Sim.** A execução restrita às classes críticas com testes unitários levou apenas **14 segundos** e revelou falhas graves que o JaCoCo ocultava — por exemplo, `CPF.java` possui **100% de cobertura de linha**, mas o teste deixava passar um CPF com o primeiro dígito inválido caso o segundo fosse válido; e `SecurityFilter.java` possui **98% de cobertura de linha**, mas os testes não garantiam que a autenticação permanecia salva ou era efetivamente limpa do `SecurityContextHolder`.

### 5.2. Frequência de Execução e Pipeline
- **Fora do CI/CD de PRs:** Não incluir na esteira padrão de Pull Requests para evitar custo computacional à medida que a suíte crescer.
- **Execução Trimestral / Por Marco de Release:** Rodar o profile `-Pmutation` trimestralmente ou ao final de ciclos de entrega das funcionalidades críticas para auditar a qualidade real das asserções.

### 5.3. Configuração de `mutationThreshold`
- **Não fixar `mutationThreshold` no momento.** Como classes inteiras de aplicação (`AppointmentApplicationServiceImpl`, `AbsenceApplicationServiceImpl`, `AuthApplicationServiceImpl`, `JwtProvider`) ainda não possuem testes unitários, a cobertura de mutação global das classes críticas é de **40%**, embora a força dos testes existentes (*Test Strength*) seja de **83%**. Recomenda-se fixar um `mutationThreshold` (ex.: 75%) apenas após a conclusão das issues de criação de testes abaixo.

### 5.4. Issues de Teste Sugeridas a partir dos Achados
1. **[Testes Unitários] Implementar testes unitários para `AuthApplicationServiceImpl` e `JwtProvider`:** cobrir fluxos de login, geração/validação de JWT, `requestPasswordRecovery` e `resetPassword`, além de cobrir `findUserByUsername` e `findUserByEmail` em `UserService`.
2. **[Testes Unitários] Implementar testes unitários para `AppointmentApplicationServiceImpl` e `AbsenceApplicationServiceImpl`:** testar isoladamente as regras de negócio de agendamentos e faltas sem depender apenas de testes de controller mockados.
3. **[Reforço de Testes] Corrigir asserções de contexto no `SecurityFilterTest`:** verificar estado real do `SecurityContextHolder` após autenticação bem-sucedida e popular contexto prévio antes de testar `clearContext()` em cenários de falha.
4. **[Reforço de Testes] Adicionar casos de borda em `CPFTest`, `ValidFileFormatTest` e `ValidFileSizeTest`:**
   - Testar CPF com apenas o 1º dígito inválido e CPF cujo resto do 2º dígito seja exatamente `2`;
   - Verificar mensagem formatada em MB e chamada a `disableDefaultConstraintViolation()` nos validadores de arquivo;
   - Testar nome de arquivo terminado em ponto (`"arquivo."`) em `ValidFileFormatTest`.