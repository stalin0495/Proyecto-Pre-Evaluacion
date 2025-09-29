export const environment = {
  production: false,
  api: {
    baseUrl: 'http://localhost:8091',
    customerService: 'http://localhost:8091/customer-services/api/v1',
    accountService: 'http://localhost:8091/account-services/api/v1',
    reportService: 'http://localhost:8091/report-services/api/v1'
  },
  pagination: {
    defaultPageSize: 10,
    maxPageSize: 100,
    defaultPage: 0
  },
  dateFormat: {
    iso: 'YYYY-MM-DDTHH:mm:ss',
    display: 'DD/MM/YYYY HH:mm'
  },
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  features: {
    enableLogging: true,
    enableMockData: false,
    enableOfflineMode: false,
    enablePagination: true
  },
  cors: {
    allowedOrigins: ['http://localhost:4200'],
    allowedMethods: ['GET', 'POST', 'PUT', 'DELETE', 'OPTIONS'],
    allowCredentials: true
  },
  healthCheck: {
    customer: 'http://localhost:8091/customer-services/actuator/health',
    account: 'http://localhost:8091/account-services/actuator/health'
  }
};