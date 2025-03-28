<p align="center">
  <img src="https://github.com/user-attachments/assets/be92f146-a67b-42bd-8d77-e4e1c02e581a" />
</p>

# APAE 

Projeto em desenvolvimento, surtido de uma parceria entre o IFPB (Campus Esperança) e a APAE.

---

## Convenção de Commits

Ao fazer um commit, siga o seguinte padrão:

    <tipo>[escopo]:<Descrição>

    <Corpo Opcional> 

Exemplo:

    Feat[service]: Adiciona login de usuário

__observações:__ Adicione o corpo do commit, somente quando achar necessário especificar algo mais importante para um melhor entendimento da alteração feita. Para adicionar quebra de linha na mensagem do commit pelo terminal, use "\n".

### Dicionário de tipos

    feat: Indica a adição de uma nova funcionalidade ou recurso no projeto.

    fix: Usado quando fazemos uma correção de bug ou problema.

    chore: Indica pequenas alterações de manutenção e ajustes.

    refactor: Indica uma refatoração de código, sem adicionar uma nova funcionalidade ou corrigir um bug.

    style: Indica alterações feitas na formatação do código, lint e outros (não altera o código em si).

    docs: Usado para indicar uma mudança na documentação (exemplo: README).

    test: Usado quando modificamos ou adicionamos testes.

    perf: Indica um commit com o intuito de melhoria de desempenho.

    ci: Usado quando há mudanças em arquivos de CI/CD (GitHub Actions, Jenkins, etc.).

    build: Indica mudanças relacionadas a build e dependências, mas não afetam o código.

    revert: Usado quando revertemos um commit anterior.

    cleanup: Usado quando removemos códigos comentados ou trechos desnecessários.

    remove: Indica uma exclusão de arquivos, diretórios ou funcionalidades obsoletas.

### Dicionário de Escopo

Backend: 

    auth: Relacionado à autenticação
    database: Mudanças no banco de dados
    api: Mudanças na API
    service: Alterações na camada de serviços
    repository: Mudanças na camada de repositório
    security: Melhorias na segurança do sistema
    cache: Implementação ou alteração de cache 

Frontend:

    ui: Alterações na interface do usuário
    componentes: Modificações em componentes reutilizáveis
    layout: Alterações no layout geral do sistema
    styles: Ajustes de CSS, Tailwind, etc.
    state: Alterações no gerenciamento de estado
    router: Alterações em rotas da aplicação
    form: Alterações em formulários

Mobile: 

    android: Alterações específicas para Android
    ios: Alterações específicas para iOS
    navigation: Ajustes na navegação do app
    notifications: Implementação ou correção de notificações push
    permissions: Mudanças no gerenciamento de permissões

DevOps:

    ci: Alterações em CI/CD
    docker: Ajustes em Docker e Docker Compose
    k8s: Configuração de Kubernetes
    terraform: Infraestrutura como código com Terraform

Testes: 

    integration: Testes de integração
    e2e: Testes de ponta a ponta (End-to-End)

## Criação de Branchs

Para criar branchs, siga a seguinte estrutura:

    <Número da Issue>-<Descrição>

Exemplo: 

    9999-corrige-bug-tela12x
    
    
