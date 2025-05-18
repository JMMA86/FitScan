#!/bin/bash

set -a
source .env
set +a

# Check if required variables are set
if [[ -z "$DB_HOST" || -z "$DB_PORT" || -z "$DB_NAME" || -z "$DB_USER" || -z "$DB_PASSWORD" ]]; then
  echo "Error: Missing database credentials in .env file."
  exit 1
fi

# Export PGPASSWORD to avoid password prompt
export PGPASSWORD=$DB_PASSWORD

# Execute the SQL file
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "data.sql"

# Unset PGPASSWORD for security
unset PGPASSWORD