#!/usr/bin/env bash
set -uo pipefail

SONAR_HOST_URL="${SONAR_HOST_URL:-http://localhost:9500}"
SONAR_TOKEN="${SONAR_TOKEN:-}"

if [ -z "$SONAR_TOKEN" ]; then
  echo "ERRO: SONAR_TOKEN não definido."
  exit 1
fi

AUTH_HEADER="Authorization: Bearer ${SONAR_TOKEN}"

sonar_api() {
  local method="$1"
  local endpoint="$2"
  shift 2
  curl -s -X "$method" -H "$AUTH_HEADER" "$SONAR_HOST_URL/$endpoint" "$@"
}

create_gate() {
  local gate_name="$1"
  echo "--> Configurando Quality Gate: '$gate_name'..."
  # Tenta destruir caso já exista
  sonar_api POST "api/qualitygates/destroy" -d "name=$gate_name" > /dev/null || true
  sonar_api POST "api/qualitygates/create" -d "name=$gate_name" > /dev/null
}

add_condition() {
  local gate_name="$1"
  local metric="$2"
  local op="$3"      
  local error="$4"   

  echo "    [Condição] $metric $op $error"
  # Tenta criar a condição (se já existir, ignora para evitar erro)
  sonar_api POST "api/qualitygates/create_condition" \
    -d "gateName=$gate_name" \
    -d "metric=$metric" \
    -d "op=$op" \
    -d "error=$error" > /dev/null || true
}

associate_project() {
  local gate_name="$1"
  local project_key="$2"
  echo "    [Associação] Projeto '$project_key' -> Gate '$gate_name'"
  local resp
  resp=$(sonar_api POST "api/qualitygates/select" \
    -d "gateName=$gate_name" \
    -d "projectKey=$project_key")
  
  if echo "$resp" | grep -q "not found";> /dev/null 2>&1; then
    echo "    [AVISO] Projeto '$project_key' ainda não foi analisado no servidor. A associação será ignorada por enquanto."
  fi
}

# 1. Gate Backend
GATE_BACKEND="apae-geral-backend"
create_gate "$GATE_BACKEND"
add_condition "$GATE_BACKEND" "new_coverage" "LT" "43"
add_condition "$GATE_BACKEND" "new_duplicated_lines_density" "GT" "2"
add_condition "$GATE_BACKEND" "new_reliability_rating" "GT" "4"
add_condition "$GATE_BACKEND" "new_security_rating" "GT" "4"
add_condition "$GATE_BACKEND" "new_security_hotspots_reviewed" "LT" "100"
associate_project "$GATE_BACKEND" "apae-geral-backend"

# 2. Gate Frontend
GATE_FRONTEND="apae-geral-frontend"
create_gate "$GATE_FRONTEND"
add_condition "$GATE_FRONTEND" "new_duplicated_lines_density" "GT" "3"
add_condition "$GATE_FRONTEND" "new_reliability_rating" "GT" "1"
add_condition "$GATE_FRONTEND" "new_security_rating" "GT" "1"
add_condition "$GATE_FRONTEND" "new_security_hotspots_reviewed" "LT" "100"
associate_project "$GATE_FRONTEND" "apae-geral-frontend"

echo "Quality Gates configurados com sucesso!"
