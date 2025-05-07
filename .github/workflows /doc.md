# Integração Contínua com GitHub Actions

## Branches monitoradas

- main
- develop

## Funcionalidades

- Executa testes automaticamente a cada push ou PR.
- Testes do frontend e backend são executados separadamente.

## Como ajustar o CI

- Os arquivos de configuração estão em `.github/workflows/pipeline.yml`.
- Para adicionar uma nova etapa, edite esse YAML e siga os padrões dos jobs existentes.

## Validação

- A configuração foi testada pela equipe de dev e QA.
- Dúvidas? Fale com a equipe de QA.

## Pipeline.yml

```yml
# Nome do workflow principal que aparecerá na aba "Actions"
name: CI Pipeline

# Define os gatilhos para iniciar o pipeline
on:
  push:
    branches:
      - main # Executa o pipeline em pushs na branch main
      - develop # Executa o pipeline em pushs na branch develop
  pull_request:
    branches:
      - main # Executa o pipeline em PRs com destino à main
      - develop # Executa o pipeline em PRs com destino à develop

jobs:
  # Etapa de lint (análise estática de código)
  lint:
    uses: ./.github/workflows/lint.yml # Reutiliza o workflow definido no arquivo lint.yml

  # Etapa de testes do backend
  test-backend:
    uses: ./.github/workflows/test-backend.yml # Reutiliza o workflow de testes do backend
    needs: lint # Só executa se o lint passar

  # Etapa de testes do frontend
  test-frontend:
    uses: ./.github/workflows/test-frontend.yml # Reutiliza o workflow de testes do frontend
    needs: lint # Só executa se o lint passar

  # Etapa de build (geração do sistema)
  build:
    uses: ./.github/workflows/build.yml # Reutiliza o workflow de build
    needs: [test-backend, test-frontend] # Só executa se os testes de backend e frontend forem bem-sucedidos
```
