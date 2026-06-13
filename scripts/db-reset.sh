#!/bin/bash

set -e

ENV=${1:-local}

ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "Environment file not found: $ENV_FILE"
  exit 1
fi

set -a
source "$ENV_FILE"
set +a

DB_NAME=$(echo "$DB_URL" | sed 's#.*/##')

echo ""
echo "Environment : $ENV"
echo "Database    : $DB_NAME"
echo ""

read -p "This will DROP and recreate database '$DB_NAME'. Continue? (y/N): " CONFIRM

if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
  echo "Cancelled."
  exit 0
fi

echo "Dropping database..."

PGPASSWORD="$DB_PASSWORD" dropdb \
  --if-exists \
  -h localhost \
  -U "$DB_USERNAME" \
  "$DB_NAME"

echo "Creating database..."

PGPASSWORD="$DB_PASSWORD" createdb \
  -h localhost \
  -U "$DB_USERNAME" \
  "$DB_NAME"

echo "Database recreated."

echo "Starting Spring Boot to run Liquibase..."

export SPRING_PROFILES_ACTIVE="$ENV"

./gradlew bootRun