# Execução Local do SonarQube

Este documento fornece as instruções para configurar e executar a análise estática de código com o SonarQube localmente, abrangendo tanto o frontend quanto o backend do repositório Geral da APAE.

## 1. Pré-requisitos

Para rodar a análise, você precisará ter instalados:

* **Docker** (e Docker Compose)
* **JDK 21**
* **Maven** (`sudo apt install maven` no Linux/Ubuntu)

**Nota sobre o Frontend:** O scanner do frontend roda embutido em um container Docker, portanto **não é necessário ter o Node instalado** na sua máquina para esta etapa.

**Configuração do Sistema Operacional (Elasticsearch):**

O SonarQube utiliza o Elasticsearch por baixo dos panos, que exige um limite de áreas de memória virtual mapeadas de pelo menos `262144`.

Verifique o valor atual na sua máquina com o comando:

```
cat /proc/sys/vm/max_map_count
```

Caso o valor retornado seja menor que `262144`, você precisará aumentá-lo (em distribuições Linux, temporariamente com `sudo sysctl -w vm.max_map_count=262144` ou permanentemente editando o arquivo `/etc/sysctl.conf`). A maioria das distribuições já vem com um valor acima disso por padrão.

## 2. Subir o servidor

Para iniciar o SonarQube localmente, utilize o arquivo de composição do repositório executando o comando abaixo a partir da raiz:

```
docker compose -f docker-compose.sonar.yml up
```

*(Rodar sem a flag `-d` fará com que você veja os logs em tempo real).*

> ⚠️ **Aviso sobre containers órfãos:** Durante a inicialização, o Docker pode exibir o alerta `Found orphan containers ([minio_docs_apae apae-postgres])` e sugerir a flag `--remove-orphans`. **Não utilize essa flag**, pois ela derrubaria os containers de banco de dados e armazenamento do compose principal do projeto.

**Aguardando o servidor:** No log, a mensagem `Web Server is operational` aparecerá primeiro, mas você deve aguardar até que a mensagem final **`SonarQube is operational`** seja exibida.

Como o servidor ficará rodando e ocupando este terminal, **abra uma nova aba ou janela de terminal** para executar os passos seguintes.

O painel do SonarQube estará disponível no endereço: <http://localhost:9500>

**Por que a porta não é a 9000 (padrão)?**

Neste repositório principal (APAE Geral), a porta 9000 já está em uso pelo serviço do **MinIO**. A porta 9500 foi escolhida como desvio para que o desenvolvedor possa ter os três servidores (Geral, Atendimento e Gestão Escolar) de pé ao mesmo tempo, além do MinIO, sem conflitos de porta.

## 3. Gerar o token

Para que os scanners consigam enviar os relatórios de código para o servidor, você precisa realizar o primeiro acesso e gerar um token:

### Primeiro login e troca de senha

1. Acesse <http://localhost:9500> e faça login com o usuário e senha padrão: `admin` / `admin`.
2. O SonarQube exigirá **obrigatoriamente a troca de senha** no primeiro acesso:
   * No campo **Old Password**, digite `admin`.
   * Defina uma nova senha que tenha **no mínimo 12 caracteres** e seja diferente de `admin`.
   *(Nota: Caso você execute o comando de limpeza com perda de volumes `down -v` futuramente, a senha voltará ao padrão `admin/admin`)*.

### Gerando o token

1. Após logar com a nova senha, clique no ícone do seu perfil no canto superior direito e vá em **My Account** > **Security** > **Generate Tokens**.
2. Preencha os campos da seguinte forma:
   * **Name:** Dê um nome de sua escolha (ex: `local-token`).
   * **Type:** O campo vem em branco por padrão. Selecione obrigatoriamente **User Token** (ou "Token de usuário" / "Token de análise global").
   * **Expires in:** Pode manter o padrão sugerido de 30 dias (ou escolher conforme preferir).
3. Clique em **Generate** e copie o token gerado.

> ⚠️ **Não use o *Project Analysis Token*.** Esse tipo de token só funciona para projetos que já existem no painel. Como esta será a primeira vez que a análise rodará, os projetos ainda não existem e o scanner falhará ao tentar criá-los.

Após gerar o token, exporte-o para a variável de ambiente `SONAR_TOKEN` no seu terminal (na nova aba que você abriu):

```
export SONAR_TOKEN="cole_seu_token_aqui"
```

> ⚠️ **Atenção:** O token deve ir para a variável de ambiente `SONAR_TOKEN` e **nunca** para dentro de um arquivo versionado no repositório.

## 4. Analisar o backend

Como o código do backend Java está contido no módulo `apps/api`, navegue até o diretório correspondente:

```
cd apps/api
```

Com o token na variável de ambiente e o servidor rodando, execute a análise com o comando Maven completo:

```
mvn clean verify sonar:sonar
```

Após o término da execução com sucesso (`BUILD SUCCESS`), retorne para a raiz do repositório:

```
cd ../..
```

**Por que o `verify` é necessário?**

A fase `verify` no Maven garante a compilação e a execução dos testes. Sem essa fase, a pasta `target/classes` não é criada, e sem os arquivos `.class` compilados, a análise de Java não acontece. O comando `sonar:sonar` sozinho não serve para analisar o projeto.

## 5. Analisar o frontend

Certifique-se de que está na raiz do projeto (`APAE/`) e execute o script preparado:

```
./.scripts/sonar-scan-frontend.sh
```

*(Certifique-se de que a variável `SONAR_TOKEN` continua exportada no ambiente desse terminal).*

**O que o script faz por baixo?**

Ele roda o scanner em container. O script baixa e executa a imagem Docker do `sonar-scanner-cli`, montando o diretório do módulo atual para dentro do container e enviando os dados para a porta 9500 sem exigir Node instalado ou configurações locais complexas na sua máquina.

## 6. Ler o painel

Após os dois comandos finalizarem com sucesso, volte ao navegador em <http://localhost:9500> e clique na aba **Projects** no menu superior (ou acesse diretamente <http://localhost:9500/projects>).

Você verá as duas chaves de projeto criadas listadas em cards:

* `apae-geral-backend`
* `apae-geral-frontend`

Ao clicar em qualquer um deles, você verá as abas de navegação:

* A aba principal exibe o **Quality Gate**, onde fica o número que interessa em cada uma das métricas (Bugs, Vulnerabilidades, Code Smells, Cobertura, etc).
* A aba **Issues** lista os detalhes específicos de cada problema encontrado no código analisado.

## 7. Cobertura

Atualmente, é esperado que a cobertura de código apareça baixa ou zerada:

* **Backend (`apae-geral-backend`):** O relatório de cobertura (`jacoco.xml`) é gerado durante a fase `verify` do Maven. A porcentagem reportada reflete o volume de testes unitários atualmente implementados no backend.
* **Frontend (`apae-geral-frontend`):** O arquivo de configuração `sonar-project.properties` do módulo espera o relatório de testes no caminho `coverage/lcov.info`. Como os runners de testes do frontend ainda não estão configurados para gerar esse arquivo durante o fluxo local, a cobertura aparece como 0.0%.

Isso deve ser lido como uma etapa futura de implementação das suítes de testes nos repositórios, e **não como um erro de configuração** do seu ambiente local do SonarQube.

## 8. Encerrar e limpar

Quando finalizar seu trabalho e quiser derrubar o container, você tem duas opções:

Para derrubar o container mantendo o histórico de análises salvo para a próxima vez:

```
docker compose -f docker-compose.sonar.yml down
```

Para derrubar e **apagar os volumes e o histórico**:

```
docker compose -f docker-compose.sonar.yml down -v
```

> ⚠️ O segundo comando (com o `-v`) apaga os volumes do banco de dados e Elasticsearch. Isso apagará todo o histórico de análises e **resetará a senha do admin de volta para o padrão `admin/admin`**. Use apenas se precisar limpar configurações corrompidas e recomeçar do zero.
