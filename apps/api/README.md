# 🚀 Backend da Aplicação APAE (API)

Seja bem-vindo ao guia de configuração do backend da APAE. Este documento serve como um tutorial completo para configurar e executar o projeto no seu ambiente de desenvolvimento local.

A API é construída com **Java** e **Spring Boot**, responsável por todas as regras de negócio, gerenciamento do banco de dados até comunicação com o frontend.

## 🛠️ Pré-requisitos Essenciais

Antes de escrever qualquer linha de código, seu ambiente precisa estar preparado. Por favor, instale as ferramentas abaixo.

| Ferramenta | Descrição | Como Instalar / Verificar                                                                                                                                                                                                                                                                                                                                                                                                           |
| :--- | :--- |:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Java (JDK)** | A linguagem de programação base do projeto. | **Versão 21 ou superior.** <br/> 1. Verifique se já tem: `java -version` <br/> 2. [Link para instalar](https://www.oracle.com/java/technologies/downloads/)                                                                                                                                                                                                                                                                         |
| **Git** | Sistema de controle de versão para baixar o código. | 1. Verifique se já tem: `git --version` <br/> 2. [Link para instalar](https://git-scm.com/downloads)                                                                                                                                                                                                                                                                                                                                |
| **Docker** | Plataforma para rodar nosso banco de dados em um contêiner. | 1. O **Docker Desktop** (Windows/Mac) já inclui o `docker compose`. <br/> 2. [Link para instalar (Windows)](https://docs.docker.com/desktop/install/windows-install/) <br/> 3. [Link para instalar (Mac)](https://docs.docker.com/desktop/install/mac-install/) <br/> 4. [Link para instalar (Linux)](https://docs.docker.com/engine/install/ubuntu/) e ([docker-compose separado](https://docs.docker.com/compose/install/linux/)) |
| **IDE** | Um editor de código para facilitar o desenvolvimento. | **Altamente recomendado:** <br/> 1. [IntelliJ IDEA Community (Gratuito)](https://www.jetbrains.com/idea/download/) <br/> 2. [Visual Studio Code (com Java Extension Pack)](https://code.visualstudio.com/docs/java/java-tutorial)                                                                                                                                                                                                   |

---

## ⚙️ Configuração do Projeto (Passo a Passo)

Siga estes passos **na ordem exata** para evitar problemas.

### 1\. Navegar até a Pasta Correta

O repositório `APAE` contém vários projetos (é um "monorepo"). O backend Java está na pasta `apps/api`.

```bash
# Pelo terminal: entre na pasta que você acabou de clonar
cd APAE

# Agora, entre na pasta da API
cd apps/api
```

### 2\. Configurar o Banco de Dados (Docker)

Nossa API precisa de um banco de dados para funcionar. Nós usamos o Docker para "simular" esse banco em sua máquina sem precisar instalá-lo manualmente.

O arquivo `docker-compose.yml` é a receita que diz ao Docker como criar esse banco.

**Abra um terminal (em qualquer lugar dentro da pasta `APAE`) e execute:**

```bash
# Este comando vai baixar a imagem do banco (ex: Postgres/MySQL)
# e o iniciará em segundo plano (-d = "detached").
docker compose up -d
```

**Para verificar se funcionou:**

```bash
# Lista todos os contêineres em execução
docker ps
```

Você deve ver alguns contêineres com os seguintes nomes: `minio_docs_apae`, `apae-postgres`, `api-db-1`.

-----

## ▶️ Sobre a Execução da Aplicação

Com o banco rodando e tudo configurado, você está pronto para iniciar a API.

### Método 1: Via Terminal (Recomendado para Padronização)

Nós usamos o "Maven Wrapper" (`mvnw`), que é um script que garante que todos os desenvolvedores usem a mesma versão do Maven, evitando conflitos.

```bash
# Se você estiver no Linux ou macOS, pode ser necessário dar permissão de execução:
# chmod +x mvnw
```

O terminal mostrará um log grande. Se tudo der certo, a última linha será algo como:
`Started ApiApplication in X.XXX seconds (JVM running for Y.YYY)`

### Método 2: Via IDE (IntelliJ / VSCode)

Se você prefere usar a interface gráfica da sua IDE:

1.  **Importante:** Abra a pasta `apps/api` diretamente na sua IDE (Não abra a pasta `APAE` principal, pois a IDE pode se confundir).
2.  A IDE deve reconhecer automaticamente que é um projeto Maven e começar a baixar as dependências (pode levar alguns minutos na primeira vez).
3.  Encontre a classe principal da aplicação, que geralmente fica em:
    `src/main/java/br/org/apae/api/ApiApplication.java`
4.  Clique no ícone de "Play" (▶) ao lado do nome da classe ou dentro do arquivo para iniciar a aplicação.

-----

## 📚 Verificando se Tudo Funcionou (Swagger)

Após a aplicação iniciar, a forma mais fácil de verificar se os endpoints estão no ar é acessando a documentação do **Swagger**.

Abra seu navegador e acesse:

[http://localhost:8090/api/swagger-ui/index.html](http://localhost:8090/api/swagger-ui/index.html)

Você deverá ver a interface do Swagger listando todos os "Controllers" (como Patient, Auth, Appointment, etc.) e seus respectivos endpoints.

-----

## 🤯 Solução de Problemas Comuns (Troubleshooting)

**Problema:** O comando `docker compose up -d` falha com um erro `port is already allocated` (ou "porta já em uso").
**Solução:** Outro programa em sua máquina (talvez outra instância de um banco de dados) está usando a porta (ex: `5432`). Você tem duas opções:
1\.  Pare o outro programa.
2\.  Altere a porta no `docker-compose.yml` (ex: de `"5432:5432"` para `"5433:5432"`) e atualize o `application.properties` (`localhost:5432` para `localhost:5433`).

**Problema:** O comando `./mvnw spring-boot:run` falha com `Permission denied` (no Linux/Mac).
**Solução:** O script não tem permissão de execução. Rode `chmod +x mvnw` e tente novamente.

**Problema:** A aplicação inicia, mas falha com erros de `Connection refused` (Conexão recusada) no log.
**Solução:** A API não conseguiu se conectar ao banco de dados. Verifique:
1\.  O Docker está rodando? Rode `docker ps` para confirmar que o contêiner do banco está `Up`.

**Problema:** A IDE (IntelliJ/VSCode) mostra erros em todos os arquivos e não encontra as classes (como `RestController`, `Autowired`, etc).
**Solução:** A IDE não importou o projeto como um projeto Maven.
1\.  Feche o projeto.
2\.  Selecione "Open" ou "Import Project".
3\.  Selecione a pasta `apps/api` (e não a `APAE`).
4\.  Se a IDE perguntar, confirme que você quer abrir como um projeto "Maven".

---


