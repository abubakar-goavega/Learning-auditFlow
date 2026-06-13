#!/bin/bash

set -e

BASE_PATH=$1
PACKAGE_NAME=$2

if [ -z "$BASE_PATH" ] || [ -z "$PACKAGE_NAME" ]; then
    echo "Usage:"
    echo "./scripts/init-project-structure.sh <src/main/java path> <package>"
    echo ""
    echo "Example:"
    echo "./scripts/init-project-structure.sh src/main/java com.abu.auditflow"
    exit 1
fi

PACKAGE_PATH=$(echo "$PACKAGE_NAME" | tr '.' '/')

ROOT="$BASE_PATH/$PACKAGE_PATH"

echo "Creating project structure..."
echo "Root: $ROOT"

mkdir -p "$ROOT"

# -----------------------------
# Core
# -----------------------------

mkdir -p "$ROOT/config"
mkdir -p "$ROOT/security"
mkdir -p "$ROOT/security/jwt"
mkdir -p "$ROOT/security/service"

mkdir -p "$ROOT/exception"

mkdir -p "$ROOT/audit"

mkdir -p "$ROOT/common/constants"
mkdir -p "$ROOT/common/util"
mkdir -p "$ROOT/common/dto"

mkdir -p "$ROOT/integration"

mkdir -p "$ROOT/scheduler"

mkdir -p "$ROOT/notification"

# -----------------------------
# User Module
# -----------------------------

mkdir -p "$ROOT/user/controller"
mkdir -p "$ROOT/user/service"
mkdir -p "$ROOT/user/repository"
mkdir -p "$ROOT/user/entity"
mkdir -p "$ROOT/user/dto"
mkdir -p "$ROOT/user/mapper"

# -----------------------------
# Role Module
# -----------------------------

mkdir -p "$ROOT/role/controller"
mkdir -p "$ROOT/role/service"
mkdir -p "$ROOT/role/repository"
mkdir -p "$ROOT/role/entity"
mkdir -p "$ROOT/role/dto"

# -----------------------------
# Permission Module
# -----------------------------

mkdir -p "$ROOT/permission/controller"
mkdir -p "$ROOT/permission/service"
mkdir -p "$ROOT/permission/repository"
mkdir -p "$ROOT/permission/entity"
mkdir -p "$ROOT/permission/dto"

# -----------------------------
# Resources
# -----------------------------

mkdir -p src/main/resources/db/changelog/migrations
mkdir -p src/main/resources/db/changelog/seed

mkdir -p docs

mkdir -p scripts

echo "Done."