<p align="center">
  <img src="https://github.com/user-attachments/assets/be92f146-a67b-42bd-8d77-e4e1c02e581a" />
</p>

# APAE

Projeto em desenvolvimento, fruto de uma parceria entre o IFPB (Campus Esperança) e a APAE.

---

## Índice

- [APAE](#apae)
  - [Índice](#índice)
  - [Introdução](#introdução)
  - [Apresentação do Projeto](#apresentação-do-projeto)
    - [Como Executar](#como-executar)
      - [Outros Comandos:](#outros-comandos)
      - [Credenciais do usuário para testes:](#credenciais-do-usuário-para-testes)
  - [Fluxo de Trabalho](#fluxo-de-trabalho)
    - [Convenção de Commits](#convenção-de-commits)
      - [Dicionário de Tipos](#dicionário-de-tipos)
      - [Dicionário de Escopo](#dicionário-de-escopo)
    - [Criação de Branches](#criação-de-branches)
    - [Labels](#labels)
      - [Tipos de Projeto](#tipos-de-projeto)
      - [Equipes](#equipes)
      - [GitFlow](#gitflow)
      - [Outras Labels Úteis](#outras-labels-úteis)
    - [Raia do Kanban](#raia-do-kanban)
  - [Qualidade de Código](#qualidade-de-código)
    - [Hooks de Pre-commit](#hooks-de-pre-commit)
    - [Formatação Manual](#formatação-manual)
    - [Checkstyle e PMD (apps/api)](#checkstyle-e-pmd-appsapi)
  - [Configuração do Projeto](#configuração-do-projeto)
    - [Variáveis de Ambiente (Opcional)](#variáveis-de-ambiente-opcional)
    - [Como configurar](#como-configurar)
    - [Envio de e-mails](#envio-de-e-mails)
    - [Segurança](#segurança)
    - [Observações](#observações)

---

## Introdução

Este projeto tem como objetivo o desenvolvimento de dois sistemas para a APAE, o primeiro focado no gerenciamento de pacientes e o outros na exibição de informações. O projeto está sendo desenvolvido em colaboração com o IFPB (Campus Esperança).

---

## Apresentação do Projeto

### Como Executar

O projeto foi automatizado para rodar com o mínimo de comandos utilizando **pnpm Workspaces**. Siga os passos abaixo:

> **Nota para Windows:** Utilize **GitBash** para executar os comandos.
> 
> ℹ️ Algumas funcionalidades, como envio de e-mails, utilizam configuração opcional de variáveis de ambiente. Veja a seção [**Configuração do Projeto**](#configuração-do-projeto).

#### Passo 1: Setup Inicial

Instale todas as dependências do projeto:

```bash
cd APAE
pnpm install
```

#### Passo 2: Build do Backend

Compile o backend (API Java):

```bash
docker build -t apae-geral-backend:local  ./apps/api
```

> 📝 Para mais detalhes sobre a configuração do Docker para o backend, consulte [`docs/docs-docker/DOCKER_LOCAL.md`](./docs/docs-docker/DOCKER_LOCAL.md)

#### Passo 3: Build do Frontend

Compile o frontend (Next.js):

```bash
docker build -t apae-geral-frontend:local ./apps/apae
```

#### Passo 4: Seed do Banco de Dados

Cria um usuário admin e views em mock no banco de dados para fins de testes:

```bash
pnpm db:seed
```

#### Passo 5: Executar o Projeto

Inicie todos os serviços (Docker + Backend + Frontend):

```bash
pnpm run dev
```

Aguarde até que todos os serviços estejam prontos. O frontend estará acessível em `http://localhost:3000/apae-geral`.

---

#### Outros Comandos Úteis:

- `pnpm dev:backend`: Executa apenas o backend (api).
- `pnpm dev:apae`: Executa apenas o frontend (apae).
- `pnpm docker:up`: Sobe apenas o banco de dados e MinIO.
- `pnpm docker:down`: Para os containers e os remove da memória.
- `pnpm docker:drop`: Para os containers, os remove e apaga os volumes associados.
- `pnpm db:seed`: Cria um usuário admin e views em mock no banco de dados para fins de testes.

#### Credenciais do usuário para testes:

- Email: `admin@teste.com`
- CPF: `123.456.789-00`
- Senha: `123456`

> ℹ️ É necessário criar o usuário teste a partir do comando `pnpm db:seed`.

---

## Fluxo de Trabalho

### Convenção de Commits

Ao fazer um commit, siga o seguinte padrão:

    <tipo>[escopo]: <descrição>

    <Corpo Opcional>

Exemplo:

    feat[service]: adiciona login de usuário

**Observações:** Adicione o corpo do commit somente quando necessário para fornecer um contexto adicional para a alteração. Para adicionar uma quebra de linha na mensagem do commit pelo terminal, use "\n".

#### Dicionário de Tipos

- **feat**: Adição de uma nova funcionalidade ou recurso no projeto.
- **fix**: Correção de um bug ou problema.
- **chore**: Pequenas alterações de manutenção e ajustes.
- **refactor**: Refatoração de código sem adicionar novas funcionalidades ou corrigir bugs.
- **style**: Alterações na formatação do código, lint e outros (não afeta a funcionalidade).
- **docs**: Mudanças na documentação (exemplo: README).
- **test**: Modificações ou adição de testes.
- **perf**: Melhoria de desempenho.
- **ci**: Alterações relacionadas a CI/CD (GitHub Actions, Jenkins, etc.).
- **build**: Mudanças relacionadas a build e dependências.
- **revert**: Reversão de um commit anterior.
- **cleanup**: Remoção de códigos comentados ou trechos desnecessários.
- **remove**: Exclusão de arquivos ou funcionalidades obsoletas.

#### Dicionário de Escopo

**Backend:**

- **auth**: Relacionado à autenticação.
- **database**: Mudanças no banco de dados.
- **api**: Mudanças na API.
- **service**: Alterações na camada de serviços.
- **repository**: Mudanças na camada de repositório.
- **security**: Melhorias na segurança.
- **cache**: Implementação ou alterações no cache.

**Frontend:**

- **ui**: Alterações na interface do usuário.
- **componentes**: Modificações em componentes reutilizáveis.
- **layout**: Alterações no layout geral.
- **styles**: Ajustes de CSS, Tailwind, etc.
- **state**: Alterações no gerenciamento de estado.
- **router**: Alterações nas rotas da aplicação.
- **form**: Alterações em formulários.

**Mobile:**

- **android**: Alterações específicas para Android.
- **ios**: Alterações específicas para iOS.
- **navigation**: Ajustes na navegação do app.
- **notifications**: Implementação ou correção de notificações push.
- **permissions**: Mudanças no gerenciamento de permissões.

**DevOps:**

- **ci**: Alterações em CI/CD.
- **docker**: Ajustes em Docker e Docker Compose.
- **k8s**: Configuração de Kubernetes.
- **terraform**: Infraestrutura como código com Terraform.

**Testes:**

- **integration**: Testes de integração.
- **e2e**: Testes de ponta a ponta (End-to-End).

---

### Criação de Branches

Ao criar uma branch, siga a estrutura abaixo:

    <número da issue>-<descrição>

Exemplo:

    9999-corrige-bug-tela12x

---

### Labels

As **labels** são usadas para categorizar e organizar as **issues** de acordo com seu tipo e prioridade. Elas são divididas em diferentes grupos:

#### Tipos de Projeto

- **mobile** – Issues relacionadas ao sistema mobile.
- **web** – Issues relacionadas ao sistema web (blog).

#### Equipes

- **back-end** – Issues relacionadas ao desenvolvimento back-end.
- **front-end** – Issues relacionadas ao desenvolvimento front-end.
- **database** – Issues relacionadas ao banco de dados (modelagem, otimizações, migrations, etc.).
- **qa** – Issues relacionadas a testes de qualidade (quality assurance).

#### GitFlow

- **feature** – Para novas funcionalidades.
- **bug** – Para correções de erros.
- **hotfix** – Correções urgentes diretamente na produção.
- **release** – Preparação para lançar uma nova versão.
- **chore** – Tarefas gerais de manutenção, ajustes de infraestrutura, etc.

#### Outras Labels Úteis

- **enhancement** – Melhoria de funcionalidades existentes.
- **documentation** – Relacionado à documentação.
- **blocked** – Issue bloqueada por algum motivo.
- **high** – Para issues de alta prioridade.
- **low** – Para issues de baixa prioridade.

---

### Raia do Kanban

O Kanban é usado para organizar as **issues** no processo de desenvolvimento. As issues são movidas entre as seguintes raias:

- **Backlog**: Issues que estão sendo especificadas e preparadas para desenvolvimento.
- **Disponível para Desenvolvimento**: Issues prontas para os desenvolvedores pegarem e começarem a trabalhar.
- **Em Processo**: Issues que estão sendo trabalhadas pelos desenvolvedores.
- **Review**: Issues concluídas e aguardando revisão antes de avançar.
- **Represado**: Issues que estão bloqueadas ou dependendo de outras tarefas para avançar.
- **Aguardando PR**: Issues concluídas, aguardando revisão e aprovação via Pull Request (PR).
- **Homologação**: Issues em testes no ambiente de homologação.
- **Disponível para Deploy**: Issues prontas para produção, após revisão e testes.

---

## Qualidade de Código

O repositório usa **Husky + lint-staged** para formatar e lintar automaticamente apenas os arquivos staged em cada commit, nos três apps:

- `apps/apae` e `apps/management-app`: Prettier (formatação) + ESLint (`--fix`).
- `apps/api`: Checkstyle (`checkstyle:check`) e PMD (`pmd:check`) via Maven — apenas **verificam**, não reformatam arquivos `.java`.

> ℹ️ O gate foi ligado sem uma reformatação retroativa de todo o repositório: arquivos antigos só serão formatados conforme forem tocados em commits futuros. `pnpm --filter apae run format:check` (e o equivalente em `management-app`) ainda vai acusar diferenças em arquivos legados até que sejam commitados novamente.

### Hooks de Pre-commit

Rodando `pnpm install` na raiz do monorepo, o script `prepare` do Husky instala o hook automaticamente (`.husky/pre-commit`, que executa `pnpm exec lint-staged`).

Se, após clonar o repositório, o hook não disparar ao commitar:

1. Confirme que rodou `pnpm install` na **raiz** (não dentro de `apps/*`).
2. Rode manualmente `pnpm exec husky` para reinstalar os hooks.
3. Verifique se `core.hooksPath` do Git aponta para `.husky` (`git config core.hooksPath` deve retornar `.husky`).

O hook pode ser burlado com `git commit --no-verify` — isso é aceitável localmente (conveniência do dev), mas por isso o CI (`backend.yml`/`frontend.yml`) também roda lint, Checkstyle e PMD; o hook local não substitui o gate do servidor.

### Formatação Manual

```bash
# apps/apae ou apps/management-app
pnpm --filter apae run format          # aplica o Prettier
pnpm --filter apae run format:check    # apenas verifica (usado no CI)

pnpm --filter management-app run format
pnpm --filter management-app run format:check
```

Ambos os apps reaproveitam o `.prettierrc`/`.prettierignore` da raiz e o binário do Prettier instalado como devDependency do workspace — não é necessário instalar o Prettier de novo em cada app.

O `.editorconfig` na raiz cobre `.ts`/`.tsx`, `.java`, `.json`, `.yml`/`.yaml` e `.md`, garantindo indentação e fim de linha consistentes entre editores/SOs.

### Checkstyle e PMD (apps/api)

```bash
cd apps/api
./mvnw checkstyle:check   # falha se houver violação de estilo (ver checkstyle.xml)
./mvnw pmd:check          # falha se houver violação de análise estática (ver pmd-ruleset.xml)
```

Os dois plugins estão presos à fase `validate`, então também rodam em qualquer `./mvnw compile|test|package`. Reformatação automática de Java (Spotless) está fora do escopo atual — Checkstyle e PMD **bloqueiam**, não corrigem.

---

## Configuração do Projeto

### Configuração de Ambiente

Este projeto automatiza a configuração inicial das variáveis de ambiente.
Ao rodar `pnpm run dev`, o script verifica a existência do arquivo `.env`.
Caso ele não exista, uma cópia será criada automaticamente a partir do `.env.example`.

**Nota:** O script nunca sobrescreverá um arquivo `.env` já existente.
Caso precise resetar as configurações, delete o `.env` manualmente, faça as modificações necessárias no .env.example e execute o comando novamente.

### Variáveis de Ambiente (Opcional)

O projeto utiliza variáveis de ambiente para configurar serviços externos, como envio de e-mails (SMTP), banco de dados e integrações.

**Importante:** A configuração dessas variáveis é opcional durante o desenvolvimento.  
O sistema funciona normalmente sem elas, porém funcionalidades como envio de e-mails não serão executadas.

---

### Como configurar

1. Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

2. Preencha:

```env
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua_senha_de_app
APP_FRONTEND_RESET_PASSWORD_URL=http://localhost:3000/apae-geral/auth/reset-password
```

3. Agora pode seguir com a execução normal do projeto, indo para seção [**Como Executar**](#como-executar)

---

### Envio de e-mails

- Sem configuração: sistema funciona normalmente, mas não envia e-mails
- Com configuração: envio de e-mails ativo

---

### Segurança

Adicionar no `.gitignore`:

```gitignore
.env
```

---

### Observações

- Cada dev pode ter seu próprio `.env`
- SMTP é opcional
- Apenas necessário para testar envio de e-mails

---

![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/IFPBEsp/APAE?utm_source=oss&utm_medium=github&utm_campaign=IFPBEsp%2FAPAE&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)
