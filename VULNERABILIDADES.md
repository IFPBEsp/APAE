# Relatorio de vulnerabilidades e decisoes

Analise das vulnerabilidades HIGH e CRITICAL apontadas pelo Trivy no repositorio APAE.

## Backend (`apps/api`)

| Pacote | CVE | Severidade | Decisao / Acao |
|---|---|---|---|
| `spring-boot` | CVE-2026-40973, CVE-2026-22731, CVE-2026-22733 | HIGH | Atualizado o parent Spring Boot de 3.5.6 para 3.5.16 |
| `spring-data-commons` | CVE-2026-41695, CVE-2026-41716 | HIGH | Atualizado para 3.5.13 via Spring Boot 3.5.16 |
| `spring-security-web` | CVE-2026-22732 | CRITICAL | Atualizado para 6.5.11 via Spring Boot 3.5.16 |
| `spring-expression` | CVE-2026-41850 | HIGH | Atualizado para 6.2.19 via Spring Boot 3.5.16 |
| `spring-webmvc` | CVE-2026-41842, CVE-2026-41845 | HIGH | Atualizado para 6.2.19 via Spring Boot 3.5.16 |
| `spring-webmvc` | CVE-2026-47884, CVE-2026-47890 | CRITICAL | Ignorado com justificativa no `.trivyignore`: nao existe patch na linha 6.2.x; a correcao exige Spring Framework 7 (Spring Boot 4.x). A aplicacao nao utiliza `XsltView` nem SSE com view |
| `tomcat-embed-core` | CVE-2026-41293, CVE-2026-43512, CVE-2026-43515, CVE-2026-55276, CVE-2026-65182, CVE-2026-65905, CVE-2026-68525 | CRITICAL | Atualizado de 10.1.46 para 10.1.61 via propriedade `tomcat.version` |
| `tomcat-embed-core` | CVE-2026-24734, CVE-2026-24880, CVE-2026-34483, CVE-2026-34487, CVE-2026-41284, CVE-2026-42498, CVE-2026-43513, CVE-2026-53404 | HIGH | Atualizado de 10.1.46 para 10.1.61 via propriedade `tomcat.version` |
| `jackson-core` | CVE-2026-68494, CVE-2026-89407, CVE-2026-89425 | HIGH | Atualizado de 2.19.2 para 2.21.8 via propriedade `jackson-bom.version` |
| `jackson-databind` | CVE-2026-54512, CVE-2026-54513, CVE-2026-68497, CVE-2026-91776, CVE-2026-91777 | HIGH | Atualizado de 2.19.2 para 2.21.8 via propriedade `jackson-bom.version` |
| `micrometer-core` | CVE-2026-40983, CVE-2026-40984 | HIGH | Atualizado para 1.15.12 via Spring Boot 3.5.16 |
| `guava` | CVE-2026-102554 | HIGH | Override para 33.8.0-jre no `dependencyManagement` |
| `bcprov-jdk18on` | CVE-2025-14813, CVE-2026-8763 | CRITICAL | Override para 1.86 no `dependencyManagement` |
| `bcprov-jdk18on` | CVE-2026-13506, CVE-2026-5598 | HIGH | Override para 1.86 no `dependencyManagement` |
| `postgresql` | CVE-2026-42198, CVE-2026-54291 | HIGH | Atualizado de 42.7.7 para 42.7.14 via propriedade `postgresql.version` |
| `snappy-java` | CVE-2026-108106, CVE-2026-93451 | HIGH | Override para 1.1.10.12 no `dependencyManagement` |
| `minio` | CVE-2025-59952 | HIGH | Atualizado de 8.5.17 para 8.6.0 |

Observacao: o MinIO 8.6.0 usa o artefato multiplataforma `okhttp`, cujas classes JVM ficam em `okhttp-jvm`. Foi adicionada a dependencia `com.squareup.okhttp3:okhttp-jvm:5.5.1` para manter o build funcional.

## Frontend (`apps/apae`)

| Pacote | CVE | Severidade | Decisao / Acao |
|---|---|---|---|
| `next` | CVE-2026-75604, GHSA-2464-fhr4-w9xw, GHSA-2xp9-vwfh-vxw4, GHSA-673q-mjm6-r5xr, GHSA-vcvr-r3jv-pc5j | CRITICAL | Atualizado de 16.2.3 para 16.4.5 |
| `next` | CVE-2026-44573, CVE-2026-44574, CVE-2026-44575, CVE-2026-44578, CVE-2026-44579, CVE-2026-45109, CVE-2026-64641, CVE-2026-64642, CVE-2026-64645, CVE-2026-64649, CVE-2026-94482, CVE-2026-94483, GHSA-8h8q-6873-q5fj | HIGH | Atualizado de 16.2.3 para 16.4.5 |
| `axios` | CVE-2026-101898, CVE-2026-101901, CVE-2026-101905, CVE-2026-101906, CVE-2026-101909, CVE-2026-25639, CVE-2026-42033, CVE-2026-42035, CVE-2026-42043, CVE-2026-42264, CVE-2026-44486, CVE-2026-44487, CVE-2026-44488, CVE-2026-44492, CVE-2026-44494, CVE-2026-44495, CVE-2026-44496, CVE-2026-67320 | HIGH | Atualizado de 1.15.2 para 1.21.0 |
| `form-data` | CVE-2026-12143 | HIGH | Override para 4.0.6 |
| `nanoid` | CVE-2026-67213, CVE-2026-67214, CVE-2026-73086 | HIGH | Override para 3.3.18 |
| `postcss` | CVE-2026-45623, CVE-2026-73646 | HIGH | Override para 8.5.32 |
| `sharp` | GHSA-f88m-g3jw-g9cj, GHSA-rgj7-g3m4-5g8c, GHSA-wq5f-xc86-pv6w | HIGH | Override para 0.35.5 |
| `source-map-js` | CVE-2026-93749 | HIGH | Override para 1.2.2 |
| `zod` | CVE-2023-54404 | HIGH | Atualizado de 4.3.6 para 4.6.7 |

## Dependencias de desenvolvimento do frontend

| Pacote | CVE | Severidade | Decisao / Acao |
|---|---|---|---|
| `vitest` | CVE-2026-47429 | CRITICAL | Atualizado de 2.1.9 para 5.1.0 |
| `tinypool` | CVE-2026-104848, CVE-2026-104849 | CRITICAL | Override para 2.2.0 |
| `vite` | CVE-2026-112942 | HIGH | Atualizado para 8.3.6 |
| `shell-quote` | CVE-2026-9277 | CRITICAL | Override para 1.12.0 |
| `shell-quote` | CVE-2026-13311 | HIGH | Override para 1.12.0 |
| `brace-expansion` | CVE-2026-13149, CVE-2026-14257, CVE-2026-69152, CVE-2026-102276, CVE-2026-102278 | HIGH | Override para 1.1.20 na linha 1.x e 5.0.12 na linha 5.x |
| `follow-redirects` | CVE-2026-111755, CVE-2026-111756, CVE-2026-116689 | HIGH | Override para 1.16.2 |
| `js-yaml` | CVE-2026-59869, CVE-2026-84375, GHSA-5p4m-2wfm-xmqj | HIGH | Override para 4.3.2 |

## Lockfile

O `package-lock.json` da raiz estava obsoleto e continha um workspace inexistente (`apps/agendamento`) com Next 15.4.5, Axios 1.13.2 e `js-cookie` 3.0.5. Como o projeto declara `pnpm` como gerenciador oficial e os workflows do GitHub Actions usam `pnpm install --frozen-lockfile`, o `package-lock.json` foi removido do git. O `pnpm-lock.yaml` foi atualizado e validado com `pnpm install --frozen-lockfile`.

## Validacao

- `pnpm audit`: nenhuma vulnerabilidade HIGH ou CRITICAL.
- `mvn clean test`: 477 testes executados, sem falhas.
- `pnpm test`: 141 testes executados, sem falhas.
- `pnpm build`: build de producao concluido com sucesso.
- `trivy fs --severity HIGH,CRITICAL .`: somente as duas CVEs de `spring-webmvc` sem patch na linha 6.2.x, ignoradas com justificativa no `.trivyignore`.
