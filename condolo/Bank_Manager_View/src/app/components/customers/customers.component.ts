import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { Customer, CreateCustomerRequest } from '../../models/bank.models';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="customers-container">
      <div class="page-header">
        <h1>Gestión de Clientes</h1>
        <button class="btn btn-primary" (click)="showNewCustomerForm = true">
          <span>👤</span>
          Nuevo Cliente
        </button>
      </div>
      
      <div *ngIf="showNewCustomerForm" class="card form-card">
        <div class="card-header">
          <h3 class="card-title">Nuevo Cliente</h3>
          <button class="btn btn-outline btn-sm" (click)="cancelNewCustomer()">✕</button>
        </div>
        <div class="card-content">
          <form (ngSubmit)="createCustomer()" #customerForm="ngForm">
            <div class="form-grid">
              <div class="form-group">
                <label for="name">Nombre Completo</label>
                <input type="text" id="name" class="input" [(ngModel)]="newCustomer.name" name="name" required>
              </div>
              <div class="form-group">
                <label for="identification">Identificación</label>
                <input type="text" id="identification" class="input" [(ngModel)]="newCustomer.identification" name="identification" required>
              </div>
              <div class="form-group">
                <label for="gender">Género</label>
                <select id="gender" class="input" [(ngModel)]="newCustomer.gender" name="gender" required>
                  <option value="">Seleccionar...</option>
                  <option value="M">Masculino</option>
                  <option value="F">Femenino</option>
                </select>
              </div>
              <div class="form-group">
                <label for="age">Edad</label>
                <input type="number" id="age" class="input" [(ngModel)]="newCustomer.age" name="age" required>
              </div>
              <div class="form-group">
                <label for="address">Dirección</label>
                <input type="text" id="address" class="input" [(ngModel)]="newCustomer.address" name="address" required>
              </div>
              <div class="form-group">
                <label for="phone">Teléfono</label>
                <input type="text" id="phone" class="input" [(ngModel)]="newCustomer.phone" name="phone" required>
              </div>
              <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" class="input" [(ngModel)]="newCustomer.password" name="password" required>
              </div>
              <div class="form-group">
                <label for="status">Estado</label>
                <select id="status" class="input" [(ngModel)]="newCustomer.status" name="status" required>
                  <option value="A">Activo</option>
                  <option value="I">Inactivo</option>
                </select>
              </div>
            </div>
            <div class="form-actions">
              <button type="button" class="btn btn-outline" (click)="cancelNewCustomer()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!customerForm.valid || creating">
                {{ creating ? 'Creando...' : 'Crear Cliente' }}
              </button>
            </div>
          </form>
        </div>
      </div>
      
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Lista de Clientes</h3>
          <div class="search-box">
            <input type="text" class="input" placeholder="Buscar clientes..." [(ngModel)]="searchTerm" (input)="filterCustomers()">
          </div>
        </div>
        
        <div class="card-content">
          <div *ngIf="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p>Cargando clientes...</p>
          </div>
          
          <div *ngIf="!loading" class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nombre Completo</th>
                  <th>Identificación</th>
                  <th>Teléfono</th>
                  <th>Género</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let customer of filteredCustomers">
                  <td>
                    <span class="customer-id">{{customer.customerId}}</span>
                  </td>
                  <td>
                    <div class="customer-info">
                      <strong>{{customer.name}}</strong>
                      <small>{{customer.age}} años</small>
                    </div>
                  </td>
                  <td>{{customer.identification}}</td>
                  <td>{{customer.phone}}</td>
                  <td>
                    <span class="badge" [class]="'badge-' + (customer.status === 'A' ? 'success' : 'error')">
                      {{customer.status === 'A' ? 'Activo' : 'Inactivo'}}
                    </span>
                  </td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn btn-sm btn-outline" title="Ver perfil" (click)="viewCustomer(customer)">
                        👁️
                      </button>
                      <button class="btn btn-sm btn-outline" title="Editar" (click)="editCustomer(customer)">
                        ✏️
                      </button>
                      <button class="btn btn-sm btn-outline" title="Eliminar" (click)="deleteCustomer(customer.customerId)">
                        🗑️
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div *ngIf="error" class="error-container">
            <p class="error-message">{{ error }}</p>
            <button class="btn btn-primary" (click)="loadCustomers()">Reintentar</button>
          </div>
        </div>
      </div>

      <!-- Customer Stats -->
      <div class="stats-grid" *ngIf="!loading">
        <div class="stat-card">
          <h4>Total Clientes</h4>
          <div class="stat-value">{{customers.length}}</div>
        </div>
        <div class="stat-card">
          <h4>Clientes Activos</h4>
          <div class="stat-value">{{getActiveCustomers()}}</div>
        </div>
        <div class="stat-card">
          <h4>Género Masculino</h4>
          <div class="stat-value">{{getCustomersByGender('M')}}</div>
        </div>
        <div class="stat-card">
          <h4>Género Femenino</h4>
          <div class="stat-value">{{getCustomersByGender('F')}}</div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .customers-container {
      max-width: 1400px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--spacing-xl);
    }

    .form-card {
      margin-bottom: var(--spacing-xl);
    }

    .form-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: var(--spacing-lg);
      margin-bottom: var(--spacing-lg);
    }

    .form-group {
      display: flex;
      flex-direction: column;
    }

    .form-group label {
      margin-bottom: var(--spacing-sm);
      font-weight: 600;
      color: var(--text-primary);
    }

    .form-actions {
      display: flex;
      gap: var(--spacing-md);
      justify-content: flex-end;
    }

    .search-box {
      width: 300px;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: var(--spacing-xl);
      color: var(--text-secondary);
    }

    .loading-spinner {
      width: 40px;
      height: 40px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid var(--primary-color);
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin-bottom: var(--spacing-md);
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .error-container {
      text-align: center;
      padding: var(--spacing-xl);
    }

    .error-message {
      color: var(--error-color);
      margin-bottom: var(--spacing-lg);
    }

    .table-container {
      overflow-x: auto;
    }

    .customer-id {
      font-family: 'Courier New', monospace;
      font-weight: 600;
      color: var(--primary-color);
    }

    .customer-info {
      display: flex;
      flex-direction: column;
    }

    .customer-info strong {
      color: var(--text-primary);
    }

    .customer-info small {
      color: var(--text-secondary);
      font-size: 0.8rem;
    }

    .action-buttons {
      display: flex;
      gap: var(--spacing-sm);
    }

    .badge-success {
      background-color: var(--success-color);
      color: white;
    }

    .badge-error {
      background-color: var(--error-color);
      color: white;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: var(--spacing-lg);
      margin-top: var(--spacing-xl);
    }

    .stat-card {
      background: var(--card-background);
      border-radius: var(--radius);
      padding: var(--spacing-lg);
      box-shadow: var(--shadow);
      border: 1px solid var(--border-color);
      text-align: center;
    }

    .stat-card h4 {
      font-size: 0.875rem;
      color: var(--text-secondary);
      margin: 0 0 var(--spacing-md) 0;
      font-weight: 500;
    }

    .stat-value {
      font-size: 2rem;
      font-weight: 700;
      color: var(--primary-color);
    }

    @media (max-width: 768px) {
      .page-header {
        flex-direction: column;
        gap: var(--spacing-lg);
        align-items: stretch;
      }

      .search-box {
        width: 100%;
      }

      .form-grid {
        grid-template-columns: 1fr;
        gap: var(--spacing-md);
      }

      .stats-grid {
        grid-template-columns: repeat(2, 1fr);
        gap: var(--spacing-md);
      }

      .action-buttons {
        flex-direction: column;
      }
    }
  `]
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];
  filteredCustomers: Customer[] = [];
  loading = true;
  creating = false;
  error: string | null = null;
  searchTerm = '';
  showNewCustomerForm = false;
  
  newCustomer: CreateCustomerRequest = {
    name: '',
    gender: '',
    age: 0,
    identification: '',
    address: '',
    phone: '',
    password: '',
    status: 'A'
  };

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.loading = true;
    this.error = null;
    
    this.customerService.getAllCustomers().subscribe({
      next: (response) => {
        this.customers = response.content;
        this.filteredCustomers = response.content;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading customers:', error);
        this.error = 'Error al cargar los clientes.';
        this.loading = false;
      }
    });
  }

  filterCustomers(): void {
    if (!this.searchTerm) {
      this.filteredCustomers = this.customers;
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredCustomers = this.customers.filter(customer =>
        customer.name?.toLowerCase().includes(term) ||
        customer.identification?.toLowerCase().includes(term) ||
        customer.phone?.toLowerCase().includes(term)
      );
    }
  }

  createCustomer(): void {
    this.creating = true;
    
    this.customerService.createCustomer(this.newCustomer).subscribe({
      next: (customer) => {
        this.customers.unshift(customer);
        this.filterCustomers();
        this.cancelNewCustomer();
        this.creating = false;
      },
      error: (error) => {
        console.error('Error creating customer:', error);
        this.error = 'Error al crear el cliente.';
        this.creating = false;
      }
    });
  }

  viewCustomer(customer: Customer): void {
    alert(`Ver detalles del cliente: ${customer.name || 'N/A'}`);
  }

  editCustomer(customer: Customer): void {
    alert(`Editar cliente: ${customer.name || 'N/A'}`);
  }

  deleteCustomer(customerId: string): void {
    if (confirm('¿Está seguro de que desea eliminar este cliente?')) {
      this.customerService.deleteCustomer(customerId).subscribe({
        next: () => {
          this.customers = this.customers.filter(c => c.customerId !== customerId);
          this.filterCustomers();
        },
        error: (error) => {
          console.error('Error deleting customer:', error);
          this.error = 'Error al eliminar el cliente.';
        }
      });
    }
  }

  cancelNewCustomer(): void {
    this.showNewCustomerForm = false;
    this.newCustomer = {
      name: '',
      gender: '',
      age: 0,
      identification: '',
      address: '',
      phone: '',
      password: '',
      status: 'A'
    };
  }

  getActiveCustomers(): number {
    return this.customers.filter(c => c.status === 'A').length;
  }

  getCustomersByGender(gender: string): number {
    return this.customers.filter(c => c.gender === gender).length;
  }
}