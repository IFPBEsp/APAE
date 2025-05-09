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
  # Etapa de testes do backend com JUnit e Gradle
  test:
    name: Run Backend Tests
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Grant execute permission for Gradle wrapper
        run: chmod +x ./gradlew

      - name: Run Tests with JUnit
        run: ./gradlew test

  # Etapa de build do projeto Spring Boot
  build:
    name: Build Spring Boot Project
    runs-on: ubuntu-latest
    needs: test # Só executa se os testes passarem

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: "17"
          distribution: "temurin"

      - name: Grant execute permission for Gradle wrapper
        run: chmod +x ./gradlew

      - name: Build the project
        run: ./gradlew build
```
