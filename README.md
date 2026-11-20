# Veterinaria San Marcos — Backend

API REST para la veterinaria, construida con **Spring Boot 4.1.1 / Java 21**. Gestiona usuarios, servicios, productos y medicamentos/vacunas, con autenticación JWT por roles y documentación OpenAPI.

## Stack

| Tecnología | Uso |
|---|---|
| Spring Boot 4.1.1 / Java 21 | Base de la aplicación |
| Spring Data JPA + Hibernate | Modelo de datos y repositorios |
| H2 (por defecto) / MySQL 8 (Docker) | Base de datos |
| Spring Security + JJWT 0.12.6 | Autenticación JWT y roles |
| springdoc-openapi 3.2.0 | Swagger UI y `/v3/api-docs` |
| Bean Validation | Validación de DTOs |
| Maven Wrapper | Build y pruebas |

## Requisitos y ejecución

- JDK 21 o superior (el wrapper descarga Maven).
- Opcional: Docker + Docker Compose para usar MySQL.

```bash
.\mvnw.cmd spring-boot:run     # Windows
./mvnw spring-boot:run         # Linux/Mac
```

La API queda en `http://localhost:8080`. Si `java` no está en el `PATH`, puedes apuntar `JAVA_HOME` a tu JDK antes de ejecutar el wrapper.

## Configuración

Todos los valores tienen un default en `src/main/resources/application.properties` y pueden sobreescribirse por variables de entorno:

| Variable | Default | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:h2:mem:veterinariadb` | URL JDBC |
| `DB_DRIVER` | `org.h2.Driver` | Driver JDBC |
| `DB_USER` | `sa` | Usuario de la BD |
| `DB_PASSWORD` | *(vacío)* | Contraseña de la BD |
| `DB_DIALECT` | `org.hibernate.dialect.H2Dialect` | Dialecto de Hibernate |
| `DDL_AUTO` | `update` | Estrategia de esquema |
| `JWT_SECRET` | valor Base64 de 32 bytes en el properties | Clave HMAC (mínimo 256 bits) |
| `JWT_EXPIRATION_MS` | `3600000` | Vigencia del token (1 hora) |
| `CORS_ALLOWED_ORIGINS` | localhost:3000/5173/8080/4173 y `https://andres-albornoz.github.io` | Orígenes permitidos |

La consola H2 está habilitada en `/h2-console`. En producción usa `JWT_SECRET` propio y una BD persistente.

## Datos iniciales

`DataSeeder` crea, si no existen:

- Usuarios `admin@admin.cl` / `admin123` (ADMIN) y `user@user.cl` / `user123` (USER).
- 3 productos de ejemplo.
- Catálogo de **32 servicios** (consultas, vacunación, cirugía, desparasitación, exámenes y otros).
- **22 medicamentos y vacunas** con código `ME001`–`ME022`, categoria, principio activo, presentación, especie, stock y precio.

El seeder es idempotente: solo inserta cuando la tabla correspondiente está vacía.

## Autenticación y seguridad

- `POST /api/auth/login` devuelve `{ token, email, roles }`.
- El JWT lleva el subject (email), los roles en el claim `roles`, `iat` y `exp`; se firma con HMAC-SHA256.
- `JwtAuthFilter` valida firma y expiración en cada petición `Bearer <token>` y recarga el usuario desde la BD.
- Las contraseñas se guardan con BCrypt.
- La sesión es stateless; CSRF no aplica porque no se usan cookies.
- `@EnableMethodSecurity` + `@PreAuthorize("hasRole('ADMIN')")` protegen las operaciones de escritura.
- Respuestas: `401` sin token o token inválido, `403` con rol insuficiente.

## Endpoints

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/api/auth/register` | Público | Registrar usuario (siempre rol USER) |
| POST | `/api/auth/login` | Público | Obtener JWT |
| GET | `/api/users`, `/api/users/{id}` | ADMIN | Listar / buscar usuarios |
| PUT, DELETE | `/api/users/{id}` | ADMIN | Actualizar / eliminar usuario |
| GET | `/api/productos`, `/api/productos/{id}` | Autenticado | Listar (filtro `?nombre=`) / buscar |
| POST, PUT, DELETE | `/api/productos[/{id}]` | ADMIN | CRUD de productos |
| GET | `/api/medicamentos`, `/api/medicamentos/{codigo}` | Autenticado | Listar (filtro `?categoria=`) / buscar |
| POST, PUT, DELETE | `/api/medicamentos[/{codigo}]` | ADMIN | CRUD de medicamentos |
| GET | `/api/servicios`, `/api/servicios/{id}` | Autenticado | Listar (filtro `?categoria=`) / buscar |
| POST, PUT, DELETE | `/api/servicios[/{id}]` | ADMIN | CRUD de servicios |

Alias disponibles por compatibilidad: `/api/medicamentos-vacunas/**` y `/api/service/**`.

## Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui/index.html`
- Especificación: `http://localhost:8080/v3/api-docs` (15 paths)
- Acceso público para que se puedan inspeccionar los endpoints sin token.

Para probar rutas protegidas desde Swagger: haz login, copia el `token`, pulsa **Authorize** y pega el valor. El esquema `bearerAuth` envía el header `Authorization: Bearer <token>` automáticamente. Los DTOs y respuestas están etiquetados con `@Tag` y `@Operation`.

## Pruebas

```bash
.\mvnw.cmd test
```

11 pruebas, entre ellas `ApiFlowTest`, que cubre flujo completo de login/registro, CRUD de servicios, productos, medicamentos y usuarios, validaciones y controles de acceso (`401`/`403`).

## Docker

```bash
# Copia .env.example a .env y ajusta DB_NAME/DB_USER/DB_PASSWORD/DB_ROOT_PASSWORD
docker compose up --build
```

`docker-compose.yml` levanta MySQL 8 con healthcheck y el backend en `${BACKEND_PORT}:8080`. El `src/Dockerfile` compila con Maven y ejecuta el JAR sobre Temurin 21.

## Relación con el frontend

El proyecto `VETERINARIA_SAN_MARCOS.github.io` consume esta API. En local usa el proxy de Vite (`/api` → `:8080`); en GitHub Pages define `VITE_API_URL` con la URL pública de este backend. El origen de Pages ya está permitido en CORS.
