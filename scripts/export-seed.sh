#!/bin/bash

TABLE=$1

if [ -z "$TABLE" ]; then
  echo "Usage:"
  echo "./scripts/export-seed.sh roles"
  exit 1
fi

mkdir -p src/main/resources/db/changelog/seed

psql \
  "$DB_URL" \
  -c "\COPY (
      SELECT *
      FROM $TABLE
      ORDER BY 1
    ) TO 'src/main/resources/db/changelog/seed/${TABLE}.csv' CSV HEADER"

echo "Exported ${TABLE}.csv"