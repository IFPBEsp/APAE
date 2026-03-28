#!/bin/bash

MODE=${1:-both}
FRONTEND_TARGET=${2:-apae}

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
MAVEN_CMD="$ROOT_DIR/apps/api/mvnw"
ENV_FILE="$ROOT_DIR/.env"

load_env() {
  if [ -f "$ENV_FILE" ]; then
    echo "Carregando variáveis de ambiente de $ENV_FILE..."
    set -a
    . "$ENV_FILE"
    set +a
  else
    echo "Arquivo .env não encontrado em $ENV_FILE. Seguindo sem variáveis locais."
  fi
}

DB_MAX_RETRIES=5
DB_WAIT_SECONDS=2

check_db_up() {
  local attempt=1
  echo "Verificando banco de dados em $POSTGRES_HOST:$POSTGRES_PORT..."
  while [ "$attempt" -le "$DB_MAX_RETRIES" ]; do
    if command -v nc >/dev/null 2>&1; then
    #nc - Netcat : verificação do linux
      if nc -z "$POSTGRES_HOST" "$POSTGRES_PORT" >/dev/null 2>&1; then
        echo "Banco de dados online (nc)"
        return 0
      fi
      #Verificação para o Windows (PowerShell)
    elif command -v powershell.exe >/dev/null 2>&1; then
      if powershell.exe -Command "Test-NetConnection -ComputerName $POSTGRES_HOST -Port $POSTGRES_PORT -InformationLevel Quiet" | grep -q "True"; then
        echo "Banco de dados online (ps)"
        return 0
      fi
    else
      echo "Nenhuma ferramenta de verificação (nc/powershell) encontrada."
      return 0
    fi

    echo "Aguardando banco... ($attempt/$DB_MAX_RETRIES)"
    attempt=$((attempt + 1))
    sleep "$DB_WAIT_SECONDS"
  done

  echo "Banco de dados não respondeu na porta $DB_PORT."
  return 1
}

run_backend() {
  load_env
  check_db_up || exit 1
  echo "Iniciando backend..."
  cd "$ROOT_DIR/apps/api" || exit 1
  $MAVEN_CMD spring-boot:run -Dmaven.test.skip=true
}

run_frontend() {
  echo "Iniciando frontend ($FRONTEND_TARGET)..."
  cd "$ROOT_DIR/apps/$FRONTEND_TARGET" || {
    echo "Projeto frontend '$FRONTEND_TARGET' não encontrado."
    exit 1
  }
  pnpm dev
}

run_both() {
  load_env
  check_db_up || exit 1
  echo "Rodando backend e frontend..."
  npx concurrently -k -n backend,frontend -c red,blue "bash ./.scripts/run-app.sh backend" "bash ./.scripts/run-app.sh frontend $FRONTEND_TARGET"
}

case "$MODE" in
  backend)
    run_backend
    ;;
  frontend)
    run_frontend
    ;;
  both|"")
    run_both
    ;;
  *)
    echo "Uso: ./run-app.sh [backend|frontend|both] [frontendProject]"
    exit 1
    ;;
esac
