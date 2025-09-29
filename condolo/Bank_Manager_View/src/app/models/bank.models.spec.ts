import { 
  Customer, 
  Account, 
  Transaction, 
  PaginatedResponse, 
  PaginationRequest,
  CreateCustomerRequest,
  CreateAccountRequest,
  CreateTransactionRequest,
  DateRange 
} from './bank.models';

describe('Bank Models', () => {
  
  describe('Customer Interface', () => {
    it('should create a valid Customer object', () => {
      // Arrange & Act
      const customer: Customer = {
        customerId: 'CUST001',
        personId: 'PERS001',
        password: 'securePassword123',
        status: 'A'
      };

      // Assert
      expect(customer.customerId).toBe('CUST001');
      expect(customer.personId).toBe('PERS001');
      expect(customer.password).toBe('securePassword123');
      expect(customer.status).toBe('A');
      expect(typeof customer.customerId).toBe('string');
      expect(typeof customer.personId).toBe('string');
    });

    it('should allow inactive status', () => {
      // Arrange & Act
      const inactiveCustomer: Customer = {
        customerId: 'CUST002',
        personId: 'PERS002',
        password: 'password',
        status: 'I'
      };

      // Assert
      expect(inactiveCustomer.status).toBe('I');
    });
  });

  describe('Account Interface', () => {
    it('should create a valid Account object', () => {
      // Arrange & Act
      const account: Account = {
        accountId: 'ACC001',
        accountNumber: '1234567890',
        accountType: 'SAVINGS',
        initialBalance: 1000.50,
        status: 'A',
        customerId: 'CUST001'
      };

      // Assert
      expect(account.accountId).toBe('ACC001');
      expect(account.accountNumber).toBe('1234567890');
      expect(account.accountType).toBe('SAVINGS');
      expect(account.initialBalance).toBe(1000.50);
      expect(account.status).toBe('A');
      expect(account.customerId).toBe('CUST001');
      expect(typeof account.initialBalance).toBe('number');
    });

    it('should handle different account types', () => {
      // Arrange & Act
      const checkingAccount: Account = {
        accountId: 'ACC002',
        accountNumber: '0987654321',
        accountType: 'CHECKING',
        initialBalance: 500.00,
        status: 'A',
        customerId: 'CUST001'
      };

      // Assert
      expect(checkingAccount.accountType).toBe('CHECKING');
    });

    it('should handle zero initial balance', () => {
      // Arrange & Act
      const zeroBalanceAccount: Account = {
        accountId: 'ACC003',
        accountNumber: '1111111111',
        accountType: 'SAVINGS',
        initialBalance: 0,
        status: 'A',
        customerId: 'CUST001'
      };

      // Assert
      expect(zeroBalanceAccount.initialBalance).toBe(0);
    });
  });

  describe('Transaction Interface', () => {
    it('should create a valid Transaction object', () => {
      // Arrange & Act
      const transaction: Transaction = {
        transactionId: 'TXN001',
        accountId: 'ACC001',
        transactionType: 'DEPOSIT',
        amount: 250.75,
        date: '2025-09-29T10:30:00',
        balance: 1250.75
      };

      // Assert
      expect(transaction.transactionId).toBe('TXN001');
      expect(transaction.accountId).toBe('ACC001');
      expect(transaction.transactionType).toBe('DEPOSIT');
      expect(transaction.amount).toBe(250.75);
      expect(transaction.date).toBe('2025-09-29T10:30:00');
      expect(transaction.balance).toBe(1250.75);
      expect(typeof transaction.amount).toBe('number');
    });

    it('should handle different transaction types', () => {
      // Arrange & Act
      const withdrawal: Transaction = {
        transactionId: 'TXN002',
        accountId: 'ACC001',
        transactionType: 'WITHDRAWAL',
        amount: 100.00,
        date: '2025-09-29T12:00:00',
        balance: 900.00
      };

      const transfer: Transaction = {
        transactionId: 'TXN003',
        accountId: 'ACC001',
        transactionType: 'TRANSFER',
        amount: 50.00,
        date: '2025-09-29T13:00:00',
        balance: 850.00
      };

      // Assert
      expect(withdrawal.transactionType).toBe('WITHDRAWAL');
      expect(transfer.transactionType).toBe('TRANSFER');
    });
  });

  describe('PaginatedResponse Interface', () => {
    it('should create a valid PaginatedResponse', () => {
      // Arrange
      const customers: Customer[] = [
        {
          customerId: 'CUST001',
          personId: 'PERS001',
          password: 'password',
          status: 'A'
        }
      ];

      // Act
      const paginatedResponse: PaginatedResponse<Customer> = {
        content: customers,
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

      // Assert
      expect(paginatedResponse.content).toEqual(customers);
      expect(paginatedResponse.totalElements).toBe(1);
      expect(paginatedResponse.totalPages).toBe(1);
      expect(paginatedResponse.size).toBe(10);
      expect(paginatedResponse.number).toBe(0);
      expect(paginatedResponse.first).toBe(true);
      expect(paginatedResponse.last).toBe(true);
      expect(paginatedResponse.content.length).toBe(1);
    });

    it('should handle empty paginated response', () => {
      // Act
      const emptyResponse: PaginatedResponse<Customer> = {
        content: [],
        totalElements: 0,
        totalPages: 0,
        size: 10,
        number: 0,
        first: true,
        last: true,
        numberOfElements: 0,
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

      // Assert
      expect(emptyResponse.content).toEqual([]);
      expect(emptyResponse.totalElements).toBe(0);
      expect(emptyResponse.numberOfElements).toBe(0);
    });
  });

  describe('Request Interfaces', () => {
    it('should create valid CreateCustomerRequest', () => {
      // Act
      const customerRequest: CreateCustomerRequest = {
        name: 'John Doe',
        gender: 'M',
        age: 30,
        identification: '12345678901',
        address: '123 Main St',
        phone: '+1234567890',
        password: 'securePassword123',
        status: 'A'
      };

      // Assert
      expect(customerRequest.name).toBe('John Doe');
      expect(customerRequest.gender).toBe('M');
      expect(customerRequest.identification).toBe('12345678901');
      expect(customerRequest.password).toBe('securePassword123');
      expect(customerRequest.status).toBe('A');
    });

    it('should create valid CreateAccountRequest', () => {
      // Act
      const accountRequest: CreateAccountRequest = {
        accountType: 'SAVINGS',
        initialBalance: 1000.00,
        customerId: 'CUST001',
        accountNumber: '1234567890',
        status: 'A'
      };

      // Assert
      expect(accountRequest.accountType).toBe('SAVINGS');
      expect(accountRequest.initialBalance).toBe(1000.00);
      expect(accountRequest.customerId).toBe('CUST001');
    });

    it('should create valid CreateTransactionRequest', () => {
      // Act
      const transactionRequest: CreateTransactionRequest = {
        accountId: 'ACC001',
        transactionType: 'DEPOSIT',
        amount: 500.00
      };

      // Assert
      expect(transactionRequest.accountId).toBe('ACC001');
      expect(transactionRequest.transactionType).toBe('DEPOSIT');
      expect(transactionRequest.amount).toBe(500.00);
    });
  });

  describe('Utility Interfaces', () => {
    it('should create valid PaginationRequest', () => {
      // Act
      const pagination: PaginationRequest = {
        page: 0,
        size: 10
      };

      // Assert
      expect(pagination.page).toBe(0);
      expect(pagination.size).toBe(10);
      expect(typeof pagination.page).toBe('number');
      expect(typeof pagination.size).toBe('number');
    });

    it('should create valid DateRange', () => {
      // Act
      const dateRange: DateRange = {
        startDate: '2025-01-01T00:00:00',
        endDate: '2025-12-31T23:59:59'
      };

      // Assert
      expect(dateRange.startDate).toBe('2025-01-01T00:00:00');
      expect(dateRange.endDate).toBe('2025-12-31T23:59:59');
      expect(typeof dateRange.startDate).toBe('string');
      expect(typeof dateRange.endDate).toBe('string');
    });

    it('should allow optional dates in DateRange', () => {
      // Act
      const partialDateRange: Partial<DateRange> = {
        startDate: '2025-01-01T00:00:00'
      };

      // Assert
      expect(partialDateRange.startDate).toBe('2025-01-01T00:00:00');
      expect(partialDateRange.endDate).toBeUndefined();
    });
  });
});