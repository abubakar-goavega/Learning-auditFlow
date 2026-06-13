#!/bin/bash

set -e

CHANGELOG_DIR="src/main/resources/db/changelog"

show_help() {
cat << EOF
Liquibase Helper Script

Usage:
./scripts/liquibase.sh <command> [options]

Commands:

new <name>
Create a new migration file.

```
  Example:
  ./scripts/liquibase.sh new create-users
```

validate
Validate Liquibase changelogs.

```
  Example:
  ./scripts/liquibase.sh validate
```

review
Generate SQL without applying migrations.

```
  Example:
  ./scripts/liquibase.sh review
```

status
Show pending migrations.

```
  Example:
  ./scripts/liquibase.sh status
```

update
Apply pending migrations.

```
  Example:
  ./scripts/liquibase.sh update
```

help
Show this help message.

Examples:

./scripts/liquibase.sh new create-users
./scripts/liquibase.sh validate
./scripts/liquibase.sh review
./scripts/liquibase.sh status

EOF
}

create_migration() {

NAME=$1

if [ -z "$NAME" ]; then
echo "Migration name required."
echo "Example:"
echo "./scripts/liquibase.sh new create-users"
exit 1
fi

mkdir -p "$CHANGELOG_DIR"

LATEST=$(find "$CHANGELOG_DIR" 
-maxdepth 1 
-type f 
-name "V*.yaml" 
| sed -E 's/.*V([0-9]+).*/\1/' 
| sort -n 
| tail -1)

if [ -z "$LATEST" ]; then
NEXT=1
else
NEXT=$((10#$LATEST + 1))
fi

VERSION=$(printf "%03d" "$NEXT")

FILE="$CHANGELOG_DIR/V${VERSION}-${NAME}.yaml"

cat > "$FILE" << EOF
databaseChangeLog:

* changeSet:
  id: ${VERSION}
  author: abubakar

  changes:
  # Add changes here

EOF

echo "Created:"
echo "  $FILE"
echo ""
echo "Add to db.changelog-master.yaml:"
echo ""
echo "  - include:"
echo "      file: db/changelog/V${VERSION}-${NAME}.yaml"
}

validate() {
./gradlew liquibaseValidate
}

review() {
./gradlew updateSQL
}

status_cmd() {
./gradlew liquibaseStatus
}

update_cmd() {
./gradlew update
}

COMMAND=$1

case "$COMMAND" in

new)
create_migration "$2"
;;

validate)
validate
;;

review)
review
;;

status)
status_cmd
;;

update)
update_cmd
;;

help|--help|-h|"")
show_help
;;

*)
echo "Unknown command: $COMMAND"
echo ""
show_help
exit 1
;;

esac
