# Qualidade de Código (Husky, lint-staged, Prettier, EditorConfig, Checkstyle, PMD)

> **Escopo deste documento:** este repositório recebe apenas a **configuração e as dependências** dessas ferramentas. A implantação definitiva do gate (quando/como ele passa a bloquear de fato commits e PRs de todo o time) é componentizada no repositório de devops. O que está aqui já funciona localmente para quem quiser usar, mas nada foi ligado de forma obrigatória em CI.

## O que foi adicionado

| Ferramenta   | Onde atua                 | O que faz                                                                                                 |
| ------------ | ------------------------- | --------------------------------------------------------------------------------------------------------- |
| Husky        | raiz do monorepo          | Gerencia o hook `.husky/pre-commit`, que roda `lint-staged`                                               |
| lint-staged  | raiz do monorepo          | Roda comandos de lint/format só nos arquivos **staged** do commit, escopados por app                      |
| Prettier     | `apps/apae`               | Formata `.ts`/`.tsx`/`.js`/`.jsx`/`.json`/`.css` automaticamente (`--write`)                              |
| ESLint       | `apps/apae`               | Lint semântico, com autofix (`--fix`) quando possível                                                     |
| EditorConfig | raiz (cobre os dois apps) | Indentação/charset/fim de linha consistentes entre editores, inclusive para `.java`                       |
| Checkstyle   | `apps/api`                | Verifica estilo Java (imports não usados, chaves obrigatórias em `if`, etc.) — só verifica, não reformata |
| PMD          | `apps/api`                | Análise estática Java (código morto, `catch` vazio, etc.) — só verifica, não corrige                      |

## Como isso influencia a rotina do dia a dia

- **`pnpm install` na raiz já instala o hook** (script `prepare` do Husky). Nada extra a fazer.
- Ao rodar `git commit`, o hook dispara `lint-staged`, que só olha pros arquivos que você deu `git add`:
  - Arquivo `.ts`/`.tsx`/`.js`/`.jsx` em `apps/apae` → é formatado com Prettier e lintado com ESLint (`--fix`). Se sobrar erro que o ESLint não consegue corrigir sozinho, o commit é **bloqueado** até você corrigir manualmente.
  - Arquivo `.java` em `apps/api` → roda `checkstyle:check` e `pmd:check` **só sobre esse arquivo**. Se ele tiver alguma violação, o commit é bloqueado (nenhum autofix acontece em Java).
  - Arquivo `.md`/`.yml`/`.yaml` na raiz → só passa pelo Prettier.
- **O hook pode ser burlado com `git commit --no-verify`** — é uma conveniência local, não uma trava definitiva. A trava de verdade (se/quando for ligada) fica no CI do repositório de devops.
- **Importante:** o Checkstyle/PMD do `apps/api` **não têm fase de build vinculada** (não rodam em `mvn compile`/`package`/`validate`) — só quando chamados explicitamente (pelo hook, ou manualmente). Isso foi proposital: o código-fonte do backend ainda tem violações pré-existentes que não foram corrigidas neste PR, então travar isso em toda build quebraria o dia a dia de qualquer dev. **Se você tocar (`git add`) num arquivo `.java` que já tinha uma violação antes da sua mudança, seu commit pode ser bloqueado mesmo sem você ter introduzido o problema** — nesse caso, corrija a violação apontada (ela é local ao arquivo que você já está mexendo) ou peça ajuda se não tiver certeza do que fazer.

## Comandos manuais

```bash
# Frontend — formatar/checar manualmente
pnpm --filter apae run format          # aplica o Prettier
pnpm --filter apae run format:check    # só verifica, não escreve (uso em CI)

# Backend — verificar manualmente (não roda sozinho em build)
cd apps/api
./mvnw checkstyle:check   # ver apps/api/checkstyle.xml
./mvnw pmd:check          # ver apps/api/pmd-ruleset.xml
```

O app `apps/apae` reaproveita o `.prettierrc`/`.prettierignore` da raiz e o binário do Prettier instalado como devDependency do workspace — não precisa instalar o Prettier de novo no app.

## Troubleshooting

Se o hook não disparar depois de clonar o repositório:

1. Confirme que rodou `pnpm install` na **raiz** (não dentro de `apps/*`).
2. Rode manualmente `pnpm exec husky` para reinstalar os hooks.
3. Verifique se `core.hooksPath` do Git aponta para `.husky`: `git config core.hooksPath` deve retornar `.husky`.

## O que fica para depois (fora do escopo deste PR)

- Ligar Checkstyle/PMD a uma fase de build ou a um gate de CI de verdade — depende de antes corrigir as violações pré-existentes no código do `apps/api`.
- Reformatação inicial do frontend inteiro com Prettier (hoje os arquivos legados ainda não estão formatados; só passam a ser formatados conforme forem tocados em commits novos).
- Spotless para reformatação automática de Java.
- Rollout/enforcement centralizado — repositório de devops.
