# Projeto APAE — Documentação de Arquitetura e Refatoração

**Migração para Arquitetura de Fatiamento Vertical (Domain-Driven UI)**

---

## 1. Visão Geral

Este documento descreve a arquitetura adotada no projeto APAE após a migração de uma estrutura horizontal (agrupamento por tipo técnico) para uma estrutura de Fatiamento Vertical (agrupamento por domínio de negócio). Serve como referência oficial para todos os desenvolvedores durante e após a refatoração.

---

## 2. Justificativa da Migração

### 2.1 Problemas da Arquitetura Anterior (Horizontal)

- **Alta carga cognitiva:** exemplo: alterar a funcionalidade de Pacientes exigia navegar por múltiplos diretórios separados (`hooks/`, `schemas/`, `services/` etc.).
- **Conflitos de merge frequentes:** vários desenvolvedores modificavam as mesmas pastas globais simultaneamente.
- **Baixa coesão:** arquivos que mudam juntos ficavam armazenados em locais distintos.

### 2.2 Solução: Fatiamento Vertical (Vertical Slicing)

Toda a lógica de uma funcionalidade — componentes, hooks, validação e chamadas de API — passa a viver dentro de um único domínio. O roteamento do Next.js fica exclusivamente responsável por receber parâmetros de URL e delegar para o domínio correto.

> **Benefício principal:** Um desenvolvedor trabalhando no domínio de `professionals` não corre o risco de quebrar o domínio de `vaccines`.

| Critério           | Arquitetura Anterior                       | Nova Arquitetura                                 |
| ------------------ | ------------------------------------------ | ------------------------------------------------ |
| Organização        | Por tipo técnico (`hooks/`, `schemas/`...) | Por domínio de negócio (`patients/`, `auth/`...) |
| Navegação          | Múltiplas pastas para uma feature          | Tudo em um único domínio                         |
| Conflitos de merge | Alta frequência                            | Reduzidos significativamente                     |
| Escalabilidade     | Pastas globais incham                      | Novos domínios isolados                          |
| Onboarding         | Curva alta                                 | Localização imediata de código                   |

---

## 3. Padrões de Código

### 3.1 Idioma

Todo o código, pastas e arquivos devem ser escritos em **inglês**. Nomes em português são considerados violação de padrão e serão rejeitados em code review.

### 3.2 Nomenclatura

| Contexto                    | Convenção              | Correto                               | Incorreto                           |
| --------------------------- | ---------------------- | ------------------------------------- | ----------------------------------- |
| Arquivos e pastas           | `kebab-case`           | `patient-card.tsx`, `use-patients.ts` | `PatientCard.tsx`, `usePatients.ts` |
| Componentes React (interno) | `PascalCase`           | `export function PatientCard() {}`    | `export function patientCard() {}`  |
| Variáveis e funções         | `camelCase`            | `const fetchPatients = () => {}`      | `const FetchPatients = () => {}`    |
| Tipos e interfaces          | `PascalCase`           | `interface PatientCardProps {}`       | `interface patientCardProps {}`     |
| Constantes globais          | `SCREAMING_SNAKE_CASE` | `const MAX_RETRIES = 3`               | `const maxRetries = 3`              |

---

## 4. Estrutura de Arquivos

### 4.1 Visão Geral da Raiz

```
src/
├── app/          # Camada de entrada Next.js (roteamento + API)
├── domains/      # Core da arquitetura (regras de negócio)
├── components/   # Componentes globais reutilizáveis
├── hooks/        # Hooks globais apenas
├── lib/          # Infraestrutura (axios, cookies, routes...)
├── types/        # Tipos globais
└── utils/        # Utilitários globais (manter enxuto)
```

### 4.2 Camada `app/` — Responsabilidade Exclusiva de Roteamento

> **Regra:** Páginas em `app/` apenas recebem parâmetros de URL e delegam para componentes de domínio. Zero lógica de negócio aqui.

```
app/
├── (public)/                       # Layouts e páginas públicas
│   └── auth/
│       ├── login/page.tsx           # /auth/login
│       ├── recovery/page.tsx        # /auth/recovery
│       └── reset-password/page.tsx  # /auth/reset-password
│
├── (private)/                       # Layouts e páginas privadas
│   ├── dashboard/page.tsx            # /dashboard
│   ├── absences/page.tsx             # /absences
│
│   ├── appointments/page.tsx         # /appointments → lista geral de agendamentos (antes all-appointments)
│   ├── patients/page.tsx             # /patients → lista de pacientes (antes visualization-patients)
│   ├── professionals/page.tsx       # /professionals → lista de profissionais (antes visualization-professional)
│
│   ├── vaccines/
│   │   ├── page.tsx                  # /vaccines → lista
│   │   ├── create/page.tsx           # /vaccines/create
│   │   └── [id]/edit/page.tsx        # /vaccines/:id/edit
│   │
│   ├── disorders/
│   │   ├── page.tsx                  # /disorders → lista
│   │   ├── create/page.tsx           # /disorders/create
│   │   └── [id]/edit/page.tsx        # /disorders/:id/edit
│   │
│   ├── documents/
│   │   ├── page.tsx                  # /documents → lista
│   │   ├── create/page.tsx           # /documents/create
│   │   └── [id]/edit/page.tsx        # /documents/:id/edit
│   │
│   └── service-types/
│       ├── page.tsx                  # /service-types → lista
│       ├── create/page.tsx           # /service-types/create
│       └── [id]/edit/page.tsx        # /service-types/:id/edit
│
├── api/                              # Endpoints da API
│   └── ...
│
├── layout.tsx                        # Layout global
└── globals.css                       # Estilos globais
```

### 4.3 Camada `domains/` — Core da Arquitetura

```
domains/
└── [nome-do-dominio]/         # Ex: patients, auth, professionals
    │
    ├── [feature]/             # Agrupamento por tela/ação (Ex: create, list, edit)
    │   ├── [feature]-form.tsx # Renderização da interface (UI)
    │   └── use-[feature].ts   # Hook contendo estado e lógica da feature
    │
    ├── shared/                # Componentes e utils exclusivos deste domínio
    │
    ├── [dominio].api.ts       # Apenas chamadas HTTP (Axios/Fetch) relativas ao domínio
    ├── [dominio].types.ts     # Interfaces e Types (Tipagem estática)
    └── [dominio].schema.ts    # Validações estruturais (Zod)
```

### 4.4 Camada `components/` — Globais Reutilizáveis

```
components/
├── ui/
│   ├── button.tsx
│   ├── input.tsx
│   ├── dialog.tsx
│   ├── table.tsx
│   ├── form.tsx
│   └── ...
├── shared/
│   ├── page-header.tsx
│   ├── empty-state.tsx
│   ├── error-state.tsx
│   ├── loading-state.tsx
│   └── status-badge.tsx
└── layout/
    ├── sidebar.tsx
    ├── topbar.tsx
    ├── breadcrumb.tsx
    └── app-shell.tsx
```

### 4.5 Infraestrutura

```
hooks/
├── use-debounce.ts
└── use-mobile.ts

lib/
├── axios.ts
├── query-client.ts
├── cookies.ts
├── routes.ts

types/
├── pagination.ts
└── shared.ts

utils/
└── form-errors.ts
├── format-date.ts
└── parse-currency.ts

middleware.ts (verificar na atualização do nextjs o uso de proxy ao invés de middleware)
```

---

## 5. Pontos de Ajuste Identificados

> ⚠️ Os itens abaixo foram identificados na proposta atual e devem ser resolvidos antes ou durante a refatoração.

### 5.1 Inconsistências de Nomenclatura (Português → Inglês)

| Arquivo atual                            | Arquivo correto                                              | Domínio                       |
| ---------------------------------------- | ------------------------------------------------------------ | ----------------------------- |
| `disponibilidade-form.tsx`               | `availability-form.tsx`                                      | `professionals/availability/` |
| `use-estados-cidades.ts`                 | `use-states-cities.ts`                                       | `professionals/`              |
| `disponibilidade.utils.ts` (em `utils/`) | `availability.utils.ts` → mover para `professionals/shared/` | `professionals/shared/`       |

### 5.2 Desalinhamento de Nomes entre `app/` e `domains/`

| Rota em `app/`                | Domínio em `domains/` | Ação necessária                            |
| ----------------------------- | --------------------- | ------------------------------------------ |
| `service-types/`              | `service-area/`       | Alinhar para `service-types/` em ambos     |
| `visualization-patients/`     | Sem domínio mapeado   | Absorver em `patients/` como sub-rota      |
| `visualization-professional/` | Sem domínio mapeado   | Absorver em `professionals/` como sub-rota |

### 5.3 Pastas Vazias nos Domínios

Não criar a pasta antes de existir o arquivo que justifica sua existência.

- `domains/absence/` — `hooks/` e `services/` vazios
- `domains/vaccines/` — `components/` vazio
- `domains/disorders/` — `hooks/` e `services/` vazios
- `domains/service-area/` — todas as subpastas vazias
- `professionals/utils/` — vazio
- `appointments/utils/` — vazio

### 5.4 Duplicidade em `types/` e `lib/`

| Problema                        | Localização atual                         | Resolução                                                                  |
| ------------------------------- | ----------------------------------------- | -------------------------------------------------------------------------- |
| Dashboard types duplicado       | `types/dashboard/` e `domains/dashboard/` | Manter apenas em `domains/dashboard/`                                      |
| Utils globais fragmentados      | `utils/` e `lib/utils/`                   | Consolidar tudo em `lib/utils/`                                            |
| `health-areas.ts` e `states.ts` | `lib/`                                    | Avaliar se são dados ou lógica — mover para domínio correto se específicos |

## 6. Regras de Código Interno

Ao migrar um domínio, não basta mover arquivos. O código interno deve ser refatorado seguindo as 4 regras abaixo. **PRs que violarem estas regras serão rejeitados.**

### Regra 1 — Separação de Lógica e Interface (Padrão Hook)

É proibido fazer requisições de API ou ter múltiplos `useState`/`useEffect` diretamente dentro de um componente visual. Toda lógica de negócio deve estar em um Custom Hook.

| Camada     | Arquivo             | Responsabilidade                       |
| ---------- | ------------------- | -------------------------------------- |
| API        | `patient.api.ts`    | Apenas chamadas HTTP (`axios`/`fetch`) |
| Hook       | `use-patients.ts`   | Estado, efeitos colaterais, lógica     |
| Componente | `patients-list.tsx` | Apenas renderização da UI              |

**Incorreto — lógica misturada no componente:**

```tsx
export function PatientList() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    axios.get("/api/patients").then((res) => {
      setPatients(res.data);
      setLoading(false);
    });
  }, []);

  return <div>{/* UI */}</div>;
}
```

**Correto — camadas separadas:**

```ts
// patient.api.ts
export const getPatients = async () => axios.get('/api/patients');

// use-patients.ts
export function usePatients() {
  const [data, setData] = useState([]);
  // lógica de fetch
  return { data, loading };
}

// patients-list.tsx
export function PatientList() {
  const { data, loading } = usePatients();
  return <div>{/* UI */}</div>;
}
```

### Regra 2 — Quebra de Componentes Gigantes

Componentes ou páginas que excedam **~150–200 linhas** são considerados code smells. Devem ser quebrados em subcomponentes dentro da pasta do domínio.

**Incorreto:** `dashboard.tsx` com 800 linhas contendo header, gráficos, tabelas e modais.

**Correto:**

```
domains/dashboard/overview/
├── dashboard-header.tsx
├── dashboard-metrics-cards.tsx
├── dashboard-charts.tsx
└── recent-patients-table.tsx
```

### Regra 3 — Promoção de Componentes Compartilhados

- Usado em **1 domínio** → fica em `domains/[dominio]/`
- Usado em **2 ou mais domínios** → promovido para `components/shared/`
- Um domínio **nunca** importa da pasta `components/` de outro domínio

### Regra 4 — Tipagem Rigorosa

É proibido usar `any` ou deixar props sem tipagem. Tipos de entidades devem viver em `[dominio].types.ts`. Props simples podem ser tipadas no próprio arquivo do componente.

```tsx
import type { Patient } from "../patient.types";

interface PatientCardProps {
  patient: Patient;
  onEdit: (id: string) => void;
}

export function PatientCard({ patient, onEdit }: PatientCardProps) {
  return <div>{patient.name}</div>;
}
```

---

**Diretrizes de padronização sugeridas para o BFF:**

- **Fluxo Estrito:** `UI (Componente) -> Hook -> [dominio].api.ts -> BFF -> Backend Core`.
- **Segurança e Simplicidade:** O BFF deve mascarar tokens/chaves e agregar múltiplas chamadas complexas do backend core em uma única resposta, entregando os dados "mastigados" no formato exato que a interface precisa.
- **Padronização de Rotas e Erros:** Revisar os endpoints do BFF para garantir que sigam um padrão de nomenclatura uniforme (ex: `/api/v1/patients`) e retornem uma estrutura de erro padrão. Isso permite que a camada de frontend (`lib/axios.ts`) consiga interceptar e tratar as falhas de forma genérica.

_Projeto APAE · Documento de uso interno · v1.0 · 2026_
