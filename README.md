# AuditFlow Backend

Enterprise-style backend learning project built using:

- Java 21
- Spring Boot
- PostgreSQL
- Flyway
- Docker
- Gradle Kotlin DSL

This project is focused on learning:
- backend engineering
- system design
- distributed systems concepts
- observability
- enterprise architecture
- production-ready backend practices

---

# Tech Stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3 |
| Build Tool | Gradle Kotlin DSL |
| Database | PostgreSQL |
| Migration | Flyway |
| Logging | SLF4J + Logback |
| Container | Docker |
| API Style | REST |
| Config | YAML |

---

# Project Structure

```text
auditflow/
├── .github/
│   └── workflows/
│
├── docs/
│   ├── architecture/
│   ├── api/
│   ├── database/
│   ├── deployment/
│   ├── decisions/
│   └── diagrams/
│
├── scripts/
│   ├── run.sh
│   └── setup-db.sh
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── logs/
│
├── compose.yml
├── Dockerfile
├── .env.local
├── .env.dev
├── .env.example
├── .gitignore
├── README.md
├── build.gradle.kts
└── settings.gradle.kts
```

---

# Configuration Structure

```text
src/main/resources/
├── application.yml
├── application-local.yml
├── application-dev.yml
└── application-prod.yml
```

| File | Purpose |
|---|---|
| application.yml | Shared base config |
| application-local.yml | Local development |
| application-dev.yml | Dev/Staging |
| application-prod.yml | Production |

---

# Environment Variables

## Create Local Environment File

Create:

```text
.env.local
```

Example:

```env
DB_URL=jdbc:postgresql://localhost:5432/auditflow
DB_USERNAME=audit_admin
DB_PASSWORD=replace_me

JWT_SECRET=replace_with_long_random_secret
```

---

# Important Security Notes

Never commit:
- `.env`
- passwords
- API keys
- JWT secrets
- tokens

Environment files are ignored using `.gitignore`.

---

# Running PostgreSQL

## Start Database

```bash
docker compose up -d
```

---

# Database Setup

## Create Database and User

```bash
chmod +x scripts/setup-db.sh

./scripts/setup-db.sh
```

This script:
- creates PostgreSQL user
- creates database
- grants permissions

---

# Running Backend

## Local Profile

```bash
chmod +x scripts/run.sh

./scripts/run.sh
```

Default profile:
```text
local
```

---

## Dev Profile

```bash
./scripts/run.sh dev
```

---

# How Profiles Work

The script:

1. loads environment variables from:
   - `.env.local`
   - `.env.dev`
   - `.env.prod`

2. sets:
   - `SPRING_PROFILES_ACTIVE`

3. starts Spring Boot

Example:

```bash
./scripts/run.sh dev
```

Loads:

```text
.env.dev
application-dev.yml
```

---

# Flyway Migrations

Migration files:

```text
src/main/resources/db/migration
```

Example:

```text
V1__create_users.sql
V2__create_workflows.sql
V3__add_indexes.sql
```

---

# Docker

## Start Services

```bash
docker compose up -d
```

## Stop Services

```bash
docker compose down
```

---

# Gradle Commands

## Run Application

```bash
./gradlew bootRun
```

## Build Project

```bash
./gradlew build
```

## Run Tests

```bash
./gradlew test
```

## Clean Build

```bash
./gradlew clean build
```

---

# Logging

Logs are stored in:

```text
logs/
```

Log file:

```text
logs/auditflow.log
```

---

# Recommended Development Workflow

## 1. Start Database

```bash
docker compose up -d
```

## 2. Run Backend

```bash
./scripts/run.sh
```

## 3. Run Tests

```bash
./gradlew test
```

## 4. Create Migration

```text
src/main/resources/db/migration/V4__add_notifications.sql
```

---

# Recommended Architecture

Feature-based modular structure:

```text
src/main/java/com/company/auditflow
├── auth/
├── workflow/
├── approval/
├── audit/
├── notification/
├── organization/
├── common/
├── infrastructure/
└── config/
```

Avoid large shared layers like:

```text
controller/
service/
repository/
```

at root level.

---

# Module Structure

Example:

```text
workflow/
├── api/
├── application/
├── domain/
├── infrastructure/
└── mapper/
```

| Layer | Responsibility |
|---|---|
| api | controllers + DTO |
| application | business logic |
| domain | entities + business rules |
| infrastructure | DB + external systems |
| mapper | object mapping |

---

# Planned Features

- JWT Authentication
- RBAC Authorization
- Workflow Engine
- Approval System
- Audit Logs
- Notifications
- File Uploads
- Activity Tracking
- Multi-Tenant Support
- Redis Caching
- Kafka Integration
- Observability
- Metrics & Monitoring

---

# Future Improvements

- Redis
- Kafka
- WebSockets
- OpenTelemetry
- Prometheus
- Grafana
- Elasticsearch
- Rate Limiting
- Distributed Tracing

---

# Documentation

Project documentation:

```text
docs/
```

Recommended docs:

```text
docs/architecture/system-overview.md
docs/database/schema-design.md
docs/decisions/why-flyway.md
```

---

# Git Workflow

## Branches

```text
main
develop
feature/*
bugfix/*
```

## Commit Convention

```text
feat: add workflow approval
fix: resolve JWT validation issue
refactor: improve audit logging
```

---

# Important Backend Concepts To Learn

Focus heavily on:
- transactions
- indexing
- concurrency
- thread pools
- connection pools
- caching
- DB optimization
- security
- observability
- Docker networking

These matter more than framework syntax.

---

# License

Learning project for backend engineering and system design practice.

#https://www.gupshup.ai/sms-api