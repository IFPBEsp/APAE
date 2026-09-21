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

#### Pré-requisitos

- Docker com Docker Compose;
- Java 21;
- Node.js e pnpm.

#### Passo 1: Setup inicial

Instale todas as dependências do projeto:

```bash
cd APAE
pnpm install
cp .env.example .env
```

O arquivo `.env.example` já contém valores adequados ao desenvolvimento local. Não use
credenciais de produção nesse arquivo.

#### Passo 2: Preparar os serviços locais

Suba PostgreSQL e MinIO, aplique as migrations Flyway V1–V11 e insira os dados
fictícios de desenvolvimento:

```bash
pnpm db:prepare
```

O PostgreSQL local cria somente o schema `apae_geral`, que é o schema de propriedade
deste produto. O desenvolvimento do APAE-Geral não depende dos schemas
`atendimento` ou `gestao_escolar`, nem do banco compartilhado no Neon.

#### Passo 3: Executar o projeto

Inicie backend e frontend no host. A infraestrutura continuará nos containers:

```bash
pnpm dev
```

Aguarde até que os serviços estejam prontos. O frontend estará acessível em
`http://localhost:3000/apae-geral` e a API em
`http://localhost:8090/apae-geral/api`.

---

#### Outros Comandos Úteis:

- `pnpm dev:infra`: Sobe PostgreSQL e MinIO e aplica as migrations.
- `pnpm dev:backend`: Executa apenas o backend (API).
- `pnpm dev:apae`: Executa apenas o frontend APAE-Geral.
- `pnpm db:migrate`: Aplica as migrations pendentes no PostgreSQL local.
- `pnpm db:seed`: Aplica as migrations e insere o seed fictício e idempotente.
- `pnpm db:prepare`: Prepara banco, seed e MinIO para o desenvolvimento.
- `pnpm docker:up`: Sobe todos os serviços definidos no Compose.
- `pnpm docker:down`: Para os containers e os remove da memória.
- `pnpm docker:drop`: Para os containers e apaga os volumes locais, incluindo os dados do banco.

#### Credenciais do usuário para testes:

- Email: `admin@teste.local`
- CPF: `000.000.001-91`
- Senha: `123456`

Todos os registros criados por esse seed são fictícios e destinados exclusivamente
ao desenvolvimento. O comando pode ser repetido sem duplicá-los.

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
