# Spring Boot Backend Project Blueprint

## Tech Stack

| Area | Choice |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.x |
| Build Tool | Gradle Kotlin DSL |
| Database | PostgreSQL |
| Migration | Flyway |
| Config | YAML |
| Container | Docker |
| IDE | VS Code / IntelliJ |
| Logging | SLF4J + Logback |

---

# Enterprise Project Structure

```text
workflow-platform/
├── .github/
│   └── workflows/
│
├── .vscode/
│
├── docs/
│   ├── architecture/
│   ├── api/
│   ├── database/
│   ├── deployment/
│   ├── decisions/
│   └── diagrams/
│
├── docker/
│   ├── local/
│   ├── dev/
│   └── prod/
│
├── scripts/
│
├── src/
│   ├── main/
│   └── test/
│
├── compose.yml
├── Dockerfile
├── .env
├── .env.example
├── README.md
├── build.gradle.kts
└── settings.gradle.kts
```

---

# Spring Boot Configuration Structure

```text
src/main/resources/
├── application.yml
├── application-local.yml
├── application-dev.yml
└── application-prod.yml
```

## Purpose

| File | Purpose |
|---|---|
| application.yml | Common shared config |
| application-local.yml | Local development |
| application-dev.yml | Dev/Staging |
| application-prod.yml | Production |

---

# application.yml

```yaml
spring:
  application:
    name: workflow-platform

server:
  shutdown: graceful

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

---

# application-local.yml

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: DEBUG

  file:
    name: logs/workflow-platform.log
```

---

# Environment Variables

## .env

```env
DB_URL=jdbc:postgresql://localhost:5432/workflow_platform
DB_USERNAME=workflow_admin
DB_PASSWORD=localdev123

JWT_SECRET=replace_with_long_secret
```

---

# Important

Never commit:

- .env
- secrets
- API keys
- tokens

---

# .gitignore

```gitignore
.env
logs/
build/
.gradle/
.idea/
.vscode/
```

---

# Running Spring Profile

## Local

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Dev

```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

---

# VS Code launch.json

```json
{
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot",
      "request": "launch",
      "mainClass": "com.example.Application",
      "envFile": "${workspaceFolder}/.env",
      "env": {
        "SPRING_PROFILES_ACTIVE": "local"
      }
    }
  ]
}
```

---

# Docker Compose

## compose.yml

```yaml
services:
  postgres:
    image: postgres:17

    container_name: workflow-postgres

    environment:
      POSTGRES_DB: workflow_platform
      POSTGRES_USER: workflow_admin
      POSTGRES_PASSWORD: localdev123

    ports:
      - "5432:5432"

    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

---

# Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

# Flyway Migration Structure

```text
src/main/resources/db/migration
```

## Example Files

```text
V1__create_users.sql
V2__create_workflows.sql
V3__add_indexes.sql
```

---

# Recommended Database Tables

| Table | Purpose |
|---|---|
| organizations | Multi tenancy |
| users | User accounts |
| roles | RBAC |
| permissions | Authorization |
| workflows | Main workflow |
| workflow_steps | Approval stages |
| comments | Discussions |
| attachments | File uploads |
| notifications | Alerts |
| audit_logs | Activity tracking |

---

# Enterprise Java Structure

```text
src/main/java/com/company/workflowplatform
├── auth/
├── identity/
├── workflow/
├── approval/
├── notification/
├── audit/
├── organization/
├── common/
├── infrastructure/
└── config/
```

---

# Inside Module Structure

```text
workflow/
├── api/
├── application/
├── domain/
├── infrastructure/
└── mapper/
```

---

# Layer Responsibility

| Layer | Responsibility |
|---|---|
| api | controllers + DTO |
| application | business logic |
| domain | entities + rules |
| infrastructure | database/external systems |
| mapper | conversions |

---

# Logging Best Practices

## Good

```java
log.info("Workflow approved workflowId={} userId={}", workflowId, userId);
```

## Avoid

```java
log.info("approved");
```

## Never Log

- passwords
- JWT tokens
- API keys
- secrets

---

# GitHub Actions

```text
.github/workflows/backend-ci.yml
```

## CI Pipeline

- build
- test
- lint
- docker build

---

# Documentation Structure

```text
docs/
├── architecture/
├── api/
├── database/
├── deployment/
├── decisions/
└── diagrams/
```

## Example Docs

```text
docs/architecture/auth-system.md
docs/database/indexing-strategy.md
docs/decisions/why-postgresql.md
```

---

# Recommended Development Commands

## Start Database

```bash
docker compose up -d
```

## Run Spring Boot

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Build

```bash
./gradlew build
```

## Run Tests

```bash
./gradlew test
```

## Docker Build

```bash
docker build -t workflow-platform .
```

---

# Recommended Learning Roadmap

## Phase 1

- REST APIs
- PostgreSQL
- Flyway
- JWT
- Docker
- Logging

## Phase 2

- Redis
- caching
- async processing
- WebSockets
- pagination

## Phase 3

- Kafka
- observability
- tracing
- rate limiting
- distributed systems

---

# Most Important Backend Concepts

Focus heavily on:

- transactions
- indexing
- concurrency
- thread management
- caching
- DB optimization
- observability
- API design
- security
- Docker networking

These matter more than framework syntax.
