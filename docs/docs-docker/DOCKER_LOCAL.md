# Rodando o APAE Geral com Docker (local)

Este guia mostra como **buildar, rodar e validar** as imagens do APAE Geral
(frontend Next.js + backend Spring Boot) na sua máquina, antes de mergear na
`dev` ou consumir as imagens publicadas no GHCR.

> O `docker-compose.yml` da raiz já sobe Postgres, MinIO, backend e frontend.
> O que muda é **de onde as imagens vêm**: build local ou pull do GHCR.

---

## 1. Pré-requisitos

- Docker 24+ e Docker Compose v2 (`docker compose`, sem hífen).
- Arquivo `.env` na raiz, baseado em `.env.example`:
  ```bash
  cp .env.example .env
  ```
- (Opcional) Login no GHCR se o pacote estiver privado:
  ```bash
  echo $GH_TOKEN | docker login ghcr.io -u SEU_USUARIO --password-stdin
  ```
  O token só precisa ter o scope `read:packages`.

---

## 2. Variáveis de ambiente — o que é runtime e o que é build-time

Existem **dois tempos** distintos onde variáveis entram em jogo. Saber isso evita
horas de debug:

### Runtime (lidas quando o container sobe)
Todas as variáveis do **backend** (`DB_URL`, `JWT_SECRET`, `MINIO_*`, `MAIL_*`,
`CORS_ALLOWED_ORIGINS`, etc) e `NODE_ENV` do frontend são runtime. Ficam no `.env`
da máquina/VM e o `docker compose` injeta nelas a cada `up`. Mudou? `restart`
resolve.

### Build-time (cravadas dentro da imagem)
Variáveis `NEXT_PUBLIC_*` do Next.js são **inlinadas no bundle JavaScript** durante
`npm run build`. Ou seja, o valor é congelado dentro da imagem Docker no momento em
que o `docker build` roda. **Trocar a env depois não tem efeito** — você precisa
buildar a imagem de novo.

Por isso o Dockerfile do frontend define:
```dockerfile
ARG NEXT_PUBLIC_API_URL=/api
```
e o workflow do GHCR sempre buildar com `/api`. A premissa é que em produção haverá
um reverse proxy (Nginx/Traefik no Portal dos 30 anos) que faz `/api/*` →
`apae-geral-backend:8080`. Assim **uma única imagem serve todos os ambientes**.

Se você quiser rodar local **sem proxy**, troque o valor na hora do build (veja a
seção 4).

---

## 3. Subindo tudo a partir das imagens do GHCR

Esse é o fluxo que reproduz produção:

```bash
# Puxa as imagens publicadas pelo workflow ghcr-publish.yml
docker compose pull

# Sobe Postgres, MinIO, backend e frontend
docker compose up -d

# Acompanha logs
docker compose logs -f apae-geral-backend apae-geral-frontend
```

Para mirar uma tag específica (ex.: build de um SHA do CI):
```bash
IMAGE_TAG=sha-abc1234 docker compose up -d
```

Acesse:
- Frontend: <http://localhost:3000>
- Backend (Swagger): <http://localhost:8080/api/swagger-ui.html>
- Healthcheck: <http://localhost:8080/api/actuator/health>
- MinIO console: <http://localhost:9001>

Para derrubar:
```bash
docker compose down            # mantém volumes
docker compose down -v         # zera Postgres e MinIO
```

---

## 4. Buildando as imagens localmente (sem o GHCR)

Útil pra validar mudanças no Dockerfile antes de abrir PR.

### 4.1 Frontend isolado

```bash
docker build \
  --build-arg NEXT_PUBLIC_API_URL=http://localhost:8080/api \
  -t apae-geral-frontend:local \
  ./apps/apae

docker run --rm -p 3000:3000 apae-geral-frontend:local
```

> Note o `NEXT_PUBLIC_API_URL` apontando pro backend direto: no modo isolado não
> há proxy, então o frontend precisa do URL absoluto.

### 4.2 Backend isolado

No **Linux** o container precisa enxergar o Postgres rodando na sua máquina:
```bash
docker build -t apae-geral-backend:local ./apps/api

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

No **macOS / Windows** o `host.docker.internal` já existe sem flag adicional.

### 4.3 Tudo junto via compose (rebuild forçado)

Se quiser que o compose reconstrua em vez de puxar do GHCR, sobrescreva as imagens
apontando pras tags locais que você acabou de criar:

```bash
BACKEND_IMAGE=apae-geral-backend:local \
FRONTEND_IMAGE=apae-geral-frontend:local \
IMAGE_TAG=local \
docker compose up -d
```

> O `docker-compose.yml` lê `${BACKEND_IMAGE:-...}:${IMAGE_TAG:-dev}` — esse override
> bate na tag `apae-geral-backend:local`.

---

## 5. Validando que as imagens estão saudáveis

### Healthchecks
Os dois containers expõem `HEALTHCHECK` no Dockerfile. Para inspecionar:

```bash
docker inspect --format='{{json .State.Health}}' apae-geral-backend  | jq
docker inspect --format='{{json .State.Health}}' apae-geral-frontend | jq
```

Status esperado depois de ~30s: `"Status": "healthy"`.

### Probes manuais
```bash
# Backend (Actuator)
curl -s http://localhost:8080/api/actuator/health
# {"status":"UP"}

# Frontend
curl -sI http://localhost:3000/ | head -1
# HTTP/1.1 200 OK
```

### Verificar usuário não-root
Ambos os Dockerfiles criam e usam `appuser` (uid 1001). Para confirmar:
```bash
docker exec apae-geral-backend  id
docker exec apae-geral-frontend id
# uid=1001(appuser) gid=1001(appgroup)
```

---

## 6. Troubleshooting

| Sintoma | Causa provável | Como resolver |
|---|---|---|
| Frontend abre mas chamadas pra API caem em 404 | Buildou com `NEXT_PUBLIC_API_URL=/api` mas não tem proxy na frente | Rebuilde com URL absoluto (seção 4.1) ou ponha um Nginx |
| Backend reinicia em loop | Postgres ainda não está pronto / credenciais erradas | Confira `.env`, espere o `healthcheck` do `db` ficar verde |
| `denied: denied` no `docker compose pull` | Pacote no GHCR está privado | `docker login ghcr.io` (seção 1) ou torne o pacote público |
| `HEALTHCHECK` do backend fica `starting` pra sempre | Spring Actuator não foi adicionado ou `/actuator/health` está bloqueado pelo Security | Confira `pom.xml` e `SecurityConfiguration.java` |
| Linux: backend não consegue achar Postgres do host | Falta `--add-host` | Use `--add-host=host.docker.internal:host-gateway` ou rode tudo via compose |

---

## 7. Integração com o Portal dos 30 anos

O `docker-compose.yml` deste repositório é para **desenvolvimento local**. Em
produção, o orquestrador central do Portal dos 30 anos é quem importa as imagens
publicadas no GHCR:

- `ghcr.io/ifpbesp/apae-geral-frontend:<tag>`
- `ghcr.io/ifpbesp/apae-geral-backend:<tag>`

Pontos a alinhar com o time do Portal:
1. **`JWT_SECRET` precisa ser idêntico** entre os produtos (SSO).
2. O Portal deve resolver `/api` no frontend do APAE para o backend correspondente
   via reverse proxy.
3. As tags publicadas são `dev`, `latest` e `sha-<short>` — o Portal pode pinar
   numa tag de SHA para releases controladas.
