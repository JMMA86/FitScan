#!/bin/bash
if [[ $1 == "reset" ]]; then
  docker compose down --rmi all --volumes
  docker compose up -d
fi

CONTAINER_NAME="fitscan-db-1"
SQL_FILE="data.sql"
DB_NAME="directus"
DB_USER="directus"

# Wait for the container to be running
while ! docker ps --filter "name=$CONTAINER_NAME" --filter "status=running" | grep -q "$CONTAINER_NAME"; do
  echo "Waiting for container $CONTAINER_NAME to be running..."
  sleep 2
done

while ! docker exec -i "$CONTAINER_NAME" bash -c "psql -U $DB_USER -d $DB_NAME -c '\dt'" | grep -q "directus_users"; do
  echo "Waiting for directus_users table to be created..."
  sleep 2
done

docker cp "$SQL_FILE" "$CONTAINER_NAME:/tmp/data.sql"
docker exec -i "$CONTAINER_NAME" bash -c "psql -U $DB_USER -d $DB_NAME -f /tmp/data.sql"
