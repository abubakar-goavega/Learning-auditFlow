Good question. Many people confuse **middleware**, **filters**, **interceptors**, and **exception handlers**.

## Is GlobalExceptionHandler Middleware?

**Not exactly.**

In Spring Boot request flow:

```text
Client Request
      │
      ▼
Tomcat
      │
      ▼
Filter
      │
      ▼
Security Filters
      │
      ▼
Interceptor
      │
      ▼
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Exception?
      │
      ▼
@RestControllerAdvice
(GlobalExceptionHandler)
      │
      ▼
JSON Response
```

So `GlobalExceptionHandler` is **exception handling layer**, not middleware.

---

# What is Middleware in Spring?

Closest equivalent:

## 1. Filter

Runs before Spring MVC.

Example:

```java
public class RequestLoggingFilter extends OncePerRequestFilter
```

Use cases:

* Logging
* Correlation ID
* JWT extraction
* Rate limiting
* Request timing

Flow:

```text
Request
  ↓
Filter
  ↓
Controller
```

---

## 2. Interceptor

Runs inside Spring MVC.

Example:

```java
public class RequestInterceptor implements HandlerInterceptor
```

Use cases:

* Audit logging
* Permission checks
* Metrics

Flow:

```text
Request
 ↓
Interceptor
 ↓
Controller
```

---

# What is GlobalExceptionHandler?

Example:

```java
@GetMapping("/{id}")
public User getUser(UUID id) {

    throw new ResourceNotFoundException("User not found");
}
```

Without handler:

```text
500 Internal Server Error
Huge ugly stacktrace
```

With:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
}
```

Response becomes:

```json
{
  "success": false,
  "code": "RESOURCE_NOT_FOUND",
  "message": "User not found"
}
```

Much better.

---

# Does It Handle Validation Errors?

Yes.

Example:

```java
public record CreateUserRequest(

    @NotBlank
    String name
) {}
```

Request:

```json
{
}
```

Spring throws:

```java
MethodArgumentNotValidException
```

Your handler converts it:

```json
{
  "success": false,
  "code": "VALIDATION_ERROR",
  "message": "name is required"
}
```

---

# Does It Handle Undefined API URLs?

Example:

```text
GET /api/abc
```

when controller doesn't exist.

By default Spring returns:

```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found"
}
```

This does **NOT** go through `GlobalExceptionHandler` by default.

---

# Can We Make 404 Use Our Format?

Yes.

Add:

```yaml
spring:
  mvc:
    throw-exception-if-no-handler-found: true

  web:
    resources:
      add-mappings: false
```

Then Spring throws:

```java
NoHandlerFoundException
```

and we handle it:

```java
@ExceptionHandler(NoHandlerFoundException.class)
```

Response:

```json
{
  "success": false,
  "code": "NOT_FOUND",
  "message": "Endpoint not found"
}
```

---

# Can We Replace White Label Error Page?

Yes.

For REST APIs:

```yaml
server:
  error:
    whitelabel:
      enabled: false
```

Enterprise APIs usually disable it.

Because APIs should return JSON, not HTML.

---

# What Enterprise Applications Usually Do

They have:

```text
Filter
├── RequestId
├── Logging
├── JWT

Interceptor
├── Audit
├── Metrics

@RestControllerAdvice
├── Validation
├── Business Exceptions
├── Security Exceptions
├── 404
├── 500
```

Everything returns a standard JSON format.

---
