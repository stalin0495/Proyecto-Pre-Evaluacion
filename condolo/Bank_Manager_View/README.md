# Bank Manager View

## Descripción
Aplicación frontend desarrollada en Angular para la gestión bancaria. Sistema completo para administrar cuentas, transacciones, clientes y generar reportes.

## Tecnologías Utilizadas
- **Angular 17+** - Framework principal
- **TypeScript** - Lenguaje de programación
- **Angular Material** - Biblioteca de componentes UI
- **Bootstrap** - Framework CSS adicional
- **SCSS** - Preprocesador CSS
- **Docker** - Containerización
- **Nginx** - Servidor web para producción

## Características
- ✅ Dashboard con métricas principales
- ✅ Gestión de cuentas bancarias
- ✅ Historial de transacciones
- ✅ Administración de clientes
- ✅ Generación de reportes
- ✅ Diseño responsive
- ✅ Componentes standalone de Angular
- ✅ Lazy loading de rutas
- ✅ Material Design

## Estructura del Proyecto
```
Bank_Manager_View/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── dashboard/
│   │   │   ├── accounts/
│   │   │   ├── transactions/
│   │   │   ├── customers/
│   │   │   └── reports/
│   │   ├── app.component.ts
│   │   └── app.routes.ts
│   ├── assets/
│   ├── index.html
│   ├── main.ts
│   └── styles.scss
├── docker/
├── Dockerfile
├── Dockerfile.dev
├── docker-compose.yml
├── nginx.conf
├── angular.json
├── package.json
└── tsconfig.json
```

## Instalación y Configuración

### Prerrequisitos
- Node.js (v18 o superior)
- npm o yarn
- Docker y Docker Compose (para desarrollo con contenedores)

### Desarrollo Local

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd Bank_Manager_View
   ```

2. **Instalar dependencias**
   ```bash
   npm install
   ```

3. **Ejecutar en modo desarrollo**
   ```bash
   npm start
   ```
   La aplicación estará disponible en `http://localhost:4200`

### Desarrollo con Docker

1. **Ejecutar en modo desarrollo**
   ```bash
   docker-compose up app-dev
   ```
   Acceder a `http://localhost:4200`

2. **Ejecutar en modo producción**
   ```bash
   docker-compose up app-prod
   ```
   Acceder a `http://localhost:80`

## Scripts Disponibles

- `npm start` - Ejecuta la aplicación en modo desarrollo
- `npm run build` - Construye la aplicación para producción
- `npm run build:prod` - Construye optimizado para producción
- `npm test` - Ejecuta las pruebas unitarias
- `npm run lint` - Ejecuta el linter
- `docker:build` - Construye la imagen Docker
- `docker:run` - Ejecuta el contenedor Docker

## Configuración Docker

### Desarrollo
- **Puerto**: 4200
- **Hot Reload**: Habilitado
- **Volúmenes**: Código fuente montado

### Producción
- **Puerto**: 80
- **Servidor**: Nginx
- **Optimización**: Habilitada
- **Compresión**: Gzip activado

## Rutas Principales

- `/dashboard` - Dashboard principal con métricas
- `/accounts` - Gestión de cuentas bancarias
- `/transactions` - Historial de transacciones
- `/customers` - Administración de clientes
- `/reports` - Generación de reportes

## Componentes Principales

### Dashboard
- Métricas generales del banco
- Tarjetas con información clave
- Navegación rápida

### Accounts
- Lista de cuentas bancarias
- Funciones CRUD para cuentas
- Estados de cuenta

### Transactions
- Historial completo de transacciones
- Filtros y búsqueda
- Detalles de transacciones

### Customers
- Base de datos de clientes
- Información de contacto
- Historial de clientes

### Reports
- Reportes financieros
- Análisis de datos
- Exportación de reportes

## Configuración de Nginx

El archivo `nginx.conf` incluye:
- Configuración para SPA (Single Page Application)
- Compresión Gzip
- Cache para archivos estáticos
- Headers de seguridad
- Manejo de errores

## Variables de Entorno

Crear un archivo `.env` para configuraciones específicas:
```bash
NODE_ENV=development
API_URL=http://localhost:3000
```

## Pruebas

```bash
# Ejecutar pruebas unitarias
npm test

# Ejecutar pruebas con coverage
npm run test:coverage
```

## Construcción para Producción

```bash
# Construir aplicación
npm run build:prod

# Construir imagen Docker
docker build -t bank-manager-view .

# Ejecutar contenedor
docker run -p 80:80 bank-manager-view
```

## Contribución

1. Fork el proyecto
2. Crear una branch para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la branch (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.

## Soporte

Para soporte técnico o preguntas, crear un issue en el repositorio.

---

**Desarrollado con ❤️ usando Angular y Docker**