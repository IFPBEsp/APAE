# Análise Técnica do Projeto APAE

Documento de resultado da análise de refatorações, padronizações, correções e melhorias do monorepo APAE.

**Data:** 15/08/2026
**Revisão sobre:** branch `dev`, commit `94f813cc`
**Escopo:** `apps/api` (Spring Boot 3.5.6 / Java 21), `apps/apae` (Next.js 16), `apps/management-app` (Next.js 15), infraestrutura Docker, scripts e pipelines CI.
**Fora de escopo:** criação de testes e qualidade de código, sob responsabilidade de outra equipe.

---

## 1. Sumário Executivo

O projeto encontra-se em migração arquitetural documentada em `docs/docs-frontend/REFACT_ARCHITECTURE.md`, com a estrutura antiga (agrupamento por tipo técnico) e a nova (fatiamento vertical por domínio) coexistindo. A maior parte dos problemas identificados decorre dessa transição incompleta e da ausência de mecanismos que imponham os padrões já definidos.

| Categoria | Itens | Prioridade predominante |
|---|---|---|
| Segurança e correção funcional | 9 | Crítica / Alta |
| Padronização | 8 | Média |
| Refatoração estrutural | 7 | Média / Alta |
| Infraestrutura, build e CI | 7 | Alta |
| Código morto e remoção | 5 | Média |

Ações de maior impacto imediato: correção do cadastro público com papel ADMIN (C-01), correção da resolução de URL da API em container (C-04), adoção de ferramenta de migração de banco (I-01) e remoção do `apps/management-app` (R-01).

---

## 2. Correções Críticas

### C-01 — Cadastro público concede papel ADMIN
`SecurityConfiguration.java:38` libera `/auth/**` sem autenticação, e `UserService.java:34` cria todo usuário com `UserRole.ADMIN` de forma incondicional. Qualquer agente com acesso à rede da API pode criar um administrador via `POST /auth/signup`.

**Ação:** restringir `/auth/signup` a usuários autenticados com papel administrativo, ou removê-lo da API pública; receber o papel como parâmetro validado em vez de fixá-lo.

### C-02 — CORS ignora a configuração de ambiente
`SecurityConfiguration.java:69` fixa `List.of("http://localhost:3000")`. A propriedade `cors.allowed-origins` existe em `application.yaml:73` e é injetada pelo `docker-compose.yml:56`, mas nunca é lida.

**Ação:** injetar a propriedade via `@Value` e converter a lista separada por vírgula.

### C-03 — Falha na validação de token retorna 500
`SecurityFilter.java:44` chama `jwtProvider.validateToken` diretamente no filtro. `TokenVerificationException` é lançada fora da cadeia do `@ControllerAdvice`, resultando em 500 em vez de 401 para tokens expirados ou inválidos. Adicionalmente, `recoverToken` usa `replace("Bearer ", "")`, que remove a substring em qualquer posição do cabeçalho.

**Ação:** capturar a exceção no filtro e delegar a um `AuthenticationEntryPoint` que responda 401; substituir `replace` por verificação de prefixo com `startsWith` e `substring`.

### C-04 — Frontend não alcança a API em container
`lib/axios.ts:20-26` resolve a URL base como `API_URL || NEXT_PUBLIC_API_URL || fallback`. O `docker-compose.yml:78` define apenas `NEXT_PUBLIC_API_URL=/apae-geral/api`, um caminho relativo que o Axios em contexto de servidor não resolve, e `API_URL` não é definida em lugar algum. Todas as rotas BFF falham no ambiente containerizado.

**Ação:** definir `API_URL=http://apae-geral-backend:8080/apae-geral/api` no serviço de frontend do compose e validar a variável na inicialização.

### C-05 — Exclusão lógica não filtrada em consultas
`Patient.isDeleted` (`Patient.java:71`) só é filtrado em `PatientSpecification.java:22`. As consultas `findDistinctCities`, `findPatientsWithAbsences` e `findById` em `PatientRepository.java` retornam pacientes excluídos.

**Ação:** aplicar `@SQLRestriction` na entidade ou incluir a condição explicitamente em todas as consultas.

### C-06 — Handler genérico expõe detalhes internos
`GlobalExceptionHandler.java:46` devolve `ex.getMessage()` de qualquer `Exception` não tratada no corpo da resposta, expondo mensagens de driver JDBC, caminhos e detalhes de infraestrutura. Não há registro em log da exceção.

**Ação:** registrar a exceção em log e retornar mensagem genérica ao cliente.

### C-07 — Vazamento de dados pessoais em log
`application.yaml` habilita `show-sql: true` e `logging.level.org.hibernate.type.descriptor.sql.BasicBinder: TRACE` no perfil padrão, ou seja, também em produção. Os parâmetros de bind incluem CPF, RG, CNS, NIS e endereços de pacientes.

**Ação:** mover essas configurações para um perfil `dev` e manter o perfil padrão silencioso.

### C-08 — Serviço de faltas lê token inexistente
`app/services/absenceService.ts:8` obtém o token via `localStorage.getItem('token')`. A sessão é armazenada em cookie `httpOnly` (`lib/cookies.ts:36`), nunca em `localStorage`. O cabeçalho `Authorization` é sempre omitido.

**Ação:** redirecionar as chamadas para as rotas BFF em `app/api/absences/`, que já injetam o token via interceptor.

### C-09 — Expiração de JWT baseada em fuso fixo
`JwtProvider.java:29` calcula a expiração com `LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"))`, misturando o fuso local da JVM com um deslocamento fixo. Em container com TZ UTC, o token expira três horas antes do previsto. A variável `JWT_EXPIRATION_HOURS`, presente em `.env.example` e no compose, não é lida em nenhum ponto do código.

**Ação:** usar `Instant.now().plus(Duration.ofHours(n))` com `n` obtido da configuração.

---

## 3. Padronização

O documento `REFACT_ARCHITECTURE.md` já define os padrões oficiais. Os itens abaixo são desvios em relação a ele.

### P-01 — Nomenclatura de arquivos
Seção 3.2 do documento exige `kebab-case`. Em `apps/apae/src`, desconsiderando `components/ui` (convenção shadcn), **62 de 150 arquivos** violam a regra, distribuídos entre `PascalCase` (34) e `camelCase` (13).

### P-02 — Idioma do código
Seção 3.1 exige inglês. Permanecem em português: `hooks/profissional/` (12 arquivos), `services/profissional-service.ts`, `schemas/transtornosSchema.ts`, `schemas/anualRegistrySchema.ts`, `types/profissional.ts`, `app/disorders/TranstornosListItem.tsx` e as rotas `app/api/patients/filtros/[slug]` e `app/api/patients/[id]/registro-anual/`.

### P-03 — Resposta das rotas BFF
Entre as 38 rotas em `app/api/`, coexistem `NextResponse.json(...)` e `new NextResponse(JSON.stringify(...))` (6 arquivos), e o corpo de erro alterna entre `{ message: string }`, `{ message: object }` e o payload bruto do backend. A indentação varia entre 2 e 4 espaços no mesmo diretório.

**Ação:** criar um utilitário `handleApiError` e um `createBffRoute` que padronizem corpo, status e log das 64 blocos `catch` existentes.

### P-04 — Mapeamento de conflito para 200
`app/api/disorders/route.ts:44` e `app/api/vaccines/route.ts:14` convertem HTTP 409 do backend em HTTP 200 com mensagem de sucesso, mascarando o conflito para o cliente.

**Ação:** propagar o 409 e tratar o caso no consumidor.

### P-05 — Estratégia de erro no backend
Convivem três abordagens: exceções de domínio com `@ControllerAdvice` (39 métodos handler para 43 classes de exceção), `ResponseStatusException` com mensagens em português embutidas (11 ocorrências em 3 arquivos) e `EntityNotFoundException` do JPA.

**Ação:** unificar em uma exceção base de domínio que carregue o `HttpStatus`, reduzindo os 39 handlers a um único.

### P-06 — Localização e nome dos pacotes de exceção
Os `@ControllerAdvice` estão em três convenções distintas: `<módulo>/exceptions/`, `<módulo>/application/exceptions/` e `common/exceptions/handler/`. `ServiceAreaExceptionHandler` reside no módulo `servicearea` mas trata exceções declaradas no módulo `professional`.

### P-07 — Pacotes de mapeadores
Quatro convenções em uso: `appointment/mapper/`, `address/application/mapper/`, `patient/application/mappers/` e `documents/domain/mappers/`.

### P-08 — Anotação transacional
`AppointmentApplicationServiceImpl` e `AbsenceApplicationServiceImpl` usam `jakarta.transaction.Transactional`; os demais 11 serviços usam a anotação do Spring, que oferece controle de propagação e `readOnly`. `ServiceAreaApplicationService` declara a anotação na interface, e não na implementação.

---

## 4. Refatorações Estruturais

### R-01 — Remoção do `apps/management-app`
A aplicação não possui Dockerfile, não é referenciada pelo `docker-compose.yml`, não tem pipeline de CI e não é alvo dos scripts em `.scripts/`. Suas cinco páginas estão reimplementadas em `apps/apae`. O `middleware.ts` redireciona para `/auth/login`, rota que não existe no projeto, e o `tsconfig.json` referencia diretórios inexistentes (`../next-app/`, `../agendamento/`). Último commit em 04/06/2026, contra 21/06/2026 em `apps/apae`.

**Ação:** remover o diretório e a entrada correspondente no workspace.

### R-02 — Estruturas duplicadas em `apps/apae`
A migração para domínios não removeu a estrutura anterior. Casos identificados:

| Artefato | Localizações |
|---|---|
| Esquema de transtornos | `schemas/transtornosSchema.ts`, `schemas/disorder-schemas.ts`, `domains/disorders/disorders.schema.ts` |
| Card de categorias de documento | `components/DocumentCategoriesCard.tsx`, `components/shared/DocumentCategoriesCard.tsx`, `domains/documents/shared/document-categories-card.tsx` |
| Card de arquivo | `components/fileCard.tsx`, `components/shared/fileCard.tsx`, `domains/documents/shared/file-card.tsx` |
| Alerta de restauração | `components/FileRestoreAlert.tsx`, `domains/documents/shared/file-restore-alert.tsx` |
| Filtros de busca | `components/search-filters.tsx`, `components/shared/filters/search-filters.tsx` |
| Documentos do profissional | `hooks/profissional/use-professional-docs.ts` (94 linhas), `use-professional-documents.ts` (48), `use-update-professional-documents.ts` (44) |

Em cada caso, apenas uma versão é importada; as demais são código morto que induz erro em manutenção.

### R-03 — Contextos CRUD duplicados
`hooks/use-disorders.tsx` (256 linhas) e `hooks/use-vaccines.tsx` (255 linhas) são idênticos exceto pelo nome da entidade e pela indentação. `hooks/use-cares.ts` segue o mesmo desenho em escala menor. No backend, `DisorderApplicationServiceImpl` e `VaccineApplicationServiceImpl` repetem a estrutura.

**Ação:** extrair uma fábrica genérica de contexto CRUD no frontend e uma classe base ou serviço genérico no backend.

### R-04 — Serviço de agendamentos sobrecarregado
`AppointmentApplicationServiceImpl.java` tem 549 linhas e injeta 8 repositórios e 3 mapeadores, dos quais 5 pertencem a outros módulos (`patient`, `professional`), violando as fronteiras da arquitetura modular. Dois mapeadores são instanciados com `new` no construtor (linhas 99-100), enquanto os demais são injetados.

**Ação:** dividir por caso de uso (criação, recorrência, consulta) e substituir o acesso direto a repositórios de outros módulos por interfaces de serviço de aplicação.

### R-05 — Implementações de controlador fora dos módulos
As 11 classes `@RestController` residem no pacote plano `br.org.apae.api.controllers.<módulo>`, enquanto suas interfaces estão em `br.org.apae.api.<módulo>.interfaces.controllers`. `PatientRecordController` foge do padrão: é uma classe concreta anotada, dentro do módulo `patient`.

**Ação:** mover as implementações para junto de suas interfaces e converter `PatientRecordController` ao padrão interface/implementação.

### R-06 — Duplicação de acesso à API no frontend
Existem cinco definições independentes de URL base com fallback embutido para `http://localhost:8090/apae-geral/api` (`lib/axios.ts`, `lib/client-service.ts`, `domains/auth/auth.api.ts`, e duas rotas em `app/api/patients/[id]/registro-anual/`). O prefixo `/apae-geral` aparece literalmente 86 vezes em 34 arquivos, duplicando o `basePath` já declarado em `next.config.ts`.

`app/api/patients/[id]/registro-anual/route.ts:15-36` reimplementa a extração do token, testando três nomes de cookie e aplicando heurísticas de parsing, em vez de usar `getTokenFromCookie()`.

`domains/auth/auth.api.ts` mistura `fetch` para a rota BFF interna (login) e `axios` direto para o backend Spring (recuperação e redefinição de senha), tornando esses dois fluxos dependentes da configuração de CORS.

**Ação:** centralizar a resolução de URL em um único módulo, eliminar os fallbacks locais e roteirizar todas as chamadas pelo BFF.

### R-07 — Navegação duplicada e desatualizada
`lib/routes.ts` exporta a constante `NAV`, que não é importada em lugar algum; `components/sidebar/sidebar.tsx` repete a estrutura em JSX. A constante lista `/dashboard` e `/documents`, rotas sem página correspondente em `app/`.

---

## 5. Infraestrutura, Build e CI

### I-01 — Ausência de ferramenta de migração de banco
`application.yaml:20` usa `ddl-auto: update`, sem Flyway ou Liquibase. Alterações estruturais são aplicadas por scripts manuais em `.scripts/manuais/`, um deles com a marcação `TODO: Este script deve ser migrado para o Liquibase`. O arquivo `create_mock_views_escolar.sql` cria uma tabela `pacientes` com definição divergente da entidade JPA, estabelecendo duas fontes de verdade para o esquema.

**Ação:** adotar Liquibase com baseline do esquema atual e definir `ddl-auto: validate`.

### I-02 — Gerenciadores de pacote conflitantes
A raiz declara `packageManager: pnpm@10.10.0` e `pnpm-workspace.yaml`, mas também mantém o campo `workspaces` do npm e ambos os lockfiles (`pnpm-lock.yaml`, 224 KB; `package-lock.json`, 315 KB).

**Ação:** remover o campo `workspaces` e o `package-lock.json`.

### I-03 — Build de imagem do frontend sem lockfile
`apps/apae/Dockerfile` executa `npm ci` condicionado à existência de `package-lock.json` no contexto de build (`./apps/apae`), onde o arquivo não existe. O build recai em `npm install`, resolvendo dependências sem fixação e ignorando o `pnpm-lock.yaml`. A imagem publicada não é reproduzível e diverge do ambiente validado em CI, que usa pnpm.

**Ação:** migrar o Dockerfile para pnpm com `--frozen-lockfile`, copiando o lockfile da raiz do workspace.

### I-04 — Workflows de CI inoperantes
`demo.yml`, `documentos-digitalizados.yml` e `orchestrator.yml` referenciam Gradle (`./gradlew`), o diretório `api/` e os módulos `api/demo/` e `api/documentos-digitalizados/`. O projeto usa Maven em `apps/api/` e nenhum desses módulos existe. O `orchestrator.yml` dispara em push e pull request de qualquer branch, executando detecção de mudanças que nunca resulta em verdadeiro.

**Ação:** remover os três arquivos.

### I-05 — Divergências entre pipelines
`backend.yml` usa `actions/checkout@v5`; os demais usam `@v4`. `frontend.yml` executa `pnpm install --frozen-lockfile` dentro de `apps/apae`, fora da raiz do workspace, e não executa etapa de lint. Não há pipeline para `apps/management-app`.

### I-06 — Variáveis de ambiente inertes
Além de `JWT_EXPIRATION_HOURS` (C-09) e `CORS_ALLOWED_ORIGINS` (C-02), o `docker-compose.yml:78` injeta `NEXT_PUBLIC_API_URL` no container em execução, sem efeito: variáveis `NEXT_PUBLIC_*` são incorporadas ao bundle no momento do build. `NEXT_PUBLIC_USE_MOCK_DATA` e `NEXT_PUBLIC_MANAGEMENT_API_URL` constam em `.env.example` sem uso no código.

### I-07 — Defeitos nos scripts de desenvolvimento
- `.scripts/setup.sh:14-15` lê `POSTGRES_NAME` e `POSTGRES_USERNAME`; `.env.example` define `POSTGRES_DB` e `POSTGRES_USER`. Os valores padrão sempre prevalecem, e o script falha se o desenvolvedor alterar as credenciais.
- `.scripts/run-app.sh:66` referencia `$DB_PORT` em mensagem de erro; a variável definida é `POSTGRES_PORT`.
- `run_frontend()` não chama `load_env()`, ao contrário de `run_backend()`.

---

## 6. Código Morto

Arquivos sem qualquer importação em `apps/apae/src`, confirmados por varredura de referências:

| Arquivo | Observação |
|---|---|
| `lib/client-service.ts` | Retorna URL fixa de localhost |
| `lib/routes.ts` | Substituído por JSX em `sidebar.tsx` (R-07) |
| `lib/health-areas.ts` | — |
| `schemas/anualRegistrySchema.ts` | Substituído pela versão em `domains/patients/annual-registry/` |
| `types/vaccine.ts`, `types/disorder.ts` | Substituídos por `domains/*/*.types.ts` |
| `hooks/use-cares.ts`, `hooks/profissional/use-states-cities.ts` | — |
| `components/shared/DataTable.tsx` | — |
| `components/forms/EditAppointmentForm.tsx` | — |
| `components/buttons/ConfirmCompletionButton.tsx` | — |
| `components/ui/scroll-area.tsx` | — |
| `components/creatable-multi-select/index.ts` e `types/index.ts` | Barrel não utilizado; importações apontam para os arquivos internos |
| `app/disorders/TranstornosListItem.tsx` | Substituído por `domains/disorders/shared/disorder-list-item.tsx` |
| `domains/patients/types/address.ts`, `types/kinship.ts` | — |

Somam-se os arquivos redundantes listados em R-02 e o diretório vazio `APAE/` na raiz do projeto.

---

## 7. Roteiro Sugerido

**Etapa 1 — Correção (imediata)**
C-01, C-02, C-03, C-04, C-06, C-07. São alterações localizadas, sem impacto estrutural, e cobrem as exposições de segurança e a falha de integração em container.

**Etapa 2 — Limpeza (baixo risco)**
R-01, Seção 6, I-02, I-04. Remoção de código e configuração sem consumidores, reduzindo a superfície antes das refatorações.

**Etapa 3 — Infraestrutura**
I-01, I-03, I-05, I-06, I-07. A adoção de migrações (I-01) deve preceder qualquer alteração de entidade.

**Etapa 4 — Padronização**
P-01 a P-08, C-05, C-08, C-09. Recomenda-se automatizar a verificação: regra de ESLint para nomenclatura de arquivos no frontend e Checkstyle no backend, integrados ao pipeline, de modo que os padrões de `REFACT_ARCHITECTURE.md` deixem de depender de revisão manual.

**Etapa 5 — Refatoração estrutural**
R-02 a R-07. Conclusão da migração para fatiamento vertical, domínio por domínio, com remoção da estrutura antiga a cada domínio concluído. Domínios ainda não migrados: `appointments`, `absences`, `service-types`, `service-areas`.
