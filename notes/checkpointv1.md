Agreed. Going forward:

### Comment Style

Single line:

```java
// Public endpoint used for health checks.
@GetMapping("/health")
```

Multi-line:

```java
/*
 * Method security example.
 * Demonstrates role-based authorization.
 */
@PreAuthorize("hasRole('ADMIN')")
```

Avoid:

```java
// line 1
// line 2
// line 3
// line 4
```

unless it is truly a one-line note.

---

# Current Project Status

Goal:

```text
Enterprise Spring Boot Template
+
Hands-on Learning Project
+
Reusable Starter Template
```

Not building AuditFlow yet.

We are building a generic template first.

---

# Phase 0 - Foundation

Status: Completed

Topics learned:

```text
Spring Initializr
Gradle
Java 21
Profiles
Configuration
Environment Variables
PostgreSQL
Liquibase
Docker Compose
Actuator
Swagger/OpenAPI
Global Exception Handling
API Response Standardization
Project Structure
```

Current modules:

```text
config
common
exception
root
security
```

---

# Phase 1 - Security Fundamentals

Status: In Progress

Completed:

```text
Spring Security
SecurityFilterChain
HTTP Basic Authentication
Authentication
Authorization
Roles
Authorities
SecurityContext
Method Security
@EnableMethodSecurity
@PreAuthorize
Authentication Injection
Protected vs Public Endpoints
```

Current endpoints:

```text
/
/public
/about
/admin
/me
/security-info
```

Current users:

```text
admin/admin123
user/user123
```

Current authentication source:

```text
InMemoryUserDetailsManager
```

Temporary only.

---

# Phase 2 - Database Authentication

Status: Planned

Topics:

```text
User Entity
Role Entity
Permission Entity

users
roles
permissions
user_roles
role_permissions
```

Spring Security concepts:

```text
UserDetails
UserDetailsService
GrantedAuthority
AuthenticationProvider
PasswordEncoder
```

Outcome:

```text
Users loaded from PostgreSQL
instead of memory.
```

---

# Phase 3 - JWT Authentication

Status: Planned

Topics:

```text
JWT
Access Token
Refresh Token
Token Expiry
Token Validation
JWT Filter
Stateless Authentication
```

Spring concepts:

```text
OncePerRequestFilter
JwtAuthenticationFilter
SecurityContextHolder
```

Outcome:

```text
REST API Authentication
without sessions.
```

---

# Phase 4 - Authorization System

Status: Planned

Topics:

```text
RBAC
Role Based Access Control

PBAC
Permission Based Access Control
```

Examples:

```text
USER_CREATE
USER_UPDATE
USER_DELETE

AUDIT_CREATE
AUDIT_APPROVE
AUDIT_VIEW
```

Annotations:

```java
@PreAuthorize("hasAuthority('USER_CREATE')")
```

---

# Phase 5 - Auditing

Status: Planned

Topics:

```text
created_by
created_at

updated_by
updated_at
```

Spring concepts:

```text
AuditorAware
JPA Auditing
SecurityContext
```

Outcome:

```text
Know who changed what.
```

---

# Phase 6 - Logging

Status: Planned

Topics:

```text
SLF4J
Logback
Structured Logging
Correlation ID
Request Logging
Audit Logging
```

Tools:

```text
MDC
Logback
Actuator
```

---

# Phase 7 - Validation & Error Handling

Status: Planned

Topics:

```text
Bean Validation
Custom Validation
Exception Mapping
Error Standards
```

Annotations:

```java
@NotNull
@NotBlank
@Email
@Pattern
```

---

# Phase 8 - Testing

Status: Planned

Topics:

```text
JUnit
Mockito
MockMvc
Integration Testing
Testcontainers
```

Coverage:

```text
Service Tests
Controller Tests
Repository Tests
Security Tests
```

---

# Phase 9 - API Design

Status: Planned

Topics:

```text
DTO
Mapper
Pagination
Sorting
Filtering
Versioning
```

---

# Phase 10 - Caching

Status: Planned

Topics:

```text
Redis
Spring Cache
Cache Eviction
```

---

# Phase 11 - Async Processing

Status: Planned

Topics:

```text
@Async
Thread Pools
Scheduling
```

---

# Phase 12 - Messaging

Status: Planned

Topics:

```text
Kafka
Event Driven Design
Retry
Dead Letter Queue
```

---

# Phase 13 - File Storage

Status: Planned

Topics:

```text
Multipart Upload
S3
MinIO
File Validation
```

---

# Phase 14 - Observability

Status: Planned

Topics:

```text
Actuator
Micrometer
Prometheus
Grafana
Health Checks
Metrics
Tracing
```

---

# Phase 15 - Containerization

Status: Planned

Topics:

```text
Dockerfile
Docker Compose
Multi-stage Build
Image Optimization
```

---

# Phase 16 - CI/CD

Status: Planned

Topics:

```text
GitHub Actions
Build
Test
Package
Docker Build
Release
```

---

# Phase 17 - Infrastructure

Status: Planned

Topics:

```text
Nginx
Reverse Proxy
Load Balancer
API Gateway
SSL/TLS
Rate Limiting
```

Docker labs:

```text
Client
  |
Nginx
  |
Spring Boot
```

Later:

```text
Client
  |
Nginx
  |
Multiple Spring Boot Containers
```

Load balancing:

```text
Nginx
  ├── App-1
  ├── App-2
  └── App-3
```

---

# Phase 18 - Architecture

Status: Planned

Topics:

```text
Monolith
Modular Monolith
Microservices

Feature-Based Structure
Layered Structure

Hexagonal Architecture
Clean Architecture
```

---

# Immediate Next Step

We are here:

```text
Phase 1 - Security Fundamentals
```

Next implementation:

```text
Database Authentication
```

Before writing code, I recommend creating the security schema:

```text
users
roles
permissions
user_roles
role_permissions
```

using Liquibase.

That is the next major learning milestone because it introduces both Spring Security and JPA together in a realistic enterprise setup.

Documentation Note:

Create:

```text
docs/security/security-fundamentals.md
```

and record:

```text
Authentication
Authorization
Roles
Authorities
SecurityContext
SecurityFilterChain
Method Security
```

These concepts will be referenced repeatedly in later phases (JWT, Auditing, API Security, and Microservices).
