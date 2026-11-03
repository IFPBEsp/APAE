# Matriz de acesso do APAE Geral

Referência: issue [#1031](https://github.com/IFPBEsp/APAE/issues/1031) — Restringir o acesso às rotas da API conforme o papel do usuário.

## Matriz de papéis

| Papel | Rotas do APAE Geral |
|---|---|
| `ADMIN` | Acesso total |
| `APAE_GERAL` | Acesso total, exceto cadastro de usuário |
| `GESTAO_ESCOLAR` | Sem acesso |
| `ATENDIMENTO` | Sem acesso |

O papel padrão de um usuário criado via cadastro continua sendo `APAE_GERAL`.

## Regras por rota (backend)

As regras são aplicadas em um único ponto, via `authorizeHttpRequests` em `SecurityConfiguration`, nesta ordem:

| Rota | Regra |
|---|---|
| `POST /auth/signup` | Somente `ADMIN` (401 sem token; 403 para qualquer outro papel) |
| `POST /auth/signin` | Pública |
| `/auth/password-recovery/**` | Pública |
| `/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html` | Públicas |
| `/actuator/health`, `/actuator/health/**`, `/actuator/info` | Públicas |
| `/error` | Pública |
| Qualquer outra rota da API | `ADMIN` ou `APAE_GERAL` (401 sem token; 403 para os demais papéis) |

O papel do usuário é gravado no token JWT (claim `role`) e também retornado na resposta do login (`{ "token": "...", "role": "ADMIN" }`).

## Comportamento no login

Após autenticar com sucesso, o `POST /auth/signin` verifica o papel do usuário. Para `GESTAO_ESCOLAR` e `ATENDIMENTO`, nenhum token é gerado e a API responde:

- **Status:** `403 Forbidden`
- **Mensagem:** `Usuário sem acesso ao APAE Geral.`

Credenciais inválidas continuam retornando `401`.

## Comportamento do frontend

- A tela `/register` (e subrotas) só é acessível para `ADMIN`. O `proxy.ts` lê o claim `role` do JWT contido no cookie `session` e redireciona para `/` quando o papel não é `ADMIN` (ou não pode ser lido).
- O grupo "Área do Administrador" do menu lateral, que contém o item "Cadastrar Usuário", só é exibido para `ADMIN`.
- No login, a mensagem retornada pela API no erro 403 é exibida ao usuário.
- A garantia real de segurança é do backend; o frontend apenas esconde e redireciona.

