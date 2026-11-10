# AGENT.md

Instruções para agentes de IA (Claude Code e outros) que trabalham neste repositório.

## Visão geral do projeto

Monorepo pnpm do sistema da APAE:

| Caminho | Stack | Comandos |
|---|---|---|
| `apps/api` | Backend Java (Spring / Maven) | `pnpm dev:backend`, `pnpm test:api` |
| `apps/apae` | Frontend Next.js + Vitest | `pnpm dev:apae`, `pnpm lint`, `pnpm test:front` |
| infraestrutura | Docker Compose (Postgres, MinIO, migrações) | `pnpm dev:infra`, `pnpm db:migrate` |

O sistema lida com **dados pessoais e de saúde de pacientes** (nome, CPF, RG, telefone, documentos médicos). Trate qualquer exposição desses dados como impacto alto, por envolver dados sensíveis sob a LGPD.

## Skills disponíveis

As skills do projeto ficam em `.claude/skills/` e são versionadas no repositório.

### `security-audit` (Cloudflare)

Revisão de segurança e detecção de vulnerabilidades, focada no código-fonte.

**Modos de uso:**

| Pedido | Modo | Gera arquivos? |
|---|---|---|
| "use a skill security-audit para revisar X" | Orientação (pergunta ou revisão pontual) | Não |
| "faça uma auditoria de segurança completa com a skill security-audit, perfil `quick`/`standard`/`deep`" | Auditoria completa (6 fases) | Sim |
| "auditoria com security-audit só em `apps/api`" ou "do diff entre `dev` e esta branch" | Auditoria restrita a um trecho | Sim (cobertura parcial) |

- Perfis: `quick` (uma rodada de busca), `standard` (padrão) e `deep` (sistemas críticos).
- Orçamento: o usuário pode limitar o custo, por exemplo "orçamento máximo de 15 agentes".
- Saída padrão, fora do repositório: `~/security-audit-skill/APAE/run-<N>/`.
- Nunca testar ambientes implantados, endpoints reais nem dados reais de pacientes. Use só análise de código e testes locais com dados fictícios.

> Não confundir com a skill nativa `security-review`, que analisa apenas as alterações pendentes da branch atual.

## Regra: relatório de vulnerabilidades em português

**Todo relatório de vulnerabilidades gerado neste repositório deve ser escrito em português do Brasil.** A regra vale para a skill `security-audit` (modo orientação ou auditoria completa), para `/security-review` e para qualquer análise de segurança pedida ao agente.

### O que traduzir

- Todo o texto em prosa de `REPORT.md`, `FINDINGS-DETAIL.md` e `NEEDS-VALIDATION.md`: títulos de seção, resumos, descrições, impacto, justificativa da prioridade, reprodução e correção recomendada.
- Respostas no terminal sobre achados de segurança.

### O que NÃO traduzir

- **`findings.json` e `coverage-ledger.json`**: nomes de campos e valores de enumeração (`confirmed`, `needs_validation`, `rejected`, `critical`, `high` etc.) ficam em inglês, porque os validadores `validate-findings.cjs` e `validate-coverage-ledger.cjs` e o `report-schema.json` dependem deles. Campos de texto livre dentro do JSON (título, descrição, impacto, correção) devem estar em português.
- Código, caminhos de arquivo, nomes de classes e métodos, comandos, mensagens de erro e identificadores (CWE, CVE, fingerprints).

### Estrutura do `REPORT.md`

Siga a estrutura definida pela skill, com estes títulos:

1. **Escopo da execução**: perfil, escopo, orçamento (agentes usados vs. planejados), commit analisado, política de execução (só código-fonte e testes locais em sandbox), execuções anteriores consideradas, itens adiados e fora do escopo. Execuções `quick`, restritas a um trecho ou incompletas devem dizer claramente que a cobertura é **parcial**.
2. **Resumo da postura de segurança**: um parágrafo curto.
3. **Vulnerabilidades confirmadas** (tabela): Gravidade | Título | Limite de confiança afetado | Resultado observado.
4. **Detalhamento de cada vulnerabilidade**: localização no código, principal de menor privilégio (atacante), reprodução local delimitada, condições, resultado obtido, impacto, justificativa da prioridade e menor correção eficaz no código.
5. **Pendentes de validação** (tabela, sem gravidade): título, rastro no código, bloqueio exato, próximo passo local e verificação segura a ser feita pelo responsável pelo deploy.
6. **Recomendações de hardening e boas práticas encontradas**.
7. **Resumo da cobertura**: quantidade de unidades cobertas, candidatas, bloqueadas e adiadas, exclusões importantes e resultado da revisão final de cobertura.

### Gravidade

Na prosa, use o rótulo em português com o valor original entre parênteses:

| JSON | Relatório |
|---|---|
| `critical` | Crítica (critical) |
| `high` | Alta (high) |
| `medium` | Média (medium) |
| `low` | Baixa (low) |
| `informational` | Informativa (informational) |
| `needs_validation` | Pendente de validação (sem gravidade) |

### Cuidados com dados sensíveis

- Nunca incluir no relatório dados reais de pacientes ou profissionais, credenciais, tokens, conteúdo de `.env` ou segredos encontrados. Cite apenas o arquivo e a linha, com o valor mascarado (por exemplo `JWT_SECRET=****`).
- Nas reproduções, use somente dados fictícios (por exemplo `paciente-teste`, CPF `000.000.000-00`).
- Quando a vulnerabilidade expõe dados pessoais ou de saúde, diga isso explicitamente no impacto e mencione a LGPD.

## Convenções gerais

- Branch principal para PRs: `dev`.
- Mensagens de commit seguem Conventional Commits em português, com o número da issue (ex.: `fix: validar res.ok em updatePatientApi #1062`).
- Os relatórios de auditoria não devem ser commitados no repositório, salvo pedido explícito. Nesse caso, use uma pasta ignorada pelo `.gitignore`.
