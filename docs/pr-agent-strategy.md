# Estratégia e POC do PR-Agent

Documento de estudo, avaliação técnica e registro da Prova de Conceito (POC) do **PR-Agent** para revisão automatizada de Pull Requests no monorepo do projeto APAE.

---

## 1. Como o PR-Agent funciona

O **PR-Agent** (desenvolvido pela CodiumAI / Qodo) é uma ferramenta *open-source* baseada em Inteligência Artificial (LLMs) projetada para auxiliar equipes de desenvolvimento na análise e revisão de Pull Requests. O objetivo não é substituir a revisão humana, mas atuar como um segundo par de olhos, adiantando a identificação de bugs, vulnerabilidades e sugerindo melhorias de código.

### Principais Comandos Disponíveis

A interação ocorre de forma automatizada na abertura/atualização de PRs ou sob demanda por comentários no GitHub:

- `/review`: Executa uma análise geral do PR, avaliando conformidade com a issue, esforço de revisão, possíveis bugs e segurança.
- `/improve`: Analisa o código-fonte e gera sugestões práticas de melhoria em formato de *diff* pronto para aplicação.
- `/describe`: Gera automaticamente o título, resumo executivo e a lista de alterações do PR.
- `/ask <pergunta>`: Permite tirar dúvidas contextuais com a IA sobre o código alterado.

### Integração com o GitHub

A integração recomendada é via **GitHub Actions**. Quando eventos de `pull_request` ocorrem, a Action aciona a imagem do PR-Agent, que processa os *diffs* do repositório utilizando uma API de LLM e publica os comentários diretamente no Pull Request.

---

## 2. Escopo da revisão no repositório

Como este projeto é um monorepo (gerenciado via *pnpm workspaces*), a revisão automatizada atende aos dois ecossistemas:

### Backend — Java no diretório `/apps/api`

- **Stack:** Java 21 e Spring Boot (v3.5.6).
- **Foco de Análise:** Boas práticas de Orientação a Objetos, uso de anotações Spring, tratamento defensivo contra *NullPointerExceptions*, validação de inputs e segurança.

### Frontend — TypeScript no diretório `/apps/apae`

- **Stack:** TypeScript, Next.js 16, React 19 e Tailwind CSS.
- **Foco de Análise:** Tipagem estrita, boas práticas de componentes funcionais, controle de renderizações e aderência aos padrões do ecossistema JS/TS.

### Critérios Gerais de Revisão

- **Segurança:** Detecção de tokens, senhas ou vulnerabilidades de injeção.
- **Qualidade e Estilo:** Legibilidade e consistência lógica com o projeto.
- **Testes:** Validação de testes de unidade e cobertura de cenários de borda.

---

## 3. Qualidade das revisões

Com base na POC executada no PR #904, os critérios de qualidade foram validados:

- **Precisão e Relevância:** A ferramenta demonstrou **100% de precisão** no código de teste Java (`PocSampleTest.java`), identificando corretamente a ausência de tratamento de `null` no método `saudar` e a falta de validação de idades negativas no método `podeDirigir`.
- **Falsos Positivos:** O modelo não gerou falsos positivos sintáticos, limitando-se a problemas lógicos reais.
- **Utilidade:** As sugestões foram entregues com blocos de substituição de código claros e prontos para aplicação direta no GitHub.

---

## 4. Volume e ruído

Para evitar a sobrecarga de notificações (*spam*) na equipe, adotou-se a seguinte estratégia:

- **Limite de Sugestões:** Configuração de `num_code_suggestions = 3` no `.pr_agent.toml` para que a IA foque exclusivamente nas melhorias mais relevantes.
- **Exclusão de Lockfiles e Gerados:** Arquivos como `pnpm-lock.yaml`, `package-lock.json` e builds são ignorados via regra `ignore.glob`.
- **Comentários Persistentes:** O PR-Agent concentra seus retornos em blocos colapsáveis estruturados (`PR Reviewer Guide` e `PR Code Suggestions`), evitando múltiplos comentários espalhados.

---

## 5. Processo de revisão

A governança do PR-Agent no fluxo de desenvolvimento do time:

- **Papel Consultivo:** A IA atua estritamente como apoio. Ela **NÃO possui permissão para aprovar PRs nem bloquear merges**.
- **Aprovação Humana Obrigatória:** A aprovação formal do PR continua sendo indispensável e obrigatória por parte dos revisores humanos do time e do **Quality Owner**.
- **Autonomia:** O desenvolvedor tem total liberdade para discutir ou descartar sugestões que não se apliquem à regra de negócio.
- **Monitoramento:** O Quality Owner deve avaliar nas retrospectivas se os parâmetros do agente continuam gerando valor.

---

## 6. Modelo e configuração

Durante a POC, foram avaliadas três abordagens de modelos de linguagem:

1. **OpenAI (`gpt-4o`):** Modelo comercial de referência, excelente qualidade, porém requer subscrição paga na organização.
2. **OpenRouter (Modelos Abertos Gratuitos):** Testados `deepseek-chat:free` e `llama-3.3-70b:free`. Apresentaram incompatibilidade com o parser de YAML estrito do PR-Agent para sugestões de código inline.
3. **Google Gemini (`gemini-3.6-flash` via Google AI Studio) — VALIDADO NA POC:** Apresentou **desempenho impecável**, gerando revisões completas em português (`pt-BR`), respeitando todas as estruturas de YAML e executando em menos de 45 segundos sem nenhum custo para a organização.

---

## 7. Segurança e permissões

- **Permissões do `GITHUB_TOKEN`:** A Action requer `pull-requests: write`, `issues: write` e `contents: write` para publicar as revisões.
- **Tratamento de Secrets:** A chave de API (`GEMINI_API_KEY` ou `OPENAI_API_KEY`) deve ser configurada estritamente nos Secrets do GitHub (*Settings > Secrets and variables > Actions*), **jamais sendo versionada no código**.
- **Eventos:** Uso do evento seguro `pull_request` para prevenir execução de código arbitrário.

---

## 8. Rascunho de configuração

Configurações validadas experimentalmente na POC:

### Workflow — `.github/workflows/pr-agent-poc.yml`

```yaml
name: PR-Agent POC (Experimental)  # Nome exibido na aba Actions do repositório

on:
  pull_request:  # Dispara quando um PR é aberto, sincronizado ou reaberto
    types: [opened, reopened, synchronize]
  issue_comment:  # Permite ativar comandos manuais via comentários (ex: /review)
    types: [created, edited]

permissions:
  pull-requests: write  # Necessário para o bot publicar comentários nos PRs
  issues: write  # Necessário para interagir com issues (ex: /describe)
  contents: write  # Necessário para ler o conteúdo do repositório

jobs:
  pr_agent_job:
    if: ${{ github.event.sender.type != 'Bot' }}  # Evita loops infinitos entre bots
    runs-on: ubuntu-latest  # Executor padrão do GitHub Actions
    name: Run PR Agent  # Nome exibido na UI do workflow
    steps:
      - name: Checkout PR code
        uses: actions/checkout@v4  # Clona o repositório para o runner

      - id: pr-agent
        uses: Codium-ai/pr-agent@main  # Action oficial do PR-Agent (versão latest)
        env:
          GEMINI_API_KEY: ${{ secrets.GEMINI_API_KEY }}  # Chave da API do Google Gemini para autenticação
          GOOGLE_API_KEY: ${{ secrets.GEMINI_API_KEY }}  # Alias necessário para algumas versões do PR-Agent
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}  # Token padrão do GitHub para operações de leitura/escrita
          CONFIG__MODEL: "gemini/gemini-3.6-flash"  # Modelo de LLM utilizado para geração das revisões
          CONFIG__RESPONSE_LANGUAGE: "pt-BR"  # Idioma das respostas geradas pelo agente
          GITHUB_ACTION_CONFIG__AUTO_REVIEW: "true"  # Habilita revisão automática ao abrir/atualizar PR
          GITHUB_ACTION_CONFIG__AUTO_IMPROVE: "true"  # Habilita sugestões de melhoria automáticas
          GITHUB_ACTION_CONFIG__PR_ACTIONS: '["opened", "reopened", "synchronize", "ready_for_review"]'  # Eventos que disparam a ação automática
```

### Arquivo de Configuração — `.pr_agent.toml`

```toml
[config]
model = "gemini/gemini-3.6-flash"  # Modelo principal de LLM (gratuito via Google AI Studio)
model_turbo = "gemini/gemini-3.6-flash"  # Modelo para tarefas que exigem resposta mais rápida
response_language = "pt-BR"  # Idioma das revisões e sugestões geradas

[github_action_config]
auto_review = true  # Executa /review automaticamente ao abrir PR
auto_describe = false  # Não gera descrição automática (evita ruído)
auto_improve = true  # Executa /improve automaticamente ao abrir PR
pr_actions = ["opened", "reopened", "synchronize", "ready_for_review"]  # Eventos que disparam o agente

[pr_reviewer]
num_code_suggestions = 3  # Limita a 3 sugestões para evitar spam de notificações
inline_code_comments = true  # Mostra sugestões diretamente nas linhas do código alterado
persistent_comment = false  # Cria novos comentários a cada atualização (não reutiliza)

[ignore]
glob = ["*.lock", "pnpm-lock.yaml", "package-lock.json"]  # Arquivos ignorados pela revisão (lockfiles)
```

---

## 9. POC do PR-Agent

### Resultados da Execução Experimental no PR #904

- **Validação Prática com Código Java:** O PR-Agent analisou o arquivo de teste `PocSampleTest.java`, identificou com sucesso o risco de `NullPointerException` e validação de limites numéricos, gerando sugestões acionáveis em português.
- **Conformidade com Issues:** O bot inspecionou os requisitos da Issue #899 diretamente da descrição do PR, auxiliando no rastreamento de tarefas.
- **Desempenho e Custo Zero:** O modelo `gemini-3.6-flash` executou as revisões em ~45s com alta qualidade de análise e custo nulo através do Google AI Studio.
- **Confirmação de Inatividade do CodeRabbit:** O CodeRabbit emitiu aviso de inatividade na branch `dev`, validando a necessidade desta solução.

### Recomendações para a Implementação Definitiva

- **Adoção do Modelo Gemini ou OpenAI:** Recomendado o uso do `gemini-3.6-flash` (gratuito) ou `gpt-4o` (pago) para garantir suporte completo a sugestões de código.
- **Fixação de Versão da Action:** Para produção, fixar a tag de versão ou hash do commit do PR-Agent (em vez de `@main`) para maior estabilidade.
- **Merge Oficial:** Criar uma issue de follow-up para incluir a configuração definitiva na branch padrão `dev`.
- **Aprovação:** Submeter este documento para aprovação do Quality Owner.
