# syntax=docker/dockerfile:1.7

# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /workspace

COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -Dmaven.test.skip=true \
 && cp target/*.jar /workspace/app.jar

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

RUN apk add --no-cache wget \
 && addgroup -S -g 1001 appgroup \
 && adduser  -S -u 1001 -G appgroup appuser

COPY --from=builder --chown=appuser:appgroup /workspace/app.jar ./app.jar

ENV API_PORT=8080
ENV JAVA_OPTS=""

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8080/api/actuator/health >/dev/null 2>&1 || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
