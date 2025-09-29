import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReportService, DashboardStats } from './report.service';
import { CustomerService } from './customer.service';
import { AccountService } from './account.service';
import { of } from 'rxjs';
import { Customer, Account, Transaction } from '../models/bank.models';

describe('ReportService', () => {
  let service: ReportService;
  let customerService: jasmine.SpyObj<CustomerService>;
  let accountService: jasmine.SpyObj<AccountService>;

  // Mock data
  const mockCustomers: Customer[] = [
    {
      customerId: 'CUST001',
      personId: 'PERS001',
      password: 'password123',
      status: 'A',
      name: 'John Doe'
    },
    {
      customerId: 'CUST002',
      personId: 'PERS002',
      password: 'password456',
      status: 'I',
      name: 'Jane Smith'
    }
  ];

  const mockAccounts: Account[] = [
    {
      accountId: 'ACC001',
      customerId: 'CUST001',
      accountNumber: '1234567890',
      accountType: 'SAVINGS',
      initialBalance: 1000.00,
      status: 'A'
    },
    {
      accountId: 'ACC002',
      customerId: 'CUST002',
      accountNumber: '0987654321',
      accountType: 'CHECKING',
      initialBalance: 500.00,
      status: 'A'
    }
  ];

  const mockTransactions: Transaction[] = [
    {
      transactionId: 'TXN001',
      accountId: 'ACC001',
      transactionType: 'DEPOSIT',
      amount: 200.00,
      date: new Date().toISOString(),
      balance: 1200.00
    },
    {
      transactionId: 'TXN002',
      accountId: 'ACC001',
      transactionType: 'WITHDRAWAL',
      amount: 50.00,
      date: new Date().toISOString(),
      balance: 1150.00
    }
  ];

  beforeEach(() => {
    // Crear spies para los servicios
    const customerServiceSpy = jasmine.createSpyObj('CustomerService', ['getAllCustomersSimple']);
    const accountServiceSpy = jasmine.createSpyObj('AccountService', ['getAllAccountsSimple', 'getAllTransactionsSimple']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ReportService,
        { provide: CustomerService, useValue: customerServiceSpy },
        { provide: AccountService, useValue: accountServiceSpy }
      ]
    });

    service = TestBed.inject(ReportService);
    customerService = TestBed.inject(CustomerService) as jasmine.SpyObj<CustomerService>;
    accountService = TestBed.inject(AccountService) as jasmine.SpyObj<AccountService>;
  });

  describe('Service Initialization', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });

  describe('getDashboardStats', () => {
    beforeEach(() => {
      // Configurar respuestas de los mocks
      customerService.getAllCustomersSimple.and.returnValue(of(mockCustomers));
      accountService.getAllAccountsSimple.and.returnValue(of(mockAccounts));
      accountService.getAllTransactionsSimple.and.returnValue(of(mockTransactions));
    });

    it('should return correct dashboard statistics', (done) => {
      // Act
      service.getDashboardStats().subscribe((stats: DashboardStats) => {
        // Assert
        expect(stats.totalCustomers).toBe(2);
        expect(stats.activeCustomers).toBe(1);
        expect(stats.totalAccounts).toBe(2);
        expect(stats.activeAccounts).toBe(2);
        expect(stats.totalTransactions).toBe(2);
        expect(stats.totalBalance).toBe(1500.00);
        expect(typeof stats.monthlyGrowth).toBe('number');
        done();
      });
    });

    it('should calculate active customers correctly', (done) => {
      // Act
      service.getDashboardStats().subscribe((stats: DashboardStats) => {
        // Assert - Solo 1 customer activo (status 'A')
        expect(stats.activeCustomers).toBe(1);
        expect(stats.totalCustomers).toBe(2);
        done();
      });
    });

    it('should calculate total balance correctly', (done) => {
      // Act
      service.getDashboardStats().subscribe((stats: DashboardStats) => {
        // Assert - Suma de initialBalance: 1000 + 500 = 1500
        expect(stats.totalBalance).toBe(1500.00);
        done();
      });
    });

    it('should handle empty data gracefully', (done) => {
      // Arrange - Configurar datos vacíos
      customerService.getAllCustomersSimple.and.returnValue(of([]));
      accountService.getAllAccountsSimple.and.returnValue(of([]));
      accountService.getAllTransactionsSimple.and.returnValue(of([]));

      // Act
      service.getDashboardStats().subscribe((stats: DashboardStats) => {
        // Assert
        expect(stats.totalCustomers).toBe(0);
        expect(stats.activeCustomers).toBe(0);
        expect(stats.totalAccounts).toBe(0);
        expect(stats.activeAccounts).toBe(0);
        expect(stats.totalTransactions).toBe(0);
        expect(stats.totalBalance).toBe(0);
        done();
      });
    });
  });

  describe('generateCustomerReport', () => {
    beforeEach(() => {
      customerService.getAllCustomersSimple.and.returnValue(of(mockCustomers));
    });

    it('should generate customer report with correct structure', (done) => {
      // Act
      service.generateCustomerReport().subscribe((report) => {
        // Assert
        expect(report.reportType).toBe('customers');
        expect(report.data).toEqual(mockCustomers);
        expect(report.summary.total).toBe(2);
        expect(report.summary.active).toBe(1);
        expect(report.summary.inactive).toBe(1);
        expect(report.generatedAt).toBeDefined();
        done();
      });
    });

    it('should include date range when provided', (done) => {
      // Arrange
      const startDate = '2025-01-01';
      const endDate = '2025-12-31';

      // Act
      service.generateCustomerReport(startDate, endDate).subscribe((report) => {
        // Assert
        expect(report.period.startDate).toBe(startDate);
        expect(report.period.endDate).toBe(endDate);
        done();
      });
    });

    it('should handle empty customer list', (done) => {
      // Arrange
      customerService.getAllCustomersSimple.and.returnValue(of([]));

      // Act
      service.generateCustomerReport().subscribe((report) => {
        // Assert
        expect(report.summary.total).toBe(0);
        expect(report.summary.active).toBe(0);
        expect(report.summary.inactive).toBe(0);
        expect(report.data).toEqual([]);
        done();
      });
    });
  });

  describe('generateTransactionReport', () => {
    beforeEach(() => {
      accountService.getAllTransactionsSimple.and.returnValue(of(mockTransactions));
    });

    it('should generate transaction report with correct calculations', (done) => {
      // Act
      service.generateTransactionReport().subscribe((report) => {
        // Assert
        expect(report.reportType).toBe('transactions');
        expect(report.data).toEqual(mockTransactions);
        expect(report.summary.total).toBe(2);
        expect(report.summary.deposits.count).toBe(1);
        expect(report.summary.deposits.amount).toBe(200.00);
        expect(report.summary.withdrawals.count).toBe(1);
        expect(report.summary.withdrawals.amount).toBe(50.00);
        expect(report.summary.netFlow).toBe(150.00); // 200 - 50
        done();
      });
    });

    it('should filter transactions by date range', (done) => {
      // Arrange
      const yesterday = new Date();
      yesterday.setDate(yesterday.getDate() - 1);
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 1);

      const transactionsWithDates = [
        {
          ...mockTransactions[0],
          date: yesterday.toISOString().split('T')[0] // Fecha de ayer
        },
        {
          ...mockTransactions[1],
          date: new Date().toISOString().split('T')[0] // Fecha de hoy
        }
      ];

      accountService.getAllTransactionsSimple.and.returnValue(of(transactionsWithDates));

      const startDate = new Date().toISOString().split('T')[0]; // Hoy
      const endDate = tomorrow.toISOString().split('T')[0]; // Mañana

      // Act
      service.generateTransactionReport(startDate, endDate).subscribe((report) => {
        // Assert - Solo debería incluir transacción de hoy
        expect(report.data.length).toBe(1);
        expect(report.summary.total).toBe(1);
        done();
      });
    });

    it('should handle no transactions', (done) => {
      // Arrange
      accountService.getAllTransactionsSimple.and.returnValue(of([]));

      // Act
      service.generateTransactionReport().subscribe((report) => {
        // Assert
        expect(report.summary.total).toBe(0);
        expect(report.summary.deposits.count).toBe(0);
        expect(report.summary.withdrawals.count).toBe(0);
        expect(report.summary.netFlow).toBe(0);
        done();
      });
    });
  });

  describe('Data Export Functions', () => {
    it('should convert data to CSV format correctly', () => {
      // Arrange
      const testData = [
        { id: 1, name: 'Test 1', amount: 100.50 },
        { id: 2, name: 'Test 2', amount: 200.75 }
      ];

      // Act
      const csvResult = (service as any).convertToCSV(testData);

      // Assert
      expect(csvResult).toContain('id,name,amount');
      expect(csvResult).toContain('1,Test 1,100.5');
      expect(csvResult).toContain('2,Test 2,200.75');
    });

    it('should handle empty data in CSV conversion', () => {
      // Act
      const csvResult = (service as any).convertToCSV([]);

      // Assert
      expect(csvResult).toBe('');
    });

    it('should handle data with commas in CSV conversion', () => {
      // Arrange
      const testData = [
        { name: 'Test, with comma', description: 'Normal text' }
      ];

      // Act
      const csvResult = (service as any).convertToCSV(testData);

      // Assert
      expect(csvResult).toContain('"Test, with comma"');
      expect(csvResult).toContain('Normal text');
    });
  });

  describe('Error Handling', () => {
    it('should handle service errors gracefully', (done) => {
      // Arrange
      const errorMessage = 'Service unavailable';
      customerService.getAllCustomersSimple.and.throwError(errorMessage);
      accountService.getAllAccountsSimple.and.returnValue(of(mockAccounts));
      accountService.getAllTransactionsSimple.and.returnValue(of(mockTransactions));

      // Act & Assert
      service.getDashboardStats().subscribe({
        next: () => fail('Should have thrown an error'),
        error: (error) => {
          expect(error).toBeDefined();
          done();
        }
      });
    });
  });
});