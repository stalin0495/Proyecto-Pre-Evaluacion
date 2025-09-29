# Microservicio de Gestión de Cuentas Bancarias

## Descripción General

Este es un microservicio desarrollado en Spring Boot para la gestión de cuentas bancarias y transacciones del sistema bancario Pichincha. El servicio proporciona funcionalidades CRUD para cuentas, gestión de transacciones y generación de reportes.

## Arquitectura

El proyecto sigue una arquitectura hexagonal (puertos y adaptadores) con las siguientes capas:

- **Domain**: Contiene las entidades de negocio y reglas de dominio
- **Application**: Contiene los casos de uso y servicios de aplicación
- **Infrastructure**: Contiene los adaptadores de entrada (REST) y salida (Base de datos)

## Tecnologías Utilizadas

- **Java 21**: Versión del lenguaje
- **Spring Boot 3.3.0**: Framework principal
- **Spring Data JPA**: Para acceso a datos
- **Spring Validation**: Para validación de datos
- **Spring Cloud OpenFeign**: Para comunicación con otros microservicios
- **MySQL**: Base de datos relacional
- **MapStruct**: Para mapeo de objetos
- **Lombok**: Para reducir código boilerplate
- **Gradle**: Herramienta de construcción

## Configuración

### Variables de Entorno Requeridas

```bash
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/account_db
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=contraseña
CUSTOMER_SERVICE_URL=http://localhost:8081/customer-services/api
```

### Configuración de Base de Datos

El servicio utiliza MySQL como base de datos. La configuración se encuentra en `application.yaml`:

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
```

## Endpoints de la API

### Base URL
```
http://localhost:8080/account-services/api
```

### Gestión de Cuentas

#### Crear Cuenta
```http
POST /v1/accounts
Content-Type: application/json

{
  "customerId": "uuid-del-cliente",
  "accountNumber": "1234567890",
  "accountType": "AHORRO",
  "initialBalance": 1000.00
}
```

#### Obtener Todas las Cuentas
```http
GET /v1/accounts?page=0&size=10
```

#### Obtener Cuenta por ID
```http
GET /v1/accounts/{accountId}
```

#### Actualizar Cuenta
```http
PUT /v1/accounts/{accountId}
Content-Type: application/json

{
  "customerId": "uuid-del-cliente",
  "accountNumber": "1234567890",
  "accountType": "CORRIENTE",
  "initialBalance": 1500.00
}
```

#### Eliminar Cuenta
```http
DELETE /v1/accounts/{accountId}
```

### Gestión de Transacciones

#### Crear Transacción
```http
POST /v1/transactions
Content-Type: application/json

{
  "accountId": "uuid-de-la-cuenta",
  "transactionType": "DEPOSITO",
  "amount": 500.00
}
```

#### Obtener Todas las Transacciones
```http
GET /v1/transactions?page=0&size=10
```

#### Obtener Transacción por ID
```http
GET /v1/transactions/{transactionId}
```

#### Eliminar Transacción
```http
DELETE /v1/transactions/{transactionId}
```

### Reportes

#### Obtener Reporte de Transacciones por Cliente
```http
GET /v1/customers/{customerId}/transactions/report?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10
```

## Modelos de Datos

### Account (Cuenta)
```json
{
  "id": "uuid",
  "customerId": "uuid-del-cliente",
  "accountNumber": "1234567890",
  "accountType": "AHORRO|CORRIENTE",
  "initialBalance": 1000.00,
  "status": true
}
```

### Transaction (Transacción)
```json
{
  "id": "uuid",
  "accountId": "uuid-de-la-cuenta",
  "date": "2024-01-01T10:30:00",
  "transactionType": "DEPOSITO|RETIRO",
  "amount": 500.00,
  "balance": 1500.00,
  "customer": {...},
  "account": {...}
}
```

### Report (Reporte)
```json
{
  "customer": {...},
  "transactions": [
    {
      "id": "uuid",
      "date": "2024-01-01T10:30:00",
      "transactionType": "DEPOSITO",
      "amount": 500.00,
      "balance": 1500.00,
      "account": {...}
    }
  ]
}
```

## Validaciones

### Cuenta
- **customerId**: Requerido, máximo 36 caracteres (UUID)
- **accountNumber**: Requerido, debe ser un número de 10 dígitos
- **accountType**: Requerido, no puede estar en blanco
- **initialBalance**: Debe tener máximo 18 dígitos enteros y 2 decimales

### Transacción
- **accountId**: Máximo 36 caracteres (UUID)
- **transactionType**: Requerido, entre 3 y 32 caracteres
- **amount**: Debe tener máximo 18 dígitos enteros y 2 decimales

## Manejo de Errores

El servicio maneja los siguientes tipos de errores:

### Códigos de Estado HTTP
- **200**: Operación exitosa
- **201**: Recurso creado exitosamente
- **400**: Solicitud incorrecta (Bad Request)
- **404**: Recurso no encontrado
- **409**: Conflicto (recurso ya existe)
- **500**: Error interno del servidor

### Estructura de Respuesta de Error
```json
{
  "timestamp": "2024-01-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción del error",
  "path": "/v1/accounts"
}
```

## Integración con Otros Servicios

El microservicio se integra con:

- **Customer Service**: Para obtener información de clientes mediante OpenFeign
- **Base de Datos MySQL**: Para persistencia de datos

## Configuración de CORS

El servicio tiene configurado CORS para permitir requests desde diferentes orígenes en desarrollo.

## Construcción y Despliegue

### Prerequisitos
- Java 21
- MySQL 8.0+
- Gradle 7+

### Compilar el proyecto
```bash
./gradlew build
```

### Ejecutar tests
```bash
./gradlew test
```

### Ejecutar la aplicación
```bash
./gradlew bootRun
```

### Generar JAR
```bash
./gradlew bootJar
```

### Docker
El proyecto incluye un `Dockerfile` para containerización:

```bash
docker build -t account-service .
docker run -p 8080:8080 account-service
```

## Base de Datos

### Script de Creación
El archivo `BaseDatos.sql` contiene el script para crear las tablas necesarias:

- **accounts**: Almacena información de las cuentas bancarias
- **transactions**: Almacena las transacciones realizadas

### Estructura de Tablas

#### accounts
- id (VARCHAR, PRIMARY KEY)
- customer_id (VARCHAR)
- account_number (VARCHAR, UNIQUE)
- account_type (VARCHAR)
- initial_balance (DECIMAL)
- status (BOOLEAN)

#### transactions
- id (VARCHAR, PRIMARY KEY)
- account_id (VARCHAR, FOREIGN KEY)
- date (TIMESTAMP)
- transaction_type (VARCHAR)
- amount (DECIMAL)
- balance (DECIMAL)

## Pruebas

### Colección de Postman
El archivo `Accounts.postman_collection.json` contiene una colección completa de pruebas para todos los endpoints del servicio.

### Ejecutar Pruebas
```bash
# Ejecutar todas las pruebas
./gradlew test

# Ejecutar solo pruebas unitarias
./gradlew test --tests "*Test"

# Generar reporte de cobertura
./gradlew jacocoTestReport
```

## Logging

El servicio utiliza el sistema de logging estándar de Spring Boot con configuración para:

- Mostrar SQL queries ejecutadas
- Logging de errores y excepciones
- Información de debug en desarrollo

## Monitoreo y Salud

El servicio incluye actuators de Spring Boot para monitoreo:

```http
GET /actuator/health
GET /actuator/info
```

## Seguridad

- Validación de datos de entrada
- Manejo seguro de errores sin exposición de información sensible
- Configuración de CORS apropiada

## Contribución

1. Fork el repositorio
2. Crear una rama para la nueva funcionalidad
3. Realizar los cambios y agregar pruebas
4. Ejecutar las pruebas para asegurar que todo funciona
5. Crear un Pull Request

## Soporte

Para reportar problemas o solicitar nuevas funcionalidades, crear un issue en el repositorio del proyecto.

## Licencia

Este proyecto es propiedad de Banco Pichincha y está destinado para uso interno.
