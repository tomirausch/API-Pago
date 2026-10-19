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

Configura estas variables en tu entorno de ejecución antes de iniciar la aplicación. No utilices credenciales reales en archivos públicos ni las subas al repositorio.

## Instalación y ejecución

Clona el repositorio y accede a la carpeta del proyecto:

```bash
git clone https://github.com/tomirausch/API-Pago.git
cd API-Pago/API_Pago
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

El código fuente se encuentra dentro de `API_Pago/`:

- `src/main/java`: código fuente de la aplicación.
- `src/main/resources`: archivos de configuración.
- `src/test/java`: pruebas unitarias y de integración.
- `pom.xml`: configuración del proyecto y sus dependencias.

## Arquitectura

El proyecto utiliza arquitectura hexagonal, separando el dominio y la lógica de aplicación de los adaptadores de entrada y salida. Esto permite reducir el acoplamiento entre la lógica de negocio y las tecnologías de infraestructura.
