export interface Person {
  personId: string;
  name: string;
  gender: string;
  age?: number;
  identification: string;
  address: string;
  phone: string;
}

export interface Customer {
  customerId: string;
  personId: string;
  password: string;
  status: string;
  person?: Person;
  // Propiedades directas para facilitar el uso
  name?: string;
  gender?: string;
  age?: number;
  identification?: string;
  address?: string;
  phone?: string;
}

export interface Account {
  accountId: string;
  customerId: string;
  accountNumber: string;
  accountType: string;
  initialBalance: number;
  status: string;
}

export interface Transaction {
  transactionId: string;
  accountId: string;
  date: string;
  transactionType: string;
  amount: number;
  balance: number;
}

// Interfaces para paginación
export interface PaginationRequest {
  page: number;
  size: number;
  sort?: string;
  direction?: 'asc' | 'desc';
}

export interface PaginatedResponse<T> {
  content: T[];
  pageable: {
    sort: {
      sorted: boolean;
      unsorted: boolean;
    };
    pageNumber: number;
    pageSize: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  numberOfElements: number;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
  };
}

// Interfaces para filtros de fecha
export interface DateRange {
  startDate: string; // Formato ISO: YYYY-MM-DDTHH:mm:ss
  endDate: string;   // Formato ISO: YYYY-MM-DDTHH:mm:ss
}

// DTOs para requests
export interface CreateCustomerRequest {
  name: string;
  gender: string;
  age?: number;
  identification: string;
  address: string;
  phone: string;
  password: string;
  status: string;
}

export interface CreateAccountRequest {
  customerId: string;
  accountNumber: string;
  accountType: string;
  initialBalance: number;
  status: string;
}

export interface CreateTransactionRequest {
  accountId: string;
  transactionType: string;
  amount: number;
}