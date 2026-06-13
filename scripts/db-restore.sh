#!/bin/bash

set -e

ENV=${1:-local}
BACKUP_FILE=$2

if [ -z "$BACKUP_FILE" ]; then
  echo "Usage:"
  echo "./scripts/db-restore.sh local backups/file.backup"
  exit 1
fi

ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "Missing $ENV_FILE"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

pg_restore \
  --clean \
  --if-exists \
  --dbname="$DB_URL" \
  "$BACKUP_FILE"