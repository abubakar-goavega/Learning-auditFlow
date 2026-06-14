# ==================================================
# Build Stage
# ==================================================
FROM gradle:8.14-jdk21 AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew

# Download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew clean bootJar --no-daemon

# ==================================================
# Runtime Stage
# ==================================================
FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.title="auditflow"
LABEL org.opencontainers.image.description="AuditFlow API"
LABEL org.opencontainers.image.version="1.0.0"

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75.0 \
-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]