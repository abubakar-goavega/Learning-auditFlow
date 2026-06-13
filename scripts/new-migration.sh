#!/bin/bash

NAME=$1

if [ -z "$NAME" ]; then
    echo "Usage:"
    echo "./scripts/new-migration.sh create-users"
    exit 1
fi

DIR="src/main/resources/db/changelog/migrations"

mkdir -p "$DIR"

TIMESTAMP=$(date +"%Y%m%d-%H%M%S")

FILE="$DIR/${TIMESTAMP}-${NAME}.yaml"

CHANGESET_ID=$(date +"%Y%m%d%H%M%S")

cat > "$FILE" <<EOF
databaseChangeLog:

  - changeSet:
      id: ${CHANGESET_ID}
      author: abubakar

      changes:

        # Add changes here

EOF

echo "Created:"
echo "$FILE"