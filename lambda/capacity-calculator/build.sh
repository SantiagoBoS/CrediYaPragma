#!/bin/bash
set -e

IMAGE_NAME="crediya-capacity-calculator"
TAG="latest"
SERVICE_DESC="Capacity Calculator"

echo "Construyendo imagen Docker para $SERVICE_DESC..."
docker build -t $IMAGE_NAME:$TAG .

echo "Imagen construida: $IMAGE_NAME:$TAG"
echo "Para ejecutar localmente: docker run -p 9000:8080 $IMAGE_NAME:$TAG"