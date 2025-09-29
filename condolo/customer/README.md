# 🏦 Sistema Bancario Pichincha - Servicio de Clientes

## 📋 Tabla de Contenidos
- [Descripción General](#descripción-general)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Modelo de Datos](#modelo-de-datos)
- [API REST](#api-rest)
- [Instalación y Configuración](#instalación-y-configuración)
- [Uso de la API](#uso-de-la-api)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Validaciones](#validaciones)
- [Manejo de Errores](#manejo-de-errores)
- [Testing](#testing)
- [Despliegue](#despliegue)

## 🎯 Descripción General

El Sistema Bancario Pichincha es una aplicación completa que maneja la gestión de clientes, cuentas bancarias y transacciones. Este servicio está construido con **Spring Boot** y sigue los principios de **Arquitectura Hexagonal (Clean Architecture)**.

### Funcionalidades Principales
- ✅ Gestión completa de clientes (CRUD)
- ✅ Gestión de cuentas bancarias (Ahorro y Corriente)
- ✅ Procesamiento de transacciones (Depósitos y Retiros)
- ✅ Generación de reportes y estados de cuenta
- ✅ API REST con documentación OpenAPI/Swagger
- ✅ Validaciones robustas y manejo de errores
- ✅ Arquitectura escalable y mantenible

### Tecnologías Utilizadas
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **OpenAPI 3.0**
- **Docker**

## 🏗️ Arquitectura del Sistema

El proyecto implementa **Arquitectura Hexagonal** con las siguientes capas:

```
📁 src/main/java/com/pichincha/customer/
├── 📁 application/          # Casos de uso y lógica de negocio
│   ├── 📁 service/         # Servicios de aplicación
│   ├── 📁 output/port/     # Puertos de salida
│   └── 📁 exception/       # Excepciones de aplicación
├── 📁 domain/              # Entidades de dominio
├── 📁 infrastructure/      # Implementaciones técnicas
│   ├── 📁 input/adapter/   # Adaptadores de entrada (REST)
│   ├── 📁 output/adapter/  # Adaptadores de salida (DB)
│   └── 📁 exception/       # Manejo global de excepciones
└── CustomerApplication.java # Clase principal
```

### Beneficios de esta Arquitectura
- 🔄 **Desacoplamiento**: Las capas no dependen de implementaciones específicas
- 🧪 **Testeable**: Fácil creación de tests unitarios y de integración
- 🔧 **Mantenible**: Cambios en una capa no afectan otras
- 📈 **Escalable**: Fácil agregar nuevas funcionalidades

## 🗄️ Modelo de Datos

El sistema utiliza **dos bases de datos** para separar responsabilidades:

### Base de Datos: `pichincha_customers`
```sql
📊 Tabla: persons
├── person_id (VARCHAR(36), PK)
├── name (VARCHAR(255), NOT NULL)
├── gender (VARCHAR(255), NOT NULL)
├── age (TINYINT UNSIGNED)
├── identification (VARCHAR(100), UNIQUE)
├── address (VARCHAR(255))
└── phone (VARCHAR(50))

📊 Tabla: customers
├── customer_id (VARCHAR(36), UNIQUE)
├── person_id (VARCHAR(36), PK, FK)
├── password (VARCHAR(255), NOT NULL)
└── status (CHAR(1))
```

### Base de Datos: `pichincha_accounts`
```sql
📊 Tabla: accounts
├── account_id (VARCHAR(36), PK)
├── customer_id (VARCHAR(36), NOT NULL)
├── account_number (VARCHAR(20), UNIQUE)
├── account_type (VARCHAR(255), NOT NULL)
├── initial_balance (DECIMAL(18,2))
└── status (CHAR(1))

📊 Tabla: transactions
├── transaction_id (VARCHAR(36), PK)
├── account_id (VARCHAR(36), FK)
├── date (TIMESTAMP)
├── transaction_type (VARCHAR(255))
├── amount (DECIMAL(18,2))
└── balance (DECIMAL(18,2))
```

### Relaciones
- Una **Persona** puede ser un **Cliente**
- Un **Cliente** puede tener múltiples **Cuentas**
- Una **Cuenta** puede tener múltiples **Transacciones**

## 🚀 API REST

La API está documentada con **OpenAPI 3.0** y proporciona endpoints para:

### 👥 Gestión de Clientes (`/v1/customers`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/v1/customers` | Lista todos los clientes (paginado) |
| `POST` | `/v1/customers` | Crea un nuevo cliente |
| `GET` | `/v1/customers/{id}` | Obtiene un cliente específico |
| `PATCH` | `/v1/customers/{id}` | Actualiza un cliente |
| `DELETE` | `/v1/customers/{id}` | Elimina un cliente (lógicamente) |

### 💳 Gestión de Cuentas (`/v1/accounts`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/v1/accounts` | Lista todas las cuentas (paginado) |
| `POST` | `/v1/accounts` | Crea una nueva cuenta |
| `GET` | `/v1/accounts/{id}` | Obtiene una cuenta específica |
| `PATCH` | `/v1/accounts/{id}` | Actualiza una cuenta |
| `DELETE` | `/v1/accounts/{id}` | Elimina una cuenta |

### 💰 Gestión de Transacciones (`/v1/transactions`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/v1/transactions` | Lista transacciones (paginado) |
| `POST` | `/v1/transactions` | Crea una nueva transacción |
| `GET` | `/v1/transactions/{id}` | Obtiene una transacción específica |

### 📊 Reportes (`/v1/reports`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/v1/reports/account-statement` | Genera estado de cuenta |

## ⚙️ Instalación y Configuración

### Prerrequisitos
- ☕ Java 17 o superior
- 🗄️ MySQL 8.0+
- 📦 Maven 3.6+
- 🐳 Docker (opcional)

### Configuración de Base de Datos

1. **Ejecutar el script de base de datos:**
```bash
mysql -u root -p < BaseDatos.sql
```

2. **Configurar las propiedades de conexión** en `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pichincha_customers
    username: tu_usuario
    password: tu_password
```

### Instalación Local

1. **Clonar el repositorio:**
```bash
git clone [repositorio]
cd customer
```

2. **Compilar el proyecto:**
```bash
./gradlew build
```

3. **Ejecutar la aplicación:**
```bash
./gradlew bootRun
```

4. **Verificar que funciona:**
```bash
curl http://localhost:8080/v1/customers
```

### Instalación con Docker

1. **Construir la imagen:**
```bash
docker build -t pichincha-customer-service .
```

2. **Ejecutar el contenedor:**
```bash
docker run -p 8080:8080 pichincha-customer-service
```

## 📖 Uso de la API

### Acceso a la Documentación
Una vez que la aplicación esté ejecutándose, puedes acceder a:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Autenticación
El sistema utiliza autenticación básica. Los clientes tienen contraseñas encriptadas que se validan en cada operación.

### Formato de Respuestas
Todas las respuestas siguen el formato JSON estándar:

**Respuesta Exitosa:**
```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Juan Carlos Pérez",
  "identification": "1234567890",
  "status": true
}
```

**Respuesta de Error:**
```json
{
  "timestamp": "2024-09-29T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Datos de entrada inválidos",
  "path": "/v1/customers"
}
```

## 💡 Ejemplos de Uso

### 1. Crear un Nuevo Cliente

**Petición:**
```bash
curl -X POST http://localhost:8080/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Carlos Pérez",
    "gender": "Masculino",
    "age": 30,
    "identification": "1234567890",
    "address": "Av. Amazonas 123 y Patria",
    "phone": "0987654321",
    "password": "Password123!"
  }'
```

**Respuesta:**
```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "personId": "456e7890-e89b-12d3-a456-426614174001",
  "name": "Juan Carlos Pérez",
  "gender": "Masculino",
  "age": 30,
  "identification": "1234567890",
  "address": "Av. Amazonas 123 y Patria",
  "phone": "0987654321",
  "status": true
}
```

### 2. Crear una Cuenta Bancaria

**Petición:**
```bash
curl -X POST http://localhost:8080/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "accountNumber": "478758",
    "accountType": "AHORRO",
    "initialBalance": 1000.00
  }'
```

### 3. Realizar una Transacción

**Petición:**
```bash
curl -X POST http://localhost:8080/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "accountId": "789e0123-e89b-12d3-a456-426614174002",
    "transactionType": "DEPOSITO",
    "amount": 500.00
  }'
```

### 4. Generar Estado de Cuenta

**Petición:**
```bash
curl "http://localhost:8080/v1/reports/account-statement?customerId=123e4567-e89b-12d3-a456-426614174000&startDate=2024-01-01&endDate=2024-12-31"
```

## ✅ Validaciones

El sistema implementa validaciones robustas para garantizar la integridad de los datos:

### Validaciones de Cliente
- **Nombre**: Solo letras y espacios, 1-255 caracteres
- **Identificación**: Exactamente 10 dígitos numéricos
- **Edad**: Entre 0 y 255 años
- **Teléfono**: 1-50 caracteres
- **Dirección**: Alfanumérica con caracteres especiales permitidos
- **Contraseña**: 8-20 caracteres, debe incluir mayúscula, minúscula, número y carácter especial

### Validaciones de Cuenta
- **Número de cuenta**: 1-20 caracteres, único en el sistema
- **Tipo de cuenta**: Solo "AHORRO" o "CORRIENTE"
- **Saldo inicial**: Debe ser mayor o igual a 0

### Validaciones de Transacción
- **Monto**: Debe ser mayor a 0.01
- **Tipo**: Solo "DEPOSITO" o "RETIRO"
- **Saldo suficiente**: Para retiros, verificar que hay fondos

## 🚨 Manejo de Errores

El sistema proporciona mensajes de error claros y específicos:

### Códigos de Estado HTTP
- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos de entrada inválidos
- `404 Not Found`: Recurso no encontrado
- `409 Conflict`: Conflicto con estado actual (ej: identificación duplicada)
- `500 Internal Server Error`: Error interno del servidor

### Tipos de Error Comunes

1. **Error de Validación:**
```json
{
  "timestamp": "2024-09-29T10:15:30Z",
  "status": 400,
  "error": "Validation Error",
  "message": "Datos de entrada inválidos",
  "validationErrors": [
    {
      "field": "identification",
      "message": "La identificación debe tener exactamente 10 dígitos"
    }
  ]
}
```

2. **Recurso No Encontrado:**
```json
{
  "timestamp": "2024-09-29T10:15:30Z",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con ID: 123e4567-e89b-12d3-a456-426614174000"
}
```

## 🧪 Testing

El proyecto incluye tests comprehensivos:

### Ejecutar Tests
```bash
# Todos los tests
./gradlew test

# Solo tests unitarios
./gradlew test --tests "*Test"

# Solo tests de integración
./gradlew test --tests "*IntegrationTest"
```

### Tipos de Tests Incluidos
- **Tests Unitarios**: Lógica de negocio y servicios
- **Tests de Integración**: Endpoints REST completos
- **Tests de Validación**: Verificación de reglas de negocio
- **Tests de Mappers**: Transformación de datos

### Cobertura de Tests
El proyecto mantiene una alta cobertura de tests para garantizar la calidad del código.

## 🚀 Despliegue

### Despliegue en Desarrollo
```bash
# Ejecutar en modo desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Despliegue en Producción

1. **Generar JAR:**
```bash
./gradlew bootJar
```

2. **Ejecutar JAR:**
```bash
java -jar build/libs/customer-0.0.1-SNAPSHOT.jar
```

3. **Usando Docker:**
```bash
docker build -t pichincha-customer-service .
docker run -d -p 8080:8080 --name customer-service pichincha-customer-service
```

### Variables de Entorno
```bash
# Base de datos
DB_URL=jdbc:mysql://localhost:3306/pichincha_customers
DB_USERNAME=usuario
DB_PASSWORD=password

# Servidor
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=prod
```

## 📞 Soporte y Contacto

Para soporte técnico o consultas sobre el sistema:

- **Email**: desarrollo@pichincha.com
- **Equipo**: Equipo de Desarrollo Pichincha

## 📋 Notas Adicionales

### Consideraciones de Seguridad
- Las contraseñas se almacenan encriptadas
- Validación de entrada en todos los endpoints
- Manejo seguro de excepciones sin exponer información sensible

### Consideraciones de Performance
- Paginación en todas las consultas de lista
- Índices en campos de búsqueda frecuente
- Pool de conexiones configurado para alta concurrencia

### Monitoreo
- Logs estructurados para facilitar el debugging
- Métricas de performance disponibles
- Health checks implementados

---

📄 **Documentación generada automáticamente para el Sistema Bancario Pichincha v1.0.0**
