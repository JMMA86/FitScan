# Usar la imagen oficial de Directus
FROM directus/directus:11.5.0

# Configurar las variables de entorno
ENV SECRET=guarrosguarrosguarros \
    ADMIN_EMAIL=admin@fitscan.com \
    ADMIN_PASSWORD=123 \
    DB_CLIENT=pg \
    DB_CONNECTION_STRING="postgres://fitscan:SE29MWOXWd8BsnX4okLEzsQEHv746TiD@dpg-d07e15qli9vc73f770dg-a.oregon-postgres.render.com:5432/fitscan?sslmode=require&rejectUnauthorized=false" \
    WEBSOCKETS_ENABLED=true \
    ACCESS_TOKEN_TTL=3600

# Exponer el puerto de Directus
EXPOSE 8055

# Inicializar las tablas de Directus si la base de datos está vacía
# RUN npx directus bootstrap

# Iniciar Directus
CMD ["npx", "directus", "start"]