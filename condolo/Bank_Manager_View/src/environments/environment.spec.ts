import { environment } from './environment';

describe('Environment Configuration', () => {
  
  describe('API Configuration', () => {
    it('should have customer service URL defined', () => {
      expect(environment.api.customerService).toBeDefined();
      expect(environment.api.customerService).toContain('localhost:8091');
      expect(environment.api.customerService).toContain('customer-services');
    });

    it('should have account service URL defined', () => {
      expect(environment.api.accountService).toBeDefined();
      expect(environment.api.accountService).toContain('localhost:8091');
      expect(environment.api.accountService).toContain('account-services');
    });

    it('should have correct base URL format', () => {
      expect(environment.api.baseUrl).toBe('http://localhost:8091');
      expect(environment.api.customerService).toBe('http://localhost:8091/customer-services/api/v1');
      expect(environment.api.accountService).toBe('http://localhost:8091/account-services/api/v1');
    });
  });

  describe('Pagination Configuration', () => {
    it('should have correct default pagination settings', () => {
      expect(environment.pagination.defaultPageSize).toBe(10);
      expect(environment.pagination.maxPageSize).toBe(100);
      expect(environment.pagination.defaultPage).toBe(0);
      expect(typeof environment.pagination.defaultPageSize).toBe('number');
    });

    it('should validate pagination limits', () => {
      expect(environment.pagination.defaultPageSize).toBeGreaterThan(0);
      expect(environment.pagination.maxPageSize).toBeGreaterThan(environment.pagination.defaultPageSize);
      expect(environment.pagination.defaultPage).toBeGreaterThanOrEqual(0);
    });
  });

  describe('Headers Configuration', () => {
    it('should have correct HTTP headers defined', () => {
      expect(environment.headers['Content-Type']).toBe('application/json');
      expect(environment.headers['Accept']).toBe('application/json');
    });

    it('should have required headers for REST API', () => {
      const headers = environment.headers;
      expect(Object.keys(headers)).toContain('Content-Type');
      expect(Object.keys(headers)).toContain('Accept');
    });
  });

  describe('Date Format Configuration', () => {
    it('should have ISO date format defined', () => {
      expect(environment.dateFormat.iso).toBe('YYYY-MM-DDTHH:mm:ss');
      expect(environment.dateFormat.display).toBe('DD/MM/YYYY HH:mm');
    });

    it('should have consistent date format patterns', () => {
      expect(environment.dateFormat.iso).toContain('YYYY');
      expect(environment.dateFormat.iso).toContain('MM');
      expect(environment.dateFormat.iso).toContain('DD');
      expect(environment.dateFormat.display).toContain('/');
    });
  });

  describe('Feature Flags', () => {
    it('should have feature flags configured', () => {
      expect(typeof environment.features.enableLogging).toBe('boolean');
      expect(typeof environment.features.enableMockData).toBe('boolean');
      expect(typeof environment.features.enableOfflineMode).toBe('boolean');
      expect(typeof environment.features.enablePagination).toBe('boolean');
    });

    it('should have development-appropriate feature flags', () => {
      expect(environment.features.enableLogging).toBe(true);
      expect(environment.features.enablePagination).toBe(true);
    });
  });

  describe('CORS Configuration', () => {
    it('should have CORS settings defined', () => {
      expect(environment.cors).toBeDefined();
      expect(environment.cors.allowedOrigins).toContain('http://localhost:4200');
      expect(environment.cors.allowedMethods).toContain('GET');
      expect(environment.cors.allowedMethods).toContain('POST');
    });

    it('should allow credentials for authenticated requests', () => {
      expect(environment.cors.allowCredentials).toBe(true);
    });
  });

  describe('Health Check Configuration', () => {
    it('should have health check endpoints defined', () => {
      expect(environment.healthCheck.customer).toBeDefined();
      expect(environment.healthCheck.account).toBeDefined();
      expect(environment.healthCheck.customer).toContain('/actuator/health');
      expect(environment.healthCheck.account).toContain('/actuator/health');
    });

    it('should have consistent health check URL patterns', () => {
      expect(environment.healthCheck.customer).toBe('http://localhost:8091/customer-services/actuator/health');
      expect(environment.healthCheck.account).toBe('http://localhost:8091/account-services/actuator/health');
    });
  });
});