# Guia de Testes - Upload de Documentos para Profissionais

Este guia explica como testar a funcionalidade de upload de arquivos para profissionais de saúde.

## Pré-requisitos

1. **Backend rodando**: A API deve estar em execução (porta 8090)
2. **MinIO configurado**: O MinIO deve estar rodando e acessível
3. **Profissional cadastrado**: Você precisa ter um profissional cadastrado para testar o upload

## Endpoint

```
POST /api/professionals/{id}/documents
Content-Type: multipart/form-data
```

---

## 1. Criar um Profissional para Teste

Primeiro, crie um profissional para usar nos testes:

```bash
curl -X POST http://localhost:8090/api/professionals \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. João Silva",
    "email": "joao.silva@teste.com",
    "healthSector": "Cardiologia",
    "phoneNumber": "(11) 99999-9999",
    "professionalDocument": "CRM123456",
    "identityDocument": "123456789",
    "address": {
      "street": "Rua Teste",
      "number": "123",
      "neighborhood": "Centro",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01234-567"
    }
  }'
```

**Resposta esperada**: Status 201 com dados do profissional criado, incluindo o `id` (UUID).

**Anote o `id` do profissional** para usar nos próximos testes.

---

## 2. Testar Upload com Arquivo Válido (PDF)

### 2.1 Usando cURL

```bash
# Substitua {PROFESSIONAL_ID} pelo ID do profissional criado
# Substitua /caminho/para/arquivo.pdf pelo caminho do seu arquivo PDF

curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@/caminho/para/arquivo.pdf"
```

### 2.2 Upload de Múltiplos Arquivos

```bash
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@/caminho/para/arquivo1.pdf" \
  -F "documents.attachments=@/caminho/para/arquivo2.jpg" \
  -F "documents.attachments=@/caminho/para/arquivo3.png"
```

### 2.3 Usando PowerShell (Windows)

```powershell
$professionalId = "SEU-UUID-AQUI"
$filePath = "C:\caminho\para\arquivo.pdf"

$form = @{
    documents = @{
        attachments = Get-Item $filePath
    }
}

Invoke-RestMethod -Uri "http://localhost:8090/api/professionals/$professionalId/documents" `
    -Method Post `
    -Form $form
```

### 2.4 Resposta Esperada

**Sucesso (200 OK)**:

```json
{}
```

**Erro - Profissional não encontrado (404)**:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Profissional não encontrado",
  "path": "/api/professionals/{id}/documents"
}
```

---

## 3. Testar Validações de Formato de Arquivo

### 3.1 Testar Formato Inválido (TXT não permitido)

```bash
# Criar arquivo de teste
echo "Conteúdo de teste" > teste.txt

curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@teste.txt" \
  -v
```

**Resposta esperada**: Status 400 Bad Request

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Extensão de arquivo 'txt' não permitida. Formatos permitidos: PDF, JPG, PNG, DOCX",
  "path": "/api/professionals/{id}/documents"
}
```

### 3.2 Testar Formatos Válidos

Teste cada formato permitido:

```bash
# PDF
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@documento.pdf"

# JPG
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@imagem.jpg"

# PNG
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@imagem.png"

# DOCX
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@documento.docx"
```

**Todos devem retornar**: Status 200 OK

---

## 4. Testar Validações de Tamanho de Arquivo

### 4.1 Criar Arquivo Grande para Teste (> 10MB)

**Windows PowerShell**:

```powershell
# Criar arquivo de 11MB
$bytes = New-Object byte[] (11 * 1024 * 1024)
[System.IO.File]::WriteAllBytes("arquivo_grande.pdf", $bytes)
```

**Linux/Mac**:

```bash
# Criar arquivo de 11MB
dd if=/dev/zero of=arquivo_grande.pdf bs=1M count=11
```

### 4.2 Testar Upload de Arquivo Grande

```bash
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@arquivo_grande.pdf" \
  -v
```

**Resposta esperada**: Status 400 Bad Request

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "O arquivo 'arquivo_grande.pdf' possui 11.00 MB e excede o limite máximo de 10.00 MB",
  "path": "/api/professionals/{id}/documents"
}
```

### 4.3 Testar Arquivo Dentro do Limite (< 10MB)

```bash
# Criar arquivo de 5MB (dentro do limite)
dd if=/dev/zero of=arquivo_ok.pdf bs=1M count=5

curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=@arquivo_ok.pdf"
```

**Resposta esperada**: Status 200 OK

---

## 5. Testar Validação de Lista Vazia

```bash
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -F "documents.attachments=" \
  -v
```

**Resposta esperada**: Status 400 Bad Request

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "A lista de arquivos não pode estar vazia",
  "path": "/api/professionals/{id}/documents"
}
```

---

## 6. Verificar Armazenamento no MinIO

### 6.1 Acessar MinIO Console

1. Acesse: http://localhost:9000
2. Login: `ROOTUSER` / `CHANGEME123`
3. Procure pelo bucket com o nome do UUID do profissional

### 6.2 Verificar Arquivos via API MinIO

```bash
# Usar MinIO client (mc) se instalado
mc ls local/{PROFESSIONAL_ID}/

# Ou via API do MinIO
curl -X GET "http://localhost:9000/{PROFESSIONAL_ID}/" \
  -H "Authorization: AWS ROOTUSER:CHANGEME123"
```

### 6.3 Verificar Logs da Aplicação

Os logs devem mostrar:

```
INFO: Recebida requisição de upload de documentos para profissional ID: {id}
INFO: Iniciando armazenamento de X documento(s) para profissional ID: {id}
INFO: Iniciando upload de documento para profissional ID: {id} - Arquivo: {nome} - Tamanho: {tamanho} bytes - Tipo: {tipo}
INFO: Upload concluído com sucesso para profissional ID: {id} - Arquivo: {nome}
INFO: Processamento de documentos concluído para profissional ID: {id} - Sucessos: X - Falhas: 0
```

---

## 7. Testar Cenários de Erro

### 7.1 Profissional Não Existente

```bash
curl -X POST http://localhost:8090/api/professionals/00000000-0000-0000-0000-000000000000/documents \
  -F "documents.attachments=@arquivo.pdf" \
  -v
```

**Resposta esperada**: Status 404 Not Found

### 7.2 Request Sem Arquivos

```bash
curl -X POST http://localhost:8090/api/professionals/{PROFESSIONAL_ID}/documents \
  -H "Content-Type: multipart/form-data"
```

**Resposta esperada**: Status 400 Bad Request

---

## 8. Testar com Postman

### 8.1 Configuração

1. Método: `POST`
2. URL: `http://localhost:8090/api/professionals/{id}/documents`
3. Body: Selecione `form-data`
4. Adicione campos:
   - Key: `documents.attachments` (tipo: File)
   - Value: Selecione um arquivo

### 8.2 Upload Múltiplos Arquivos

Adicione múltiplos campos com o mesmo nome `documents.attachments` e selecione arquivos diferentes.

---

## 9. Testar com Swagger UI

1. Acesse: http://localhost:8090/swagger-ui.html
2. Procure pelo endpoint `POST /professionals/{id}/documents`
3. Clique em "Try it out"
4. Preencha o `id` do profissional
5. Clique em "Choose Files" e selecione arquivos
6. Clique em "Execute"

---

## 10. Script de Teste Automatizado

Crie um script de teste (exemplo em bash):

```bash
#!/bin/bash

BASE_URL="http://localhost:8090/api"
PROFESSIONAL_ID=""

# 1. Criar profissional
echo "Criando profissional..."
RESPONSE=$(curl -s -X POST "$BASE_URL/professionals" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dr. Teste",
    "email": "teste@teste.com",
    "healthSector": "Teste",
    "phoneNumber": "(11) 99999-9999",
    "professionalDocument": "TEST123",
    "identityDocument": "123456789",
    "address": {
      "street": "Rua Teste",
      "number": "123",
      "neighborhood": "Centro",
      "city": "São Paulo",
      "state": "SP",
      "zipCode": "01234-567"
    }
  }')

PROFESSIONAL_ID=$(echo $RESPONSE | grep -o '"id":"[^"]*' | cut -d'"' -f4)
echo "Profissional criado com ID: $PROFESSIONAL_ID"

# 2. Testar upload válido
echo "Testando upload válido..."
curl -X POST "$BASE_URL/professionals/$PROFESSIONAL_ID/documents" \
  -F "documents.attachments=@teste.pdf" \
  -w "\nStatus: %{http_code}\n"

# 3. Testar formato inválido
echo "Testando formato inválido..."
curl -X POST "$BASE_URL/professionals/$PROFESSIONAL_ID/documents" \
  -F "documents.attachments=@teste.txt" \
  -w "\nStatus: %{http_code}\n"

# 4. Testar arquivo grande
echo "Testando arquivo grande..."
curl -X POST "$BASE_URL/professionals/$PROFESSIONAL_ID/documents" \
  -F "documents.attachments=@arquivo_grande.pdf" \
  -w "\nStatus: %{http_code}\n"

echo "Testes concluídos!"
```

---

## Resumo dos Testes

| Teste                   | Método | Endpoint                        | Status Esperado |
| ----------------------- | ------ | ------------------------------- | --------------- |
| Upload válido (PDF)     | POST   | `/professionals/{id}/documents` | 200 OK          |
| Upload válido (JPG)     | POST   | `/professionals/{id}/documents` | 200 OK          |
| Upload válido (PNG)     | POST   | `/professionals/{id}/documents` | 200 OK          |
| Upload válido (DOCX)    | POST   | `/professionals/{id}/documents` | 200 OK          |
| Formato inválido        | POST   | `/professionals/{id}/documents` | 400 Bad Request |
| Arquivo muito grande    | POST   | `/professionals/{id}/documents` | 400 Bad Request |
| Lista vazia             | POST   | `/professionals/{id}/documents` | 400 Bad Request |
| Profissional não existe | POST   | `/professionals/{id}/documents` | 404 Not Found   |

---

## Verificação de Persistência

Para verificar se os arquivos foram persistidos corretamente:

1. **Logs da aplicação**: Verifique os logs para confirmar sucesso
2. **MinIO Console**: Acesse http://localhost:9000 e verifique os buckets
3. **API de listagem**: Use o endpoint de listagem de documentos (se disponível)

Os arquivos devem estar organizados em buckets por profissional (bucket = UUID do profissional).
