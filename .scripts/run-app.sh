#!/bin/bash

MODE=${1:-both}
FRONTEND_TARGET=${2:-web}

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
MAVEN_CMD="$ROOT_DIR/apps/api/mvnw"

DB_HOST="localhost"
DB_PORT=5200
DB_MAX_RETRIES=5
DB_WAIT_SECONDS=2

check_db_up() {
  local attempt=1
  echo "Verificando banco de dados em $DB_HOST:$DB_PORT..."
  while ! nc -z "$DB_HOST" "$DB_PORT" >/dev/null 2>&1; do
    if [ "$attempt" -ge "$DB_MAX_RETRIES" ]; then
      echo "Banco de dados não respondeu."
      return 1
    fi
    attempt=$((attempt + 1))
    sleep "$DB_WAIT_SECONDS"
  done
  return 0
}

run_backend() {
  check_db_up || exit 1
  echo "Iniciando backend..."
  cd "$ROOT_DIR/apps/api" || exit 1
  $MAVEN_CMD spring-boot:run
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
  check_db_up || exit 1
  echo "Rodando backend e frontend..."
  BACKEND_CMD="cd $ROOT_DIR/apps/api && $MAVEN_CMD spring-boot:run"
  FRONTEND_CMD="cd $ROOT_DIR/apps/$FRONTEND_TARGET && pnpm dev"

  npx concurrently -k -n backend,frontend -c red,blue "$BACKEND_CMD" "$FRONTEND_CMD"
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
