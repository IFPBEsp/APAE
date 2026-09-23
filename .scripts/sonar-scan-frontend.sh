#!/bin/bash
set -e

if [ -z "$SONAR_TOKEN" ]; then
  echo "Erro: A variável de ambiente SONAR_TOKEN não está definida."
  echo "Uso: SONAR_TOKEN=<seu_token> $0"
  exit 1
fi

SONAR_HOST_URL="${SONAR_HOST_URL:-http://localhost:9500}"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

echo "Iniciando análise do SonarQube para o frontend (apps/apae)..."
echo "Servidor SonarQube: $SONAR_HOST_URL"

docker run --rm \
  --network host \
  -v "$ROOT_DIR/apps/apae:/usr/src" \
  -e SONAR_HOST_URL="$SONAR_HOST_URL" \
  -e SONAR_TOKEN="$SONAR_TOKEN" \
  sonarsource/sonar-scanner-cli:11