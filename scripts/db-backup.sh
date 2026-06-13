#!/bin/bash

set -e

ENV=${1:-local}

ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "Missing $ENV_FILE"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

mkdir -p backups

TIMESTAMP=$(date +"%Y%m%d_%H%M%S")

FILE="backups/auditflow_${ENV}_${TIMESTAMP}.backup"

pg_dump \
  --dbname="$DB_URL" \
  --format=custom \
  --file="$FILE"

echo ""
echo "Backup created:"
echo "$FILE"