# Estratégia e POC do PR-Agent

## 1. Como o PR-Agent funciona

O **PR-Agent** (desenvolvido pela CodiumAI) é uma ferramenta *open-source* que utiliza Inteligência Artificial (LLMs) para analisar e revisar Pull Requests de forma automatizada. Seu objetivo não é substituir a revisão humana, mas adiantar o trabalho identificando bugs, sugerindo melhorias de código e resumindo as alterações para a equipe.

### Principais comandos disponíveis

A interação com a ferramenta é feita diretamente via comentários no próprio Pull Request no GitHub. Os principais comandos são:

| Comando         | Descrição                                                                                     |
| --------------- | --------------------------------------------------------------------------------------------- |
| `/review`       | Analisa o PR e gera um feedback geral apontando possíveis problemas de lógica e bugs.         |
| `/describe`     | Gera automaticamente o título e a descrição do PR, além de listar as alterações feitas.        |
| `/improve`      | Sugere melhorias de código (focando em boas práticas e performance), muitas vezes com trechos de código prontos para aplicar. |
| `/ask <pergunta>` | Permite que o desenvolvedor tire dúvidas com a IA sobre o código específico daquele PR.      |

### Integração com o GitHub

A ferramenta obtém as informações do PR lendo os eventos do repositório. Para este projeto, a integração sugerida é via **GitHub Actions**. Quando um Pull Request é criado ou um comando é digitado, a Action aciona o PR-Agent, que processa as diferenças de código (*diffs*) usando a API de um provedor de IA e retorna a resposta como um comentário no PR.

---

## 2. Escopo da revisão no repositório

Como este projeto é um monorepo, a revisão automatizada precisa ser capaz de lidar de forma inteligente com diferentes ecossistemas. O escopo de avaliação do PR-Agent deve cobrir:

### Backend — Java no diretório `/apps/api`

- A IA deve compreender código Java 21 e o framework **Spring Boot 3.5.6**.
- **Foco de análise:** boas práticas de Orientação a Objetos, uso correto das anotações do Spring, tratamento de exceções, vulnerabilidades no backend e otimização.

### Frontend — TypeScript no diretório `/apps/apae`

- A IA deve compreender o ecossistema **TypeScript**, **Next.js 16**, **React 19** e **Tailwind CSS**.
- **Foco de análise:** boas práticas de estruturação, identificação de códigos que possam causar gargalos de performance no navegador, e aderência a padrões do ecossistema JS.

### Critérios gerais de revisão

- **Qualidade e Consistência:** o código adicionado faz sentido no contexto do repositório? A legibilidade está boa?
- **Segurança:** a ferramenta deve ser capaz de apontar se o PR introduz senhas, tokens de API vazados ou falhas comuns de segurança.
- **Testes:** alertar se PRs grandes foram enviados sem testes de unidade e verificar se os testes criados fazem sentido.

---

## 3. Qualidade das revisões

> _Avaliar e documentar critérios de qualidade das revisões geradas: precisão das sugestões, relevância dos comentários, capacidade de detectar bugs e code smells, aderência aos padrões do projeto e taxa de falsos positivos._

## 4. Volume e ruído

> _Analisar o volume esperado de comentários e comentários irrelevantes (ruído). Definir estratégias de filtragem e thresholds para evitar spam de revisões, garantindo que apenas feedbacks acionáveis cheguem aos desenvolvedores._

## 5. Processo de revisão

> _Descrever o fluxo atual de code review no time e como o PR-Agent se encaixa nele. Definir quando o agente é acionado (abertura do PR, push de commits, comando manual), quem valida as sugestões e como elas são aplicadas ou descartadas._

## 6. Modelo e configuração

> _Especificar qual modelo de linguagem será utilizado (provider, versão, custo), os parâmetros de configuração (temperatura, tokens máximos, system prompts customizados) e como esses valores afetam a qualidade e o custo das revisões._

## 7. Segurança e permissões

Para que o PR-Agent funcione via GitHub Actions, ele precisa de permissões específicas de leitura e escrita. Durante a POC, identificamos as seguintes necessidades de segurança:

### Permissões do `GITHUB_TOKEN`

A Action exige permissões de escrita em `issues`, `pull-requests` e `contents` para conseguir ler o código, ler os comentários e postar a revisão de volta no PR.

> ℹ️ Nos workflows existentes (`backend.yml`, `frontend.yml`), nenhuma permissão explícita é declarada — o token assume os defaults do repositório. O `ghcr-publish.yml` já declara `contents: read` + `packages: write` como referência de boas práticas.

### Tratamento de Secrets

O PR-Agent não traz um modelo de IA embutido — ele consulta uma API externa (como OpenAI, Anthropic, etc). Portanto, a chave de API (`OPENAI_API_KEY` ou similar) **jamais deve ser exposta no código**. Ela deve ser configurada obrigatoriamente na aba **Settings → Secrets and variables → Actions** do repositório.

> ℹ️ O `.gitignore` do projeto já exclui `.env` e `.env.*` (mantendo apenas `.env.example` como template). O arquivo `.env.example` contém valores placeholder para variáveis sensíveis como `JWT_SECRET`, `MINIO_SECRET_KEY` e `POSTGRES_PASSWORD`.

### Riscos do `pull_request_target`

O uso do evento `pull_request` é mais seguro. O evento `pull_request_target` tem acesso aos secrets do repositório base e pode ser perigoso se PRs vierem de *forks* maliciosos. Como este é um repositório interno (monorepo), o risco é menor, mas recomenda-se o uso restrito de secrets no pipeline de review.

> ℹ️ Nenhuma workflow existente neste repositório utiliza `pull_request_target` — todas usam `pull_request`, o padrão mais seguro.

---

## 8. Rascunho de configuração

Abaixo está o rascunho da configuração de GitHub Actions utilizado experimentalmente na Prova de Conceito (POC).

> ⚠️ **Nota:** Esta é uma configuração experimental para validação e **não deve** ser promovida para a branch principal sem a aprovação do time e do Quality Owner.

### Workflow — `.github/workflows/pr-agent.yml`

```yaml
name: PR-Agent POC (Experimental)

on:
  pull_request:
    types: [opened, reopened, synchronize]
  issue_comment:
    types: [created, edited]

permissions:
  pull-requests: write
  issues: write
  contents: write

jobs:
  pr_agent_job:
    if: ${{ github.event.sender.type != 'Bot' }}
    runs-on: ubuntu-latest
    name: Run PR Agent
    steps:
      - id: pr-agent
        uses: Codium-ai/pr-agent@main
        env:
          OPENAI_KEY: ${{ secrets.OPENAI_API_KEY }}
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

### Arquivo de configuração — `.pr_agent.toml`

> ℹ️ O PR-Agent lê configurações adicionais de um arquivo `.pr_agent.toml` na raiz do repositório. Abaixo um rascunho inicial para a POC com as opções mais relevantes.

```toml
# See: https://github.com/Codium-ai/pr-agent/blob/main/pr_agent/settings/configuration.toml
[config]
model = "gpt-4o"                    # Modelo principal para revisão
model_turbo = "gpt-4o-mini"         # Modelo turbo para tarefas leves
response_language = "pt-BR"          # Idioma das respostas (PT-BR)

[github_action_config]
auto_review = true                  # Revisão automática ao abrir PR
auto_describe = false                # Não gera descrição automática (via /describe manual)
auto_improve = true                  # Sugere melhorias automaticamente
pr_actions = ["opened", "reopened", "ready_for_review", "review_requested"]

[pr_reviewer]
num_code_suggestions = 3             # Nº de sugestões de código por revisão
inline_code_comments = true          # Comentários inline no diff
persistent_comment = false           # Novo comentário a cada push (não atualiza o anterior)
enable_review_labels_security = true # Adiciona label "possible security issue"
enable_review_labels_effort = true   # Adiciona label "Review effort [1-5]"

[pr_code_suggestions]
persistent_comment = false

[ignore]
glob = ["*.lock", "pnpm-lock.yaml", "package-lock.json"]  # Ignorar arquivos de lock
```

---

## 9. POC do PR-Agent

> _Planejar e documentar a prova de conceito (POC): objetivos específicos, métricas de sucesso, cenários de teste, timeframe, quem participa e critérios de aprovação para decidir se o PR-Agent segue para adoção ampla no time._
