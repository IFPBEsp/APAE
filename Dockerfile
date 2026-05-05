# Frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /frontend

COPY apps/apae/package*.json ./
RUN npm ci --frozen-lockfile

COPY apps/apae/ ./

# Variáveis de ambiente de build
ARG NEXT_PUBLIC_API_URL
ENV NEXT_PUBLIC_API_URL=$NEXT_PUBLIC_API_URL

RUN npm run build

# Backend
FROM maven:3.9-eclipse-temurin-21-alpine AS backend-builder
WORKDIR /backend

COPY apps/api/pom.xml .
RUN mvn dependency:go-offline -B -q

COPY apps/api/src ./src
RUN mvn clean package -DskipTests -B -q

# Imagem de Produção
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache nodejs supervisor

WORKDIR /app

COPY --from=backend-builder /backend/target/*.jar ./backend.jar

COPY --from=frontend-builder /frontend/.next/standalone ./frontend/
COPY --from=frontend-builder /frontend/.next/static ./frontend/.next/static/
COPY --from=frontend-builder /frontend/public ./frontend/public/

COPY supervisord.conf /etc/supervisord.conf

EXPOSE 3000 8090

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]