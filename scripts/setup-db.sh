#!/bin/bash

read -p "Enter PostgreSQL admin username: " PG_ADMIN
read -s -p "Enter PostgreSQL admin password: " PG_PASSWORD
echo

read -p "Enter new database name [auditflow]: " DB_NAME
DB_NAME=${DB_NAME:-auditflow}

read -p "Enter new database user [audit_admin]: " DB_USER
DB_USER=${DB_USER:-audit_admin}

read -s -p "Enter password for new database user: " DB_USER_PASSWORD
echo

export PGPASSWORD=$PG_PASSWORD

psql -U $PG_ADMIN -h localhost -c "CREATE USER $DB_USER WITH PASSWORD '$DB_USER_PASSWORD';"

psql -U $PG_ADMIN -h localhost -c "CREATE DATABASE $DB_NAME OWNER $DB_USER;"

psql -U $PG_ADMIN -h localhost -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;"

echo "Database setup completed."