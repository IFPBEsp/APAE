#!/bin/bash

set -e

if [ -f .env ]; then
  echo "Carregando variáveis do .env..."
  set -a
  source .env
  set +a
else
  echo "Arquivo .env não encontrado, usando defaults..."
fi

DB_CONTAINER=${POSTGRES_CONTAINER:-apae-postgres}
DB_NAME=${POSTGRES_NAME:-apae}
DB_USERNAME=${POSTGRES_USERNAME:-user}

echo "Verificando se o container está rodando..."

if ! docker ps --format '{{.Names}}' | grep -q "$DB_CONTAINER"; then
  echo "Container $DB_CONTAINER não está rodando!"
  exit 1
fi

echo "Verificando conexão com o banco dentro do container..."

if ! docker exec "$DB_CONTAINER" pg_isready -U "$DB_USERNAME" > /dev/null 2>&1; then
  echo "❌ Banco de dados não está disponível dentro do container!"
  exit 1
fi

if docker exec -i "$DB_CONTAINER" psql -U "$DB_USERNAME" -d "$DB_NAME" <<EOF
INSERT INTO usuarios (id, email, cpf, senha, nome_completo, cargo)
VALUES (
  '11111111-1111-4111-8111-111111111111',
  'admin@teste.com',
  '123.456.789-00',
  '\$2a\$10\$a7iR65cTGffpfuaBAImdHegVl99oyUHk.w6ldu9YmpBGs7dhIpLtK',
  'Administrador do Sistema',
  'ADMIN'
)
ON CONFLICT (email) DO NOTHING;
EOF
then
  echo "Usuário inserido (ou já existente)."
else
  echo "Erro ao inserir usuário."
  exit 1
fi

echo "Criando views mockadas do contexto escolar..."

if docker exec -i "$DB_CONTAINER" psql -v ON_ERROR_STOP=1 -U "$DB_USERNAME" -d "$DB_NAME" < .scripts/manuais/create_mock_views_escolar.sql; then
  echo "Views mockadas escolares criadas com sucesso(Relatórios e Avaliações)."
else
  echo "Erro ao criar views mockadas escolares."
  exit 1
fi