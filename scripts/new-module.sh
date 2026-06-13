#!/bin/bash

set -e

MODULE=$1
BASE_PATH=${2:-src/main/java}
PACKAGE_NAME=${3:-com.abu.auditflow}

if [ -z "$MODULE" ]; then
    echo "Usage:"
    echo "./scripts/new-module.sh <module-name>"
    echo ""
    echo "Examples:"
    echo "./scripts/new-module.sh customer"
    echo "./scripts/new-module.sh department"
    echo "./scripts/new-module.sh audit"
    exit 1
fi

PACKAGE_PATH=$(echo "$PACKAGE_NAME" | tr '.' '/')

ROOT="$BASE_PATH/$PACKAGE_PATH/$MODULE"

if [ -d "$ROOT" ]; then
    echo "Module already exists:"
    echo "$ROOT"
    exit 1
fi

echo "Creating module: $MODULE"

mkdir -p "$ROOT/controller"
mkdir -p "$ROOT/service"
mkdir -p "$ROOT/repository"
mkdir -p "$ROOT/entity"
mkdir -p "$ROOT/dto"
mkdir -p "$ROOT/mapper"

CLASS_NAME="$(tr '[:lower:]' '[:upper:]' <<< ${MODULE:0:1})${MODULE:1}"

cat > "$ROOT/controller/${CLASS_NAME}Controller.java" <<EOF
package ${PACKAGE_NAME}.${MODULE}.controller;

public class ${CLASS_NAME}Controller {
}
EOF

cat > "$ROOT/service/${CLASS_NAME}Service.java" <<EOF
package ${PACKAGE_NAME}.${MODULE}.service;

public class ${CLASS_NAME}Service {
}
EOF

cat > "$ROOT/repository/${CLASS_NAME}Repository.java" <<EOF
package ${PACKAGE_NAME}.${MODULE}.repository;

public interface ${CLASS_NAME}Repository {
}
EOF

cat > "$ROOT/entity/${CLASS_NAME}.java" <<EOF
package ${PACKAGE_NAME}.${MODULE}.entity;

public class ${CLASS_NAME} {
}
EOF

echo ""
echo "Module created:"
echo "$ROOT"