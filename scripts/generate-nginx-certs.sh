#!/usr/bin/env bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
CERT_DIR="$PROJECT_ROOT/nginx/certs"

mkdir -p "$CERT_DIR"

echo "Generating private key..."
openssl genrsa -out "$CERT_DIR/nginx.key" 2048

echo "Generating self-signed certificate..."
openssl req \
  -new \
  -x509 \
  -key "$CERT_DIR/nginx.key" \
  -out "$CERT_DIR/nginx.crt" \
  -days 365 \
  -subj "/C=IN/ST=Karnataka/L=Mysore/O=AuditFlow/OU=IT/CN=localhost"

chmod 600 "$CERT_DIR/nginx.key"

echo
echo "Certificate generated:"
echo "  $CERT_DIR/nginx.crt"
echo "  $CERT_DIR/nginx.key"