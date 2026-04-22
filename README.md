# API Pago

## Prerequisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java**: JDK 11 o superior (recomendado JDK 17 LTS)
- **Maven**: Versión 3.8.0 o superior

### Verificar instalación

```bash
# Verificar versión de Java
java -version

# Verificar versión de Maven
mvn -version
```

## Cómo ejecutar el proyecto

### Compilar el proyecto

```bash
mvn clean install
```

### Ejecutar tests

```bash
mvn test
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

O compilar y ejecutar directamente:

```bash
mvn clean package
java -jar target/api-pago-*.jar
```

## Estructura del proyecto

- `src/main/java`: Código fuente principal
- `src/test/java`: Tests unitarios e integración
- `pom.xml`: Configuración de dependencias Maven
