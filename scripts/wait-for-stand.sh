#!/usr/bin/env bash
# ПУ-1: ждать готовности стенда. Healthcheck контейнеров не используется (засчитывает 302 во время настройки),
# готовность — ответ GET /session в формате JSON с полем "authenticated".
# Использование: scripts/wait-for-stand.sh [url] [таймаут в секундах]
set -euo pipefail
URL="${1:-http://localhost/openmrs/ws/rest/v1/session}"
TIMEOUT="${2:-1200}"
START=$(date +%s)
until curl -sf "$URL" | grep -q '"authenticated"'; do
  if (( $(date +%s) - START > TIMEOUT )); then
    echo "Stand is not ready after ${TIMEOUT}s: $URL" >&2
    exit 1
  fi
  sleep 10
done
echo "Stand is ready in $(( $(date +%s) - START ))s"
