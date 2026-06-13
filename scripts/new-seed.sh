#!/bin/bash

NAME=$1

if [ -z "$NAME" ]; then
echo "Usage:"
echo "./scripts/new-seed.sh roles"
exit 1
fi

DIR="src/main/resources/db/changelog/seed"

mkdir -p "$DIR"

FILE="$DIR/${NAME}.csv"

touch "$FILE"

echo "Created: $FILE"
echo "Remember to add CSV headers."
