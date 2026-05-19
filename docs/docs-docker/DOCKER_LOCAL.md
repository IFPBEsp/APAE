# Rodando o APAE Geral com Docker (local)

Este guia mostra como **buildar, rodar e validar** as imagens do APAE Geral
(frontend Next.js + backend Spring Boot) na sua máquina, antes de mergear na
`dev` ou consumir as imagens publicadas no GHCR.

> O `docker-compose.yml` da raiz já sobe Postgres, MinIO, backend e frontend.
> O que muda é **de onde as imagens vêm**: build local ou pull do GHCR.

---

## TL;DR — passo a passo do caminho feliz

Se você só quer rodar tudo localmente e validar que está funcionando, siga
estes 7 passos **na ordem**:

### 1. Confirme pré-requisitos
```bash
docker --version           # >= 24
docker compose version     # v2 (sem hífen)
```
Docker Desktop (Mac/Windows) ou Docker Engine + Compose plugin (Linux).

### 2. Prepare o `.env`
```bash
cp .env.example .env
```
Não precisa alterar nada para rodar local — os valores padrão funcionam.
Se for personalizar, atenção principalmente em:
- `POSTGRES_PORT` (padrão `5200`) — a porta no host
- `POSTGRES_USER` / `POSTGRES_PASSWORD` — devem bater com `DB_USERNAME` /
  `DB_PASSWORD` (já batem por padrão)
- `JWT_SECRET` — qualquer string base64 serve para dev

### 3. (Opcional) Buildar as imagens localmente
Se você quer testar as suas mudanças nos Dockerfiles **antes** de publicar no
GHCR, builde local primeiro:
```bash
docker build -t apae-geral-backend:local  ./apps/api
docker build -t apae-geral-frontend:local ./apps/apae
```
Senão, pule esse passo — o compose puxa as imagens do GHCR automaticamente.

### 4. Suba toda a stack
**Usando imagens do GHCR (padrão):**
```bash
docker compose pull        # baixa as últimas imagens publicadas
docker compose up -d       # sobe db, minio, backend, frontend
```

**Usando suas imagens locais do passo 3** — descomente as 3 linhas no seu
`.env` (estão pré-preenchidas em `.env.example` como referência):
```bash
BACKEND_IMAGE=apae-geral-backend
FRONTEND_IMAGE=apae-geral-frontend
IMAGE_TAG=local
```
E então:
```bash
docker compose up -d
```
> Sem essas variáveis, o compose tenta puxar do GHCR e falha com
> `denied: denied` se o pacote estiver privado. Comente as linhas de volta
> quando quiser puxar do GHCR de novo.

### 5. Confira que tudo subiu
```bash
docker compose ps
```
Esperado: 4 serviços com `STATUS = running` (ou `healthy`):

```
NAME                  STATUS
apae-postgres         Up (healthy)
minio_docs_apae       Up
apae-geral-backend    Up
apae-geral-frontend   Up
```

> O backend leva **30–60s** para ficar healthy (Spring Boot + Hibernate
> precisam construir o schema). Acompanhe com `docker compose logs -f
> apae-geral-backend` — espere ver `Started ApiApplication in X.X seconds`.

### 6. Valide os endpoints

> ℹ️ Tanto o backend (Spring `server.servlet.context-path`) quanto o frontend
> (Next.js `basePath`) rodam sob o prefixo `/apae-geral`. A raiz `/` retorna
> 404 — isso é esperado.

```bash
# Backend deve responder UP
curl -s http://localhost:8080/apae-geral/actuator/health
# {"status":"UP"}

# Frontend deve devolver 200 no caminho com prefixo
curl -sI http://localhost:3000/apae-geral/ | head -1
# HTTP/1.1 200 OK
```

E no navegador:
- Frontend: <http://localhost:3000/apae-geral>
- Swagger backend: <http://localhost:8080/apae-geral/api/swagger-ui/index.html>
- MinIO console: <http://localhost:9001> (login com `MINIO_ROOT_USER` /
  `MINIO_ROOT_PASSWORD` do `.env`)

### 7. Para derrubar
```bash
docker compose down            # mantém dados de Postgres/MinIO
docker compose down -v         # ZERA Postgres e MinIO (cuidado)
```

---

## 1. Pré-requisitos

- Docker 24+ e Docker Compose v2 (`docker compose`, sem hífen).
- Arquivo `.env` na raiz, baseado em `.env.example` (passo 2 do TL;DR).
- (Opcional) Login no GHCR se o pacote estiver privado:
  ```bash
  echo $GH_TOKEN | docker login ghcr.io -u SEU_USUARIO --password-stdin
  ```
  O token só precisa do scope `read:packages`.

---

## 2. Variáveis de ambiente — runtime vs. build-time

Existem **dois tempos** distintos onde variáveis entram em jogo. Saber isso
evita horas de debug:

### Runtime (lidas quando o container sobe)
Todas as variáveis do **backend** (`DB_URL`, `JWT_SECRET`, `MINIO_*`, `MAIL_*`,
`CORS_ALLOWED_ORIGINS`, etc) e `NODE_ENV` do frontend são runtime. Ficam no
`.env` da máquina/VM e o `docker compose` injeta a cada `up`. Mudou? `restart`
resolve.

### Build-time (cravadas dentro da imagem)
Variáveis `NEXT_PUBLIC_*` do Next.js são **inlinadas no bundle JavaScript**
durante `npm run build`. O valor é congelado dentro da imagem Docker no
momento em que o `docker build` roda. **Trocar a env depois não tem efeito** —
você precisa buildar a imagem de novo.

Por isso o Dockerfile do frontend define:
```dockerfile
ARG NEXT_PUBLIC_API_URL=/apae-geral/api
```
e o workflow do GHCR builda com `/apae-geral/api`. A premissa é que em
produção haverá um reverse proxy (Nginx/Traefik no Portal dos 30 anos) que faz
`/apae-geral/*` → containers do APAE (backend e frontend, conforme a rota).
Assim **uma única imagem serve todos os ambientes**.

Se quiser rodar local **sem proxy**, builde o frontend passando a URL absoluta
(veja seção 4.1).

---

## 3. Subindo a stack a partir das imagens do GHCR

Esse é o fluxo que reproduz produção (já coberto no TL;DR, recapitulado aqui):

```bash
docker compose pull
docker compose up -d
docker compose logs -f apae-geral-backend apae-geral-frontend
```

Para mirar uma tag específica (ex.: build de um SHA do CI):
```bash
IMAGE_TAG=sha-abc1234 docker compose up -d
```

---

## 4. Buildando localmente (sem usar o GHCR)

Útil para validar mudanças no Dockerfile antes de abrir PR.

### 4.1 Frontend isolado (sem proxy na frente)
```bash
docker build \
  --build-arg NEXT_PUBLIC_API_URL=http://localhost:8080/apae-geral/api \
  -t apae-geral-frontend:local \
  ./apps/apae

docker run --rm -p 3000:3000 apae-geral-frontend:local
```
> Note o `NEXT_PUBLIC_API_URL` absoluto — no modo isolado não há proxy.
> O frontend continuará servindo sob `/apae-geral` (basePath do Next.js), então
> acesse em <http://localhost:3000/apae-geral>.

### 4.2 Backend isolado
**Pré-requisito:** Postgres precisa estar rodando antes (`docker compose up -d
db`). Senão você cai no erro `Connection to host.docker.internal:5200 refused`.

```bash
docker build -t apae-geral-backend:local ./apps/api

# sobe só o Postgres do compose primeiro
docker compose up -d db

# depois roda o backend isolado conectando nele
docker run --rm -p 8080:8080 \
  --add-host=host.docker.internal:host-gateway \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5200/apae \
  -e DB_USERNAME=user \
  -e DB_PASSWORD=pass \
  -e JWT_SECRET=U29tZVNlY3JldEF0TGVhc3QtMjU2Qml0cw== \
  -e MINIO_URL=http://host.docker.internal:9000 \
  -e MINIO_ACCESS_KEY=ROOTUSER \
  -e MINIO_SECRET_KEY=CHANGEME123 \
  apae-geral-backend:local
```

> No **macOS / Windows** o `host.docker.internal` já existe sem `--add-host`,
> mas a flag não atrapalha. No **Linux** ela é obrigatória.

### 4.3 Stack inteira com imagens locais
Igual ao passo 4 do TL;DR (variante "imagens locais"):
```bash
BACKEND_IMAGE=apae-geral-backend \
FRONTEND_IMAGE=apae-geral-frontend \
IMAGE_TAG=local \
docker compose up -d
```

---

## 5. Validando que está saudável

### Healthchecks Docker
```bash
docker inspect --format='{{json .State.Health}}' apae-geral-backend  | jq
docker inspect --format='{{json .State.Health}}' apae-geral-frontend | jq
```
Status esperado depois de ~30–60s: `"Status": "healthy"`.

### Probes manuais
```bash
curl -s http://localhost:8080/apae-geral/actuator/health    # backend
# {"status":"UP"}

curl -sI http://localhost:3000/apae-geral/ | head -1        # frontend
# HTTP/1.1 200 OK
```

### Confirmar usuário não-root
Ambos os Dockerfiles criam e usam `appuser` (uid 1001):
```bash
docker exec apae-geral-backend  id
docker exec apae-geral-frontend id
# uid=1001(appuser) gid=1001(appgroup)
```

---

## 6. Troubleshooting

| Sintoma | Causa provável | Como resolver |
|---|---|---|
| `Connection to host.docker.internal:5200 refused` ao subir o backend isolado | Postgres não está rodando | `docker compose up -d db` antes (seção 4.2) |
| Backend reinicia em loop, logs mostram `Hibernate: Unable to determine Dialect` | Backend subiu antes do Postgres terminar de iniciar | Use `docker compose up -d` (o `depends_on: condition: service_healthy` resolve), ou `docker compose restart apae-geral-backend` |
| Frontend abre mas chamadas pra API caem em 404 | Buildou com `NEXT_PUBLIC_API_URL=/apae-geral/api` mas não tem proxy | Rebuilde com URL absoluto (seção 4.1) ou ponha um Nginx |
| Abriu `http://localhost:3000/` e veio 404 | Esperado — o frontend está sob `basePath: /apae-geral` | Acesse `http://localhost:3000/apae-geral` |
| `mvn ... COMPILATION ERROR` em `*Test.java` durante `docker build` | Dockerfile usou `-DskipTests` (só pula execução) em vez de `-Dmaven.test.skip=true` (pula compilação+execução) | Já corrigido no `apps/api/Dockerfile` |
| `denied: denied` no `docker compose pull` | Pacote no GHCR está privado | `docker login ghcr.io` (seção 1) ou torne o pacote público |
| `HEALTHCHECK` do backend fica `starting` para sempre | Spring Actuator faltando ou `/actuator/health` bloqueado | Confira `pom.xml` (tem `spring-boot-starter-actuator`) e `SecurityConfiguration.java` (libera `/actuator/health`) |
| `port is already allocated` no `docker compose up` | Algo já está usando 3000/8080/5200/9000/9001 no host | `lsof -i :PORTA` para descobrir e matar, ou mude a porta no `.env` |
| `bind: address already in use` apenas na porta 8080 | Você tinha um backend `docker run` solto antes | `docker ps` + `docker stop <id>` |
| Frontend mostra "API offline" ou CORS error | `CORS_ALLOWED_ORIGINS` do backend não inclui a origem do frontend | No `.env`: `CORS_ALLOWED_ORIGINS=http://localhost:3000` |

### Comandos de diagnóstico
```bash
# status geral
docker compose ps

# logs (último 1m)
docker compose logs --since 1m apae-geral-backend
docker compose logs --since 1m apae-geral-frontend

# entrar no container pra debugar
docker exec -it apae-geral-backend  sh
docker exec -it apae-geral-frontend sh

# rede do compose (útil pra Opção C de docker run isolado)
docker network ls | grep apae
docker network inspect apae_apae-network
```

---

## 7. Integração com o Portal dos 30 anos

O `docker-compose.yml` deste repositório é para **desenvolvimento local**. Em
produção, o orquestrador central do Portal dos 30 anos é quem importa as
imagens publicadas no GHCR:

- `ghcr.io/ifpbesp/apae-geral-frontend:<tag>`
- `ghcr.io/ifpbesp/apae-geral-backend:<tag>`

Pontos a alinhar com o time do Portal:
1. **`JWT_SECRET` precisa ser idêntico** entre os produtos (SSO).
2. Todo o tráfego do APAE (frontend e API) vive sob o prefixo **`/apae-geral`**.
   O Nginx do Portal deve fazer proxy preservando esse prefixo:
   - `/apae-geral/api/*` → `apae-geral-backend:8080`
   - `/apae-geral/*`     → `apae-geral-frontend:3000`
   Isso elimina o conflito de paths com os demais produtos (gestão-escolar,
   apae-30-anos, etc.) que dividem o mesmo domínio.
3. As tags publicadas são `dev`, `latest` e `sha-<short>` — o Portal pode
   pinar numa tag de SHA para releases controladas.
