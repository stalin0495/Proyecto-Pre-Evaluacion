export const environment = {
  production: true,
  api: {
    baseUrl: 'https://your-production-domain.com',
    customerService: 'https://your-production-domain.com/customer-services/api/v1',
    accountService: 'https://your-production-domain.com/account-services/api/v1',
    reportService: 'https://your-production-domain.com/report-services/api/v1'
  },
  pagination: {
    defaultPageSize: 20,
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
    enableLogging: false,
    enableMockData: false,
    enableOfflineMode: false,
    enablePagination: true
  }
};