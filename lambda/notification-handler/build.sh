#!/bin/bash
set -e

IMAGE_NAME="crediya-notification-lambda"
TAG="latest"

echo "🏗️  Construyendo imagen Docker..."
docker build -t $IMAGE_NAME:$TAG .

echo "✅ Imagen construida: $IMAGE_NAME:$TAG"
