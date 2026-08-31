## GRADLE

Essa será nossa ferramenta de automação de build e teste, vamos usar ele para gerenciar nossos projetos e suas dependências.

> Vamos utilizar a versão **8.14**, é a última versão disponível até o momento.

## EXEMPLO DA ÁRVORE DE PACOTES

```bash
├── api
│   ├── buildSrc #--->*COMPONENTE DE LÓGICA DE BUILD (DEFINE CONVENÇÕES)*
│   │   ├── build.gradle.kts
│   │   ├── settings.gradle.kts
│   │   └── src
│   │       └── main
│   │           └── kotlin
│   │               ├── buildlogic.java-application-conventions.gradle.kts
│   │               ├── buildlogic.java-common-conventions.gradle.kts
│   │               └── buildlogic.java-library-conventions.gradle.kts
│   ├── demo  #--->*EXEMPLO DE API (MICROSERVIÇO)*
│   │   ├── build.gradle.kts #--->*ARAQUIVO DE BUILD (CONFIGURA DEPENDÊNCIAS)*
│   │   └── src
│   │       ├── main
│   │       │   ├── java
│   │       │   │   └── br
│   │       │   │       └── edu
│   │       │   │           └── ifpb
│   │       │   │               └── esp
│   │       │   │                   └── demo
│   │       │   │                       └── DemoApplication.java
│   │       │   └── resources
│   │       │       └── application.properties
│   │       └── test
│   │           └── java
│   │               └── br
│   │                   └── edu
│   │                       └── ifpb
│   │                           └── esp
│   │                               └── demo
│   │                                   └── DemoApplicationTests.java
│   ├── gradle  #--->*COMPONENTE GRADLE (DEFINE PROPRIEDADES BASE)*
│   │   ├── libs.versions.toml
│   │   └── wrapper
│   │       ├── gradle-wrapper.jar
│   │       └── gradle-wrapper.properties
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   └── settings.gradle.kts #--->*COMPONENTE GRADLE (DEFINE RAÍZ DO PROJETO E INCLUI SUBPROJETOS)*
```

---

![componentes-back](./images/draw-backend.svg)

> Componentes back-end.

---

## COMO INSTALAR

#### Podemos utilizar o gradle interno do projeto com o comando terminal:

```sh
./gradlew <task>
```

> Assim como podemos fazer isso via IDE, através da interface (seja no vscode ou intellij).

#### Caso queira instalar no seu terminal, siga os passos:

1. baixe a versão desejada (vamos seguir com a versão 8.14)

```sh
wget https://services.gradle.org/distributions/gradle-8.14-bin.zip -P /tmp
```

2. crie a pasta para a ferramenta

```sh
sudo mkdir /opt/gradle
```

3. exporte a instalação

```sh
sudo unzip -d /opt/gradle /tmp/gradle-8.7-bin.zip
```

4. adicione essa linha no seu arquivo `.bashrc`

```sh
echo 'export PATH=$PATH:/opt/gradle/gradle-8.14/bin' >> ~/.bashrc
source ~/.bashrc
```

---

## COMO USAR

Agora podemos partir para alguns comandos do gradle.

### ALGUNS COMANDOS BÁSICOS

- Para compilar o projeto

```bash
./gradlew build
```

- Para executar todos os testes

```bash
./gradlew test
```

- Para listar todas as tasks

```bash
./gradlew tasks
```

- Para parar o ./gradlew

```bash
./gradlew --stop
```

Podemos especificar qual sub-projeto que queremos executar e testar:

- Para buildar o sub-projeto

```bash
./gradlew :demo:build
```

- Para rodar o sub-projeto

```bash
./gradlew :demo:bootRun #no caso estamos usando o spring boot
```

- Para testar o sub-projeto

```bash
./gradlew :demo:test
```

- Para listar as dependências do sub-projeto

```bash
./gradlew :demo:dependencies
```

- Para listar todas as tasks de um sub-projeto

```bash
./gradlew :demo:tasks
```

## **PARA MAIS INFORMAÇÕES ACESSE A DOCUMENTAÇÃO OFICIAL DA TECNOLOGIA:**

Documentação gradle: https://docs.gradle.org/current/userguide/userguide.html
