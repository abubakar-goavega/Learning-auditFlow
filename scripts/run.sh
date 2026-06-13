#!/bin/bash

ENV=${1:-local}

ENV_FILE=".env.$ENV"

if [ ! -f "$ENV_FILE" ]; then
  echo "Environment file not found: $ENV_FILE"
  exit 1
fi

echo "Using environment: $ENV"

export $(grep -v '^#' "$ENV_FILE" | xargs)

export SPRING_PROFILES_ACTIVE=$ENV

./gradlew bootRun