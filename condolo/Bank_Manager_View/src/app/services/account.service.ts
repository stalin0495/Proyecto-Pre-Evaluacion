import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Account, Transaction, CreateAccountRequest, CreateTransactionRequest, PaginationRequest, PaginatedResponse, DateRange } from '../models/bank.models';
import { environment } from '../../environments/environment';
import { BaseHttpService } from './base-http.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService extends BaseHttpService {
  private readonly accountsEndpoint = '/accounts';
  private readonly transactionsEndpoint = '/transactions';

  constructor(http: HttpClient) { 
    super(http);
  }

  // ===== OPERACIONES DE CUENTAS =====
  
  // Obtener todas las cuentas con paginación
  getAllAccounts(pagination?: PaginationRequest): Observable<PaginatedResponse<Account>> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', this.accountsEndpoint);
    
    this.log('Fetching accounts', { pagination, url });
    
    return this.getPaginated<Account>(url, pagination);
  }

  // Obtener todas las cuentas sin paginación (para compatibilidad)
  getAllAccountsSimple(): Observable<Account[]> {
    return this.getAllAccounts({ page: 0, size: 1000 }).pipe(
      map(response => response.content)
    );
  }

  // Obtener cuenta por ID
  getAccountById(accountId: string): Observable<Account> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/${accountId}`);
    
    this.log('Fetching account by ID', { accountId, url });
    
    return this.get<Account>(url);
  }

  // Obtener cuentas por cliente con paginación
  getAccountsByCustomer(customerId: string, pagination?: PaginationRequest): Observable<PaginatedResponse<Account>> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/customer/${customerId}`);
    
    this.log('Fetching accounts by customer', { customerId, pagination, url });
    
    return this.getPaginated<Account>(url, pagination);
  }

  // Obtener cuenta por número
  getAccountByNumber(accountNumber: string): Observable<Account> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/number/${accountNumber}`);
    
    this.log('Fetching account by number', { accountNumber, url });
    
    return this.get<Account>(url);
  }

  // Crear nueva cuenta
  createAccount(accountRequest: CreateAccountRequest): Observable<Account> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', this.accountsEndpoint);
    
    this.log('Creating account', { accountRequest, url });
    
    return this.post<Account>(url, accountRequest);
  }

  // Actualizar cuenta
  updateAccount(accountId: string, accountRequest: Partial<CreateAccountRequest>): Observable<Account> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/${accountId}`);
    
    this.log('Updating account', { accountId, accountRequest, url });
    
    return this.put<Account>(url, accountRequest);
  }

  // Eliminar cuenta
  deleteAccount(accountId: string): Observable<void> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/${accountId}`);
    
    this.log('Deleting account', { accountId, url });
    
    return this.delete<void>(url);
  }

  // Obtener balance de cuenta
  getAccountBalance(accountId: string): Observable<{ balance: number }> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/${accountId}/balance`);
    
    this.log('Fetching account balance', { accountId, url });
    
    return this.get<{ balance: number }>(url);
  }

  // ===== OPERACIONES DE TRANSACCIONES =====

  // Obtener todas las transacciones con paginación y filtros de fecha
  getAllTransactions(
    pagination?: PaginationRequest, 
    dateRange?: DateRange
  ): Observable<PaginatedResponse<Transaction>> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', this.transactionsEndpoint);
    
    let additionalParams: { [key: string]: string } = {};
    
    if (dateRange) {
      if (dateRange.startDate) {
        additionalParams['startDate'] = this.formatDateToISO(dateRange.startDate);
      }
      if (dateRange.endDate) {
        additionalParams['endDate'] = this.formatDateToISO(dateRange.endDate);
      }
    }
    
    this.log('Fetching transactions', { pagination, dateRange, additionalParams, url });
    
    return this.getPaginated<Transaction>(url, pagination, additionalParams);
  }

  // Obtener todas las transacciones sin paginación (para compatibilidad)
  getAllTransactionsSimple(): Observable<Transaction[]> {
    return this.getAllTransactions({ page: 0, size: 1000 }).pipe(
      map(response => response.content)
    );
  }

  // Obtener transacciones por cuenta con paginación
  getTransactionsByAccount(accountId: string, pagination?: PaginationRequest): Observable<PaginatedResponse<Transaction>> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/account/${accountId}`);
    
    this.log('Fetching transactions by account', { accountId, pagination, url });
    
    return this.getPaginated<Transaction>(url, pagination);
  }

  // Obtener transacciones por rango de fechas con paginación
  getTransactionsByDateRange(
    startDate: string, 
    endDate: string, 
    pagination?: PaginationRequest
  ): Observable<PaginatedResponse<Transaction>> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/date-range`);
    
    const additionalParams = {
      start: this.formatDateToISO(new Date(startDate)),
      end: this.formatDateToISO(new Date(endDate))
    };
    
    this.log('Fetching transactions by date range', { startDate, endDate, pagination, additionalParams, url });
    
    return this.getPaginated<Transaction>(url, pagination, additionalParams);
  }

  // Crear nueva transacción
  createTransaction(transactionRequest: CreateTransactionRequest): Observable<Transaction> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', this.transactionsEndpoint);
    
    this.log('Creating transaction', { transactionRequest, url });
    
    return this.post<Transaction>(url, transactionRequest);
  }

  // Procesar depósito
  processDeposit(accountId: string, amount: number): Observable<Transaction> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/deposit`);
    
    const request: CreateTransactionRequest = {
      accountId,
      transactionType: 'DEPOSIT',
      amount
    };
    
    this.log('Processing deposit', { accountId, amount, request, url });
    
    return this.post<Transaction>(url, request);
  }

  // Procesar retiro
  processWithdrawal(accountId: string, amount: number): Observable<Transaction> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/withdrawal`);
    
    const request: CreateTransactionRequest = {
      accountId,
      transactionType: 'WITHDRAWAL',
      amount
    };
    
    this.log('Processing withdrawal', { accountId, amount, request, url });
    
    return this.post<Transaction>(url, request);
  }

  // Procesar transferencia
  processTransfer(fromAccountId: string, toAccountId: string, amount: number): Observable<Transaction[]> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/transfer`);
    
    const transferRequest = {
      fromAccountId,
      toAccountId,
      amount
    };
    
    this.log('Processing transfer', { fromAccountId, toAccountId, amount, transferRequest, url });
    
    return this.post<Transaction[]>(url, transferRequest);
  }

  // ===== ESTADÍSTICAS Y REPORTES =====

  // Obtener estadísticas de cuentas
  getAccountsStats(): Observable<any> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/stats`);
    
    this.log('Fetching accounts statistics', { url });
    
    return this.get<any>(url);
  }

  // Obtener estadísticas de transacciones
  getTransactionsStats(): Observable<any> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.transactionsEndpoint}/stats`);
    
    this.log('Fetching transactions statistics', { url });
    
    return this.get<any>(url);
  }

  // Obtener reporte de estado de cuenta
  getAccountStatement(accountId: string, startDate: string, endDate: string): Observable<any> {
    this.validateServiceAvailability('Account Service');
    const url = this.buildUrl('accountService', `${this.accountsEndpoint}/${accountId}/statement`);
    
    const params = {
      start: this.formatDateToISO(new Date(startDate)),
      end: this.formatDateToISO(new Date(endDate))
    };
    
    this.log('Fetching account statement', { accountId, startDate, endDate, params, url });
    
    return this.get<any>(url, params);
  }
}