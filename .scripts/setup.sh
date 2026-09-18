#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$ROOT_DIR"

echo "Preparando PostgreSQL local..."
docker compose up -d db

echo "Aplicando migrations Flyway do schema apae_geral..."
docker compose up db-migrate

echo "Inserindo dados fictícios de desenvolvimento..."
docker compose --profile tools run --rm db-seed

echo "Banco local preparado com migrations e seed fictício."
