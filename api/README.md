<p align="center">
  <img src="https://github.com/user-attachments/assets/be92f146-a67b-42bd-8d77-e4e1c02e581a" />
</p>

# APAE

Projeto em desenvolvimento, fruto de uma parceria entre o IFPB (Campus Esperança) e a APAE.

---

## Índice

1. [Introdução](#introdução)
2. [Fluxo de Trabalho](#fluxo-de-trabalho)
   - [Convenção de Commits](#convenção-de-commits)
   - [Criação de Branches](#criação-de-branches)
   - [Labels](#labels)
   - [Raia do Kanban](#raia-do-kanban)
3. [Configuração do Projeto](#configuração-do-projeto)

---

## Introdução

Este projeto tem como objetivo o desenvolvimento de dois sistemas para a APAE, o primeiro focado no gerenciamento de pacientes e o outros na exibição de informações. O projeto está sendo desenvolvido em colaboração com o IFPB (Campus Esperança).

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

Obs. Ainda em desenvolvimento...
(Incluir as instruções de como configurar o ambiente de desenvolvimento, instalar dependências, rodar o projeto, etc.)

---
