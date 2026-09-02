# syntax=docker/dockerfile:1.7

FROM node:20-alpine@sha256:fb4cd12c85ee03686f6af5362a0b0d56d50c58a04632e6c0fb8363f609372293 AS builder

WORKDIR /app

COPY package.json ./
COPY package-lock.json* ./

RUN if [ -f package-lock.json ]; then \
      npm ci; \
    else \
      npm install --no-audit --no-fund; \
    fi

COPY . .

ARG NEXT_PUBLIC_API_URL=/apae-geral/api

ENV NEXT_PUBLIC_API_URL=$NEXT_PUBLIC_API_URL
ENV NEXT_TELEMETRY_DISABLED=1

RUN npm run build


FROM gcr.io/distroless/nodejs20-debian13:nonroot@sha256:c8da1b6cccb5c6cc4b8826c67353f31c5f7b2719c5517b1312d191b337d8bd99 AS runner

ARG APP_VERSION
ARG VCS_REF

LABEL org.opencontainers.image.title="APAE Geral Frontend" \
      org.opencontainers.image.description="Frontend do sistema APAE" \
      org.opencontainers.image.source="https://github.com/IFPBEsp/APAE" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.version=$APP_VERSION \
      org.opencontainers.image.revision=$VCS_REF

WORKDIR /app

ENV NODE_ENV=production
ENV NEXT_TELEMETRY_DISABLED=1
ENV PORT=3000
ENV HOSTNAME=0.0.0.0

COPY --from=builder --chown=65532:65532 /app/public ./public
COPY --from=builder --chown=65532:65532 /app/.next/standalone ./
COPY --from=builder --chown=65532:65532 /app/.next/static ./.next/static

USER 65532:65532

EXPOSE 3000

CMD ["server.js"]