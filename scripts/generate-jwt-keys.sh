#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
KEY_DIR="$PROJECT_ROOT/src/main/resources/keys"

mkdir -p "$KEY_DIR"

echo "Generating RSA private key..."
openssl genrsa -out "$KEY_DIR/private.pem" 2048

echo "Generating RSA public key..."
openssl rsa \
  -in "$KEY_DIR/private.pem" \
  -pubout \
  -out "$KEY_DIR/public.pem"

chmod 600 "$KEY_DIR/private.pem"

echo
echo "JWT keys generated:"
echo "  $KEY_DIR/private.pem"
echo "  $KEY_DIR/public.pem"