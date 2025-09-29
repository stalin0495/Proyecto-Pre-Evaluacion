import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CustomerService } from './customer.service';
import { Customer, PaginatedResponse } from '../models/bank.models';
import { environment } from '../../environments/environment';

describe('CustomerService', () => {
  let service: CustomerService;
  let httpMock: HttpTestingController;
  
  // Mock data para pruebas
  const mockCustomer: Customer = {
    customerId: '1',
    personId: 'P001',
    password: 'test123',
    status: 'A'
  };

  const mockPaginatedResponse: PaginatedResponse<Customer> = {
    content: [mockCustomer],
    totalElements: 1,
    totalPages: 1,
    size: 10,
    number: 0,
    first: true,
    last: true,
    numberOfElements: 1,
    pageable: {
      sort: { sorted: false, unsorted: true },
      pageNumber: 0,
      pageSize: 10,
      offset: 0,
      paged: true,
      unpaged: false
    },
    sort: { sorted: false, unsorted: true }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CustomerService]
    });

    service = TestBed.inject(CustomerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Verificar que no hay requests HTTP pendientes
    httpMock.verify();
  });

  describe('Inicialización del Servicio', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });

  describe('getAllCustomers', () => {
    it('should return paginated customers', () => {
      // Arrange
      const expectedUrl = `${environment.api.customerService}/customers`;

      // Act
      service.getAllCustomers().subscribe((response) => {
        // Assert
        expect(response).toEqual(mockPaginatedResponse);
        expect(response.content.length).toBe(1);
        expect(response.content[0]).toEqual(mockCustomer);
      });

      // Verificar que se hizo la petición HTTP correcta
      const req = httpMock.expectOne(expectedUrl + '?page=0&size=10');
      expect(req.request.method).toBe('GET');
      expect(req.request.headers.get('Content-Type')).toBe('application/json');
      
      // Simular respuesta del servidor
      req.flush(mockPaginatedResponse);
    });

    it('should handle pagination parameters correctly', () => {
      // Arrange
      const pagination = { page: 1, size: 20 };
      const expectedUrl = `${environment.api.customerService}/customers`;

      // Act
      service.getAllCustomers(pagination).subscribe();

      // Assert
      const req = httpMock.expectOne(expectedUrl + '?page=1&size=20');
      expect(req.request.method).toBe('GET');
      
      req.flush(mockPaginatedResponse);
    });
  });

  describe('getCustomerById', () => {
    it('should return a single customer', () => {
      // Arrange
      const customerId = '1';
      const expectedUrl = `${environment.api.customerService}/customers/${customerId}`;

      // Act
      service.getCustomerById(customerId).subscribe((customer) => {
        // Assert
        expect(customer).toEqual(mockCustomer);
      });

      // Verificar petición HTTP
      const req = httpMock.expectOne(expectedUrl);
      expect(req.request.method).toBe('GET');
      
      req.flush(mockCustomer);
    });

    it('should handle error when customer not found', () => {
      // Arrange
      const customerId = 'non-existent';
      const expectedUrl = `${environment.api.customerService}/customers/${customerId}`;
      const errorMessage = 'Customer not found';

      // Act & Assert
      service.getCustomerById(customerId).subscribe({
        next: () => fail('Should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
          expect(error.statusText).toBe('Not Found');
        }
      });

      const req = httpMock.expectOne(expectedUrl);
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    });
  });

  describe('getAllCustomersSimple', () => {
    it('should return array of customers from paginated response', () => {
      // Arrange
      const expectedUrl = `${environment.api.customerService}/customers`;

      // Act
      service.getAllCustomersSimple().subscribe((customers) => {
        // Assert
        expect(customers).toEqual([mockCustomer]);
        expect(Array.isArray(customers)).toBeTruthy();
      });

      // Verificar petición HTTP
      const req = httpMock.expectOne(expectedUrl + '?page=0&size=1000');
      expect(req.request.method).toBe('GET');
      
      req.flush(mockPaginatedResponse);
    });
  });

  describe('Error Handling', () => {
    it('should handle network errors gracefully', () => {
      // Act & Assert
      service.getAllCustomers().subscribe({
        next: () => fail('Should have failed with network error'),
        error: (error) => {
          expect(error.message).toContain('Http failure');
        }
      });

      const req = httpMock.expectOne(request => request.url.includes('/customers'));
      
      // Simular error de red
      req.error(new ErrorEvent('Network error'));
    });
  });
});