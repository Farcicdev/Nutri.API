#!/usr/bin/env bash

  set -Eeuo pipefail

  PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
  cd "$PROJECT_DIR"

  if ! command -v docker >/dev/null 2>&1; then
    echo "Erro: Docker não está instalado."
    exit 1
  fi

  if ! docker compose version >/dev/null 2>&1; then
    echo "Erro: Docker Compose não está disponível."
    exit 1
  fi

  if ! docker info >/dev/null 2>&1; then
    echo "Erro: o Docker não está iniciado ou o usuário não possui permissão."
    exit 1
  fi

  if [ ! -f ".env" ]; then
    echo "Criando .env a partir do .env.example..."

    if [ ! -f ".env.example" ]; then
      echo "Erro: arquivo .env.example não encontrado."
      exit 1
    fi

    if ! command -v openssl >/dev/null 2>&1; then
      echo "Erro: OpenSSL não está instalado e é necessário para gerar o JWT_SECRET."
      exit 1
    fi

    cp .env.example .env

    JWT_SECRET_GENERATED="$(openssl rand -base64 32)"

    sed -i \
      "s|^JWT_SECRET=.*$|JWT_SECRET=${JWT_SECRET_GENERATED}|" \
      .env

    echo "Arquivo .env criado com um JWT_SECRET seguro."
  else
    echo "Usando o arquivo .env existente."
  fi

  if ! grep -Eq '^JWT_SECRET=.+$' .env; then
    echo "Erro: JWT_SECRET não está definido no arquivo .env."
    exit 1
  fi

  echo "Validando o Docker Compose..."
  docker compose config --quiet

  echo "Construindo e iniciando a API e o PostgreSQL..."
  docker compose up --build --detach

  echo
  echo "Containers iniciados:"
  docker compose ps

  echo
  echo "Backend disponível em:"
  echo "http://localhost:8083/api"
  echo
  echo "Para acompanhar os logs:"
  echo "docker compose logs -f api"
  echo
  echo "Para encerrar:"
  echo "docker compose down"
