# Guia do Desenvolvedor Back-End (Maven & Spring Boot)

Este guia apresenta os padrões de build, testes e execução do backend da APAE, utilizando **Maven** e **Spring Boot** com **Java 21**.

---

## Ferramenta de Build: Apache Maven

A API utiliza o **Apache Maven** para automação de compilação, gerenciamento de dependências e execução de testes.

O projeto inclui o **Maven Wrapper (`mvnw` e `mvnw.cmd`)** na pasta `apps/api/`, eliminando a necessidade de instalar o Maven manualmente no sistema operacional.

---

## Estrutura de Diretórios do Back-End

O backend está localizado dentro do monorepo no diretório `apps/api/`:

```bash
apps/api/
├── .mvn/                      # Configurações e binários do Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/br/org/apae/api/
│   │   │   ├── ApiApplication.java     # Classe principal (@SpringBootApplication)
│   │   │   ├── auth/                   # Autenticação e segurança JWT
│   │   │   ├── patient/                # Gestão de pacientes
│   │   │   ├── professional/           # Profissionais de saúde/apoio
│   │   │   ├── appointment/            # Agendamentos e atendimentos
│   │   │   ├── documents/              # Armazenamento e integração com MinIO
│   │   │   ├── notification/           # Notificações e alertas
│   │   │   ├── dashboard/              # Métricas e relatórios
│   │   │   └── common/                 # DTOs, validações e exceções globais
│   │   └── resources/
│   │       ├── application.yaml        # Configuração principal da aplicação
│   │       ├── application-dev.yaml    # Perfil de desenvolvimento
│   │       └── db/migration/           # Migrations do Flyway (PostgreSQL)
│   └── test/
│       └── java/br/org/apae/api/       # Testes unitários e de integração (JUnit 5, Mockito)
├── mvnw                       # Script Maven Wrapper para Linux/macOS
├── mvnw.cmd                   # Script Maven Wrapper para Windows
└── pom.xml                    # Arquivo de configuração Maven (dependências e plugins)
```

---

## Como Executar

### 1. Pré-requisitos
- **Java JDK 21** instalado (`java -version`).
- **Docker** e **Docker Compose** para inicializar banco de dados e serviços auxiliares.

### 2. Subir Serviços de Infraestrutura (PostgreSQL + MinIO)
Na raiz do monorepo, execute:
```bash
pnpm dev:infra
# ou diretamente via Docker:
docker compose up -d db minio
```

### 3. Rodar a API

#### Opção A: A partir da raiz do monorepo (via pnpm)
```bash
pnpm dev:backend
```

#### Opção B: A partir da pasta `apps/api` (via Maven Wrapper)
```bash
cd apps/api

# Linux/macOS:
./mvnw spring-boot:run

# Windows:
mvnw.cmd spring-boot:run
```

---

## Como Executar os Testes

O projeto utiliza **JUnit 5** e **Mockito**, gerenciados pelo `maven-surefire-plugin`.

### Rodar todos os testes

#### Pela raiz do monorepo:
```bash
pnpm test:api
```

#### Pelo terminal dentro de `apps/api`:
```bash
# Executa todos os testes
./mvnw test

# Executa compilando e empacotando o .jar
./mvnw clean package
```

### Rodar uma classe de teste específica
```bash
./mvnw test -Dtest=AuthServiceTest
```

### Rodar apenas um método de teste específico
```bash
./mvnw test -Dtest=AuthServiceTest#deveAutenticarComSucesso
```

---

## Comandos Maven Mais Utilizados

| Comando | Descrição |
| :--- | :--- |
| `./mvnw clean` | Limpa o diretório `target/` gerado em builds anteriores |
| `./mvnw compile` | Compila o código-fonte da aplicação |
| `./mvnw test` | Executa a suíte de testes automatizados |
| `./mvnw package` | Compila, roda testes e empacota a aplicação em um `.jar` executável |
| `./mvnw package -DskipTests` | Gera o `.jar` sem rodar a suíte de testes (útil em builds rápidos) |
| `./mvnw spring-boot:run` | Inicializa a aplicação Spring Boot em modo desenvolvimento |
| `./mvnw dependency:tree` | Exibe a árvore completa de dependências do projeto |

---

## Documentação Oficial e Referências

- **Documentação Apache Maven:** https://maven.apache.org/guides/
- **Spring Boot Maven Plugin:** https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/
- **JUnit 5 User Guide:** https://junit.org/junit5/docs/current/user-guide/
