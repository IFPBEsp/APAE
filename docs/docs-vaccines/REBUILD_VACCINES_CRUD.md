# Como reconstruir o CRUD de vacinas

Este guia descreve como um dev pode reconstruir as funcionalidades de criar, editar e excluir vacinas no projeto APAE.

As funcionalidades foram removidas temporariamente para virar um exercício futuro. A listagem e a busca de vacinas continuam existindo e devem ser preservadas durante a reconstrução.

## Estado atual

Hoje o domínio de vacinas está em modo somente consulta.

No frontend, a rota `/vaccines` continua listando vacinas e filtrando por nome. Não existem mais páginas, botões ou chamadas para criar, editar ou excluir vacinas.

No backend, os endpoints `GET` de vacinas continuam disponíveis. Os endpoints `POST`, `PUT` e `DELETE` foram removidos.

Não altere:

- Entidade `Vaccine`.
- Repositório JPA enquanto os métodos existentes forem suficientes.
- Tabela, migrations ou dados existentes.
- Listagem e busca atuais.
- Fluxos de pacientes que apenas selecionam vacinas já cadastradas.

## Referências do projeto

Antes de implementar, leia:

- `docs/docs-frontend/DEVS_FRONTEND_README.md`
- `docs/docs-frontend/REFACT_ARCHITECTURE.md`
- `docs/docs-backend/DEVS_BACKEND_README.md`

Também use o domínio de transtornos como referência prática, porque ele ainda contém um CRUD parecido:

- Frontend: `apps/apae/src/domains/disorders`
- Rotas frontend: `apps/apae/src/app/disorders`
- Proxy frontend: `apps/apae/src/app/api/disorders`
- Backend controller: `DisorderController`
- Backend service: `DisorderApplicationService` e `DisorderApplicationServiceImpl`
- Backend mapper: `DisorderMapper`

## Objetivo da reconstrução

Ao final da issue, o sistema deve voltar a permitir:

- Criar vacina.
- Editar vacina.
- Excluir vacina.

E ainda deve manter:

- Listagem em `/vaccines`.
- Filtro por nome em `/vaccines`.
- Endpoints `GET /vaccines`, `GET /vaccines/{id}` e `GET /vaccines/search/by-name`.
- Entidade/modelo e dados existentes.

## Backend

### 1. DTOs

Crie novamente DTOs próprios para escrita de vacina em:

```text
apps/api/src/main/java/br/org/apae/api/common/dto/patient/request/vaccine/
```

Sugestão:

- `CreateVaccineDTO`
- `UpdateVaccineDTO`

O `VaccineNameDTO` existe para os fluxos de paciente que recebem nomes de vacinas já existentes. Não reutilize esse DTO como contrato principal do CRUD se a intenção for deixar a API mais clara.

Campos esperados:

```java
String name
```

Validações recomendadas:

- `@NotBlank`
- `@Size(min = 2, max = 100)`

### 2. Exceções de domínio

Recrie as exceções específicas de escrita se forem necessárias:

```text
apps/api/src/main/java/br/org/apae/api/patient/domain/exceptions/
```

Sugestões:

- `VaccineConflictException`: usada quando já existe vacina com o mesmo nome.
- `VaccineInUseException`: usada quando a vacina está vinculada a paciente e não pode ser removida.

Depois registre handlers em:

```text
apps/api/src/main/java/br/org/apae/api/patient/application/exceptions/PatientExceptionHandler.java
```

Status recomendados:

- Duplicidade: `409 CONFLICT`.
- Em uso: `409 CONFLICT`.
- Não encontrada: manter `404 NOT_FOUND`.

### 3. Mapper

Atualize:

```text
apps/api/src/main/java/br/org/apae/api/patient/application/mappers/VaccineMapper.java
```

Reintroduza apenas os métodos necessários para escrita, por exemplo:

```java
public Vaccine toEntity(CreateVaccineDTO dto) {
    return new Vaccine(dto.name());
}
```

Evite remover os métodos de leitura já usados por pacientes e listagem.

### 4. Service

Atualize a interface:

```text
apps/api/src/main/java/br/org/apae/api/patient/application/interfaces/VaccineApplicationService.java
```

Adicione:

```java
VaccineResponseDTO createVaccine(CreateVaccineDTO dto);
VaccineResponseDTO updateVaccine(UUID id, UpdateVaccineDTO dto);
void deleteVaccine(UUID id);
```

Atualize a implementação:

```text
apps/api/src/main/java/br/org/apae/api/patient/application/internal/VaccineApplicationServiceImpl.java
```

Regras esperadas:

- Criar:
  - Verificar se já existe vacina com o mesmo nome.
  - Lançar `VaccineConflictException` se houver duplicidade.
  - Salvar a nova entidade.
  - Retornar `VaccineResponseDTO`.

- Editar:
  - Buscar por `id`.
  - Lançar `VaccineNotFoundException` se não existir.
  - Verificar duplicidade de nome em outro registro.
  - Atualizar o nome da entidade.
  - Retornar `VaccineResponseDTO`.

- Excluir:
  - Verificar se a vacina existe.
  - Verificar se está em uso por algum paciente.
  - Se estiver em uso, lançar `VaccineInUseException`.
  - Excluir e fazer `flush()` para capturar violação de integridade.

Use `@Transactional` nas operações de escrita.

### 5. Repository

Confira se o repository já atende ao necessário:

```text
apps/api/src/main/java/br/org/apae/api/patient/domain/repository/VaccineRepository.java
```

Hoje ele já possui:

- `findByName`
- `findByNameInIgnoreCase`

Para editar com validação de duplicidade, pode ser útil adicionar algo como:

```java
boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
```

Para excluir, a checagem de uso de vacina atualmente passa por:

```text
apps/api/src/main/java/br/org/apae/api/patient/domain/repository/PatientRepository.java
```

Procure por `isVaccineInUse`.

### 6. Controller

Atualize:

```text
apps/api/src/main/java/br/org/apae/api/patient/interfaces/controllers/VaccineController.java
apps/api/src/main/java/br/org/apae/api/controllers/vaccine/VaccineControllerImpl.java
```

Endpoints a recriar:

```http
POST /vaccines
PUT /vaccines/{id}
DELETE /vaccines/{id}
```

Respostas esperadas:

- `POST /vaccines`: `201 CREATED` com `VaccineResponseDTO`.
- `PUT /vaccines/{id}`: `200 OK` com `VaccineResponseDTO`.
- `DELETE /vaccines/{id}`: `204 NO CONTENT`.

Mantenha intactos:

```http
GET /vaccines
GET /vaccines/{id}
GET /vaccines/search/by-name?name=...
```

## Frontend

Siga a arquitetura de fatiamento vertical descrita em `REFACT_ARCHITECTURE.md`.

O domínio principal fica em:

```text
apps/apae/src/domains/vaccines/
```

As páginas Next.js devem ficar leves, apenas delegando para componentes do domínio.

### 1. Tipos

Atualize:

```text
apps/apae/src/domains/vaccines/vaccines.types.ts
```

Adicione novamente:

```ts
export type CreateVaccineParams = Readonly<{
  name: string;
}>;

export type UpdateVaccineParams = Readonly<{
  id: string;
  name: string;
}>;

export type DeleteVaccineParams = Readonly<{
  id: string;
}>;
```

Mantenha o tipo `Vaccine` usado pela listagem.

### 2. Schemas

Recrie:

```text
apps/apae/src/domains/vaccines/vaccines.schema.ts
```

Sugestão:

```ts
import { z } from "zod";

export const createVaccineSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const updateVaccineSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export type CreateVaccineFormData = z.infer<typeof createVaccineSchema>;
export type UpdateVaccineFormData = z.infer<typeof updateVaccineSchema>;
```

### 3. API do domínio

Atualize:

```text
apps/apae/src/domains/vaccines/vaccines.api.ts
```

Reintroduza:

- `fetchVaccineApi(id)`
- `createVaccineApi(params)`
- `updateVaccineApi(params)`
- `deleteVaccineApi(params)`

Use como referência:

```text
apps/apae/src/domains/disorders/disorders.api.ts
```

O `BASE_URL` deve continuar:

```ts
const BASE_URL = "/apae-geral/api/vaccines";
```

### 4. Rotas proxy do Next.js

Atualize:

```text
apps/apae/src/app/api/vaccines/route.ts
```

Reintroduza:

```ts
export async function POST(request: Request) {}
```

Recrie:

```text
apps/apae/src/app/api/vaccines/[id]/route.ts
```

Com:

```ts
export async function GET(request: Request, { params }: Params) {}
export async function PUT(request: Request, { params }: Params) {}
export async function DELETE(request: Request, { params }: Params) {}
```

Essas rotas devem encaminhar para a API Java usando `createBaseApi()`.

### 5. Hooks e telas de criação

Recrie:

```text
apps/apae/src/domains/vaccines/create/use-vaccine-create.ts
apps/apae/src/domains/vaccines/create/vaccine-form.tsx
apps/apae/src/app/vaccines/new/page.tsx
```

Comportamento esperado:

- Exibir formulário com campo `name`.
- Validar com `createVaccineSchema`.
- Chamar `createVaccineApi`.
- Exibir toast de sucesso ou erro.
- Redirecionar para `/vaccines` ao salvar com sucesso.
- Botão de voltar/cancelar deve retornar para a tela anterior ou `/vaccines`.

### 6. Hooks e telas de edição

Recrie:

```text
apps/apae/src/domains/vaccines/edit/use-vaccine-edit.ts
apps/apae/src/domains/vaccines/edit/vaccine-form.tsx
apps/apae/src/app/vaccines/[id]/edit/page.tsx
```

Comportamento esperado:

- Ler `id` da rota.
- Buscar a vacina por `GET /vaccines/{id}`.
- Preencher o formulário com o nome atual.
- Validar com `updateVaccineSchema`.
- Chamar `updateVaccineApi`.
- Exibir loading enquanto carrega.
- Redirecionar para `/vaccines` ao salvar com sucesso.

### 7. Ações na listagem

Atualize:

```text
apps/apae/src/domains/vaccines/list/vaccines-list.tsx
apps/apae/src/domains/vaccines/list/use-vaccines-list.ts
apps/apae/src/domains/vaccines/shared/vaccine-list-item.tsx
```

Reintroduza:

- Botão desktop para adicionar vacina.
- Botão flutuante mobile para adicionar vacina.
- Botão de editar em cada item.
- Botão de excluir em cada item.
- Modal de confirmação para exclusão.
- Tooltip ou estado desabilitado quando `hasPatient` for `true`.

Cuidados:

- A busca por nome deve continuar local e sem regressão.
- A listagem deve recarregar após exclusão.
- Não exiba ação de excluir habilitada se a vacina estiver associada a paciente.

### 7. Criação inline no cadastro de paciente

Antes da remoção, o cadastro de paciente tinha um modal para criar vacina a partir do campo de seleção.

Se a issue pedir apenas o CRUD em `/vaccines`, não reintroduza criação inline no cadastro de paciente.

Se a issue pedir explicitamente a criação inline, recrie:

```text
apps/apae/src/domains/patients/components/dialogs/CreateVaccineDialog.tsx
```

E volte a passar `onCreate` no `CreatableMultiSelect` de vacinas em:

```text
apps/apae/src/app/patients/create/additional/page.tsx
```

Essa decisão deve estar clara no escopo da issue.

## Critérios de aceite

- `/vaccines` lista vacinas.
- `/vaccines` filtra vacinas por nome.
- `/vaccines/new` permite criar vacina.
- `/vaccines/[id]/edit` permite editar vacina.
- A listagem permite excluir vacina com confirmação.
- Vacina vinculada a paciente não pode ser excluída.
- Backend expõe `POST /vaccines`, `PUT /vaccines/{id}` e `DELETE /vaccines/{id}`.
- Backend continua expondo todos os endpoints `GET` existentes.
- Não há alterações em schema/tabela/migration/dados de vacina.

## Checklist de implementação

- Recriar DTOs de escrita no backend.
- Recriar exceções de conflito e vacina em uso.
- Registrar handlers de erro.
- Recriar métodos de service.
- Recriar endpoints no controller.
- Recriar tipos e schemas frontend.
- Recriar chamadas de API frontend.
- Recriar proxy routes do Next.js.
- Recriar páginas `/vaccines/new` e `/vaccines/[id]/edit`.
- Recolocar botões e ações na listagem.
- Validar build, lint e testes.
