# Estratégia de Renovate

## Objetivo

Realizar um estudo e uma POC controlada para avaliar a adoção do Renovate no repositório, sem aplicar uma configuração definitiva nesta etapa.

Atualmente, não foi identificada configuração do Renovate nem ferramenta de SCA no repositório. Dessa forma, não existe automação específica para acompanhar atualizações de dependências e vulnerabilidades.

A adoção definitiva do Renovate deverá ocorrer somente após a avaliação da POC, discussão com o time e aprovação do Quality Owner.

## Como o Renovate funciona

O Renovate analisa os arquivos de dependências do repositório, identifica versões disponíveis para atualização e pode criar Pull Requests automaticamente para propor essas atualizações.

Para este projeto, os principais ecossistemas identificados são:

* Maven;
* pnpm/npm;
* GitHub Actions.

O Renovate possui managers específicos para cada ecossistema e pode aplicar regras para limitar e agrupar os Pull Requests gerados.

## Ecossistemas do repositório

### Maven

O backend utiliza Maven através do arquivo:

```text
apps/api/pom.xml
```

O arquivo contém as dependências e plugins utilizados pelo backend.

A POC deve verificar a descoberta e atualização dessas dependências através do manager Maven.

### pnpm/npm

O projeto utiliza pnpm `10.10.0`.

A estrutura do workspace é definida em `pnpm-workspace.yaml`:

```yaml
packages:
  - 'apps/*'
  - 'packages/*'
```

Foram identificados manifests na raiz e nos aplicativos:

```text
package.json
apps/apae/package.json
apps/management-app/package.json
```

Também existe o lockfile:

```text
pnpm-lock.yaml
```

A POC deve verificar se o Renovate identifica corretamente o `package.json` da raiz e os manifests localizados em `apps/*`, considerando a estrutura atual do workspace.

Não deve ser realizada reorganização dos manifests para adequação ao Renovate.

### GitHub Actions

Foram identificados os workflows:

```text
.github/workflows/backend.yml
.github/workflows/frontend.yml
```

Existe atualmente uma diferença entre as versões utilizadas:

No backend:

```yaml
uses: actions/checkout@v5
```

No frontend:

```yaml
uses: actions/checkout@v4
```

A POC deve verificar a descoberta dessas dependências pelo Renovate, sem realizar alterações manuais nos workflows.

## Frequência

Foram consideradas duas opções: execução diária e semanal.

### Diária

**Vantagens:**

* identifica atualizações mais rapidamente;
* reduz o tempo entre o lançamento e a atualização;
* é adequada para acompanhar atualizações de segurança.

**Desvantagens:**

* pode gerar maior quantidade de Pull Requests;
* aumenta a quantidade de notificações;
* pode gerar maior interação da equipe com atualizações pequenas.

### Semanal

**Vantagens:**

* reduz a quantidade de notificações;
* concentra as atualizações;
* facilita a revisão periódica das dependências.

**Desvantagens:**

* aumenta o intervalo para identificação das atualizações;
* pode acumular mais atualizações em um mesmo período.

### Recomendação

A recomendação inicial é utilizar **frequência semanal**, buscando controlar o volume de Pull Requests e de notificações.

A frequência poderá ser reavaliada após os resultados da POC.

## Volume de PRs

Para controlar o número de Pull Requests simultâneos, recomenda-se iniciar com:

```json
"prConcurrentLimit": 3
```

O limite poderá ser ajustado após observar o volume real produzido durante a POC.

Para reduzir o ruído, recomenda-se agrupar atualizações `patch` e `minor` das dependências de desenvolvimento do pnpm:

```json
{
  "packageRules": [
    {
      "matchManagers": ["pnpm"],
      "matchDepTypes": ["devDependencies"],
      "matchUpdateTypes": ["patch", "minor"],
      "groupName": "devDependencies patch/minor"
    }
  ]
}
```

Atualizações `major` devem permanecer separadas inicialmente para permitir uma revisão individual.

## Convenção de commits

Não foi identificada uma configuração de commitlint nos arquivos analisados.

Por esse motivo, não deve ser realizada alteração na configuração de commitlint nesta etapa.

O Renovate possui a opção `commitMessagePrefix`, que poderá ser utilizada posteriormente para adequar as mensagens geradas à convenção de commits adotada pelo projeto.

A recomendação é não excluir os commits do Renovate das validações do commitlint. Caso exista uma convenção baseada em Conventional Commits, a configuração do Renovate deverá ser compatível com ela.

A definição definitiva do `commitMessagePrefix` deverá ocorrer somente após a confirmação da convenção utilizada pelo projeto.

## Processo de revisão

A revisão dos Pull Requests gerados pelo Renovate deve ser tratada como responsabilidade do **Quality Owner**, e não como responsabilidade fixa de uma pessoa específica.

O papel deve ser documentado como:

> Quality Owner: responsável por acompanhar e garantir a revisão dos Pull Requests de atualização de dependências e vulnerabilidades gerados pelo Renovate.

Dessa forma, a responsabilidade permanece válida mesmo com mudanças na composição da equipe entre os semestres.

## `spring-boot-devtools`

Foi identificada em `apps/api/pom.xml` a dependência:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

A dependência merece atenção por estar declarada com `runtime`, apesar de estar marcada como `optional`.

Nesta etapa, não será realizada alteração no `pom.xml`.

O achado deve ser registrado como uma observação para avaliação posterior sobre a necessidade do `spring-boot-devtools` no ambiente de produção.

Caso seja necessária uma alteração, ela deverá ser tratada separadamente da adoção do Renovate.

## Configuração experimental da POC

A configuração abaixo é exclusivamente experimental e não deve ser aplicada como configuração definitiva do repositório:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",

  "enabledManagers": [
    "maven",
    "pnpm",
    "github-actions"
  ],

  "prConcurrentLimit": 3,

  "packageRules": [
    {
      "matchManagers": ["pnpm"],
      "matchDepTypes": ["devDependencies"],
      "matchUpdateTypes": ["patch", "minor"],
      "groupName": "devDependencies patch/minor"
    }
  ]

  /*
   * Avaliar posteriormente:
   *
   * "schedule": ["* * * * 1"]
   *
   * para execução semanal.
   *
   * Avaliar também:
   *
   * "commitMessagePrefix": "chore(deps):"
   *
   * após confirmação da convenção de commits do projeto.
   */
}
```

A configuração experimental deve ser utilizada somente para validar o comportamento do Renovate.

## POC

A POC deve validar a descoberta e geração de Pull Requests para os seguintes ecossistemas:

* Maven em `apps/api/pom.xml`;
* pnpm na raiz;
* pnpm em `apps/apae`;
* pnpm em `apps/management-app`;
* GitHub Actions em `.github/workflows`.

Também devem ser observados:

* quantidade de PRs gerados;
* funcionamento do `prConcurrentLimit`;
* comportamento do agrupamento de `devDependencies`;
* execução dos workflows existentes sobre os PRs gerados;
* eventuais ajustes necessários na configuração experimental.

O resultado da POC deve ser registrado no PR da issue, incluindo a configuração utilizada, os PRs gerados, os resultados observados e os ajustes realizados.

A configuração definitiva do Renovate não deve ser criada ou aplicada nesta etapa.

## Vulnerabilidades

A ausência de Renovate e de uma ferramenta de SCA impede atualmente a automação solicitada para acompanhamento das dependências e a verificação contínua de vulnerabilidades.

O Renovate deve ser considerado como mecanismo de automação de atualização de dependências. A validação específica de vulnerabilidades HIGH/CRITICAL deve continuar dependendo de uma ferramenta de análise de vulnerabilidades adequada.

Nesta issue, não deve ser adicionada uma solução SCA diferente da solicitada.

## Próximos passos

Após a execução da POC:

1. registrar os resultados no PR;
2. avaliar o volume de Pull Requests;
3. avaliar os agrupamentos;
4. verificar o comportamento dos workflows;
5. discutir os resultados com o time;
6. obter a aprovação do Quality Owner;
7. somente então avaliar a criação da configuração definitiva do Renovate.
