#!/bin/bash

BACKEND_TARGET=${1:-all}
FRONTEND_TARGET=${2:-all}
SCOPE=${3:-both}

DB_HOST="localhost"
DB_PORT=5200
MAX_RETRIES=5
WAIT_SECONDS=2

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
GRADLE_CMD="$ROOT_DIR/api/gradlew"

check_db_up() {
  local attempt=1
  while ! nc -z "$DB_HOST" "$DB_PORT" >/dev/null 2>&1; do
    if [ "$attempt" -ge $MAX_RETRIES ]; then
      echo "Banco de dados não ativo em $DB_HOST:$DB_PORT após $MAX_RETRIES tentativas."
      return 1
    fi
    attempt=$((attempt + 1))
    sleep $WAIT_SECONDS
  done
}

run_command() {
  local cmd="$1"
  echo "$cmd"
  eval "$cmd"
}

build_backend_cmd() {
  local target=${1:-all}
  if [ "$target" = "all" ]; then
    echo "cd $ROOT_DIR/api && $GRADLE_CMD bootRun"
  else
    echo "cd $ROOT_DIR/api && $GRADLE_CMD :$target:bootRun"
  fi
}

build_frontend_cmd() {
  local target=${1:-all}
  if [ "$target" = "all" ]; then
    echo "pnpm -r --filter \"./apps/*\" dev"
  else
    echo "cd $ROOT_DIR/apps/$target && pnpm dev"
  fi
}

run_backend() {
  check_db_up || exit 1
  run_command "$(build_backend_cmd "$BACKEND_TARGET")"
}

run_frontend() {
  run_command "$(build_frontend_cmd "$FRONTEND_TARGET")"
}

run_both() {
  check_db_up || exit 1
  local backend_cmd frontend_cmd
  backend_cmd=$(build_backend_cmd "$BACKEND_TARGET")
  frontend_cmd=$(build_frontend_cmd "$FRONTEND_TARGET")
  echo "Rodando backend (${BACKEND_TARGET}) + frontend (${FRONTEND_TARGET})..."
  npx concurrently -k -n backend,frontend -c red,blue "$backend_cmd" "$frontend_cmd"
}

case "$SCOPE" in
  backend)  run_backend ;;
  frontend) run_frontend ;;
  both|"")  run_both ;;
  *) 
    echo "Escopo inválido: $SCOPE"
    echo "Uso: ./dev.sh [backendProject|all] [frontendProject|all] [backend|frontend|both]"
    exit 1
    ;;
esac
