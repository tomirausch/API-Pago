# API de Pagos

API REST para la gestión de pagos, desarrollada con Java y Spring Boot, utilizando arquitectura hexagonal para separar la lógica de negocio de los detalles de infraestructura.

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java JDK 21**
- **Apache Maven 3.8.0 o superior**
- **PostgreSQL**, si vas a utilizar la base de datos configurada para la aplicación.

Verifica las instalaciones con:

```bash
java -version
mvn -version
```

## Configuración

La aplicación utiliza variables de entorno para configurar la conexión a la base de datos:

| Variable | Descripción |
|---|---|
| `APP_DATASOURCE_URL` | URL de conexión a la base de datos |
| `APP_USER` | Usuario de la base de datos |
| `APP_PASSWORD` | Contraseña de la base de datos |
| `JWT_SECRET_KEY` | Clave para firmar los tokens JWT (mínimo 32 caracteres) |

Configura estas variables en tu entorno de ejecución antes de iniciar la aplicación. No utilices credenciales reales en archivos públicos ni las subas al repositorio.

## Instalación y ejecución

Clona el repositorio y accede a la carpeta del proyecto:

```bash
git clone https://github.com/tomirausch/API-Pago.git
cd API-Pago
```

### Compilar el proyecto

```bash
mvn clean install
```

### Ejecutar los tests

```bash
mvn test
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

También puedes generar el archivo JAR con:

```bash
mvn clean package
```

Luego, ejecuta el JAR generado dentro de la carpeta `target`, utilizando el nombre correspondiente al artefacto configurado en Maven.

## Endpoints

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Devuelve un JWT | No |
| `POST` | `/api/v1/payments` | Crea un pago (idempotente) | Bearer |
| `GET` | `/api/v1/payments/{id}` | Consulta un pago por id | Bearer |
| `GET` | `/api/v1/payments/idempotency/{key}` | Consulta por clave de idempotencia | Bearer |
| `PATCH` | `/api/v1/payments/{id}/status` | Cambia el estado (`COMPLETED` o `FAILED`) | Bearer |

### Ejemplo

```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"<usuario>","password":"<contraseña>"}'

# Crear un pago
curl -X POST http://localhost:8080/api/v1/payments \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"idempotencyKey":"orden-1001","amount":1500.50,"currency":"ARS"}'
```

Si repetís la misma `idempotencyKey`, la API devuelve el pago existente (`200`) en lugar de crear otro (`201`).

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Security y JWT
- Spring Data JPA
- PostgreSQL
- Maven
- Spring Boot Actuator y Micrometer

## Estructura del proyecto

- `src/main/java`: código fuente de la aplicación.
- `src/main/resources`: archivos de configuración.
- `src/test/java`: pruebas unitarias y de integración.
- `pom.xml`: configuración del proyecto y sus dependencias.

## Arquitectura

El proyecto utiliza arquitectura hexagonal, separando el dominio y la lógica de aplicación de los adaptadores de entrada y salida. Esto permite reducir el acoplamiento entre la lógica de negocio y las tecnologías de infraestructura.
