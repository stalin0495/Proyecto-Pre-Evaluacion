import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Customer, CreateCustomerRequest, Person, PaginationRequest, PaginatedResponse } from '../models/bank.models';
import { environment } from '../../environments/environment';
import { BaseHttpService } from './base-http.service';

@Injectable({
  providedIn: 'root'
})
export class CustomerService extends BaseHttpService {
  private readonly customersEndpoint = '/customers';

  constructor(http: HttpClient) { 
    super(http);
  }

  // Obtener todos los clientes con paginación
  getAllCustomers(pagination?: PaginationRequest): Observable<PaginatedResponse<Customer>> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', this.customersEndpoint);
    
    this.log('Fetching customers', { pagination, url });
    
    return this.getPaginated<Customer>(url, pagination);
  }

  // Obtener todos los clientes sin paginación (para compatibilidad)
  getAllCustomersSimple(): Observable<Customer[]> {
    return this.getAllCustomers({ page: 0, size: 1000 }).pipe(
      map(response => response.content)
    );
  }

  // Obtener cliente por ID
  getCustomerById(customerId: string): Observable<Customer> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/${customerId}`);
    
    this.log('Fetching customer by ID', { customerId, url });
    
    return this.get<Customer>(url);
  }

  // Obtener cliente por identificación
  getCustomerByIdentification(identification: string): Observable<Customer> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/identification/${identification}`);
    
    this.log('Fetching customer by identification', { identification, url });
    
    return this.get<Customer>(url);
  }

  // Crear nuevo cliente
  createCustomer(customerRequest: CreateCustomerRequest): Observable<Customer> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', this.customersEndpoint);
    
    this.log('Creating customer', { customerRequest, url });
    
    return this.post<Customer>(url, customerRequest);
  }

  // Actualizar cliente
  updateCustomer(customerId: string, customerRequest: Partial<CreateCustomerRequest>): Observable<Customer> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/${customerId}`);
    
    this.log('Updating customer', { customerId, customerRequest, url });
    
    return this.put<Customer>(url, customerRequest);
  }

  // Eliminar cliente
  deleteCustomer(customerId: string): Observable<void> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/${customerId}`);
    
    this.log('Deleting customer', { customerId, url });
    
    return this.delete<void>(url);
  }

  // Obtener estadísticas de clientes
  getCustomersStats(): Observable<any> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/stats`);
    
    this.log('Fetching customer stats', { url });
    
    return this.get<any>(url);
  }

  // Buscar clientes por nombre con paginación
  searchCustomersByName(name: string, pagination?: PaginationRequest): Observable<PaginatedResponse<Customer>> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/search`);
    
    const searchParams = { name: name };
    
    this.log('Searching customers by name', { name, pagination, url });
    
    return this.getPaginated<Customer>(url, pagination, searchParams);
  }

  // Buscar clientes por filtros avanzados
  searchCustomers(filters: {
    name?: string;
    identification?: string;
    status?: string;
    gender?: string;
  }, pagination?: PaginationRequest): Observable<PaginatedResponse<Customer>> {
    this.validateServiceAvailability('Customer Service');
    const url = this.buildUrl('customerService', `${this.customersEndpoint}/search`);
    
    // Filtrar parámetros vacíos
    const cleanFilters: { [key: string]: string } = {};
    Object.keys(filters).forEach(key => {
      const value = filters[key as keyof typeof filters];
      if (value && value.trim() !== '') {
        cleanFilters[key] = value;
      }
    });
    
    this.log('Searching customers with filters', { filters: cleanFilters, pagination, url });
    
    return this.getPaginated<Customer>(url, pagination, cleanFilters);
  }
}