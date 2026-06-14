#!/usr/bin/env bash
set -e

echo "WARNING: This will remove ALL Docker data:"
echo "  - All containers (running and stopped)"
echo "  - All images"
echo "  - All volumes"
echo "  - All networks (except default)"
echo "  - All build cache"
echo ""
read -p "Are you sure? (yes/no): " CONFIRM

if [ "$CONFIRM" != "yes" ]; then
  echo "Aborted."
  exit 0
fi

echo ""
echo "Stopping all containers..."
docker stop $(docker ps -aq) 2>/dev/null || true

echo "Removing all containers..."
docker rm -f $(docker ps -aq) 2>/dev/null || true

echo "Removing all images..."
docker rmi -f $(docker images -aq) 2>/dev/null || true

echo "Removing all volumes..."
docker volume rm -f $(docker volume ls -q) 2>/dev/null || true

echo "Removing all networks..."
docker network rm $(docker network ls -q --filter type=custom) 2>/dev/null || true

echo "Pruning build cache..."
docker builder prune -af 2>/dev/null || true

echo "Pruning system..."
docker system prune -af --volumes 2>/dev/null || true

echo ""
echo "Done. Docker is clean."
docker system df
