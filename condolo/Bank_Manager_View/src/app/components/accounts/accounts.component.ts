import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="accounts-container">
      <div class="page-header">
        <h1>Gestión de Cuentas</h1>
        <button class="btn btn-primary">
          <span>➕</span>
          Nueva Cuenta
        </button>
      </div>
      
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Lista de Cuentas</h3>
          <div class="search-box">
            <input type="text" class="input" placeholder="Buscar cuentas...">
          </div>
        </div>
        
        <div class="card-content">
          <div class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>Número de Cuenta</th>
                  <th>Cliente</th>
                  <th>Tipo</th>
                  <th>Balance</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let account of accounts">
                  <td>
                    <span class="account-number">{{account.accountNumber}}</span>
                  </td>
                  <td>
                    <div class="customer-info">
                      <strong>{{account.customerName}}</strong>
                      <small>{{account.customerEmail}}</small>
                    </div>
                  </td>
                  <td>
                    <span class="badge badge-info">{{account.type}}</span>
                  </td>
                  <td>
                    <span class="balance" [class.positive]="account.balance > 0" [class.negative]="account.balance < 0">
                      {{account.balance | currency}}
                    </span>
                  </td>
                  <td>
                    <span class="badge" 
                          [class.badge-success]="account.status === 'active'"
                          [class.badge-error]="account.status === 'inactive'"
                          [class.badge-warning]="account.status === 'suspended'">
                      {{getStatusText(account.status)}}
                    </span>
                  </td>
                  <td>
                    <div class="action-buttons">
                      <button class="btn btn-sm btn-outline" title="Ver detalles">
                        👁️
                      </button>
                      <button class="btn btn-sm btn-outline" title="Editar">
                        ✏️
                      </button>
                      <button class="btn btn-sm btn-outline" title="Eliminar">
                        🗑️
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Summary Cards -->
      <div class="summary-grid">
        <div class="summary-card">
          <h4>Total de Cuentas</h4>
          <div class="summary-value">{{accounts.length}}</div>
        </div>
        <div class="summary-card">
          <h4>Balance Total</h4>
          <div class="summary-value">{{getTotalBalance() | currency}}</div>
        </div>
        <div class="summary-card">
          <h4>Cuentas Activas</h4>
          <div class="summary-value">{{getActiveAccounts()}}</div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .accounts-container {
      max-width: 1200px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--spacing-xl);
    }

    .search-box {
      width: 300px;
    }

    .table-container {
      overflow-x: auto;
    }

    .account-number {
      font-family: 'Courier New', monospace;
      font-weight: 600;
      color: var(--primary-color);
    }

    .customer-info {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-xs);
    }

    .customer-info strong {
      color: var(--text-primary);
    }

    .customer-info small {
      color: var(--text-secondary);
    }

    .balance {
      font-weight: 600;
      font-size: 1rem;
    }

    .balance.positive {
      color: var(--success-color);
    }

    .balance.negative {
      color: var(--error-color);
    }

    .action-buttons {
      display: flex;
      gap: var(--spacing-sm);
    }

    .summary-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: var(--spacing-lg);
      margin-top: var(--spacing-xl);
    }

    .summary-card {
      background: var(--card-background);
      padding: var(--spacing-lg);
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      text-align: center;
      border: 1px solid var(--border-color);
    }

    .summary-card h4 {
      margin: 0 0 var(--spacing-md) 0;
      color: var(--text-secondary);
      font-size: 0.875rem;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }

    .summary-value {
      font-size: 1.75rem;
      font-weight: 700;
      color: var(--primary-color);
    }

    @media (max-width: 768px) {
      .page-header {
        flex-direction: column;
        gap: var(--spacing-md);
        align-items: stretch;
      }

      .search-box {
        width: 100%;
      }

      .action-buttons {
        flex-direction: column;
      }
      
      .summary-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class AccountsComponent {
  accounts = [
    {
      accountNumber: '1001-2345-6789',
      customerName: 'Juan Pérez',
      customerEmail: 'juan.perez@email.com',
      type: 'Ahorros',
      balance: 15000,
      status: 'active'
    },
    {
      accountNumber: '1001-2345-6790',
      customerName: 'María García',
      customerEmail: 'maria.garcia@email.com',
      type: 'Corriente',
      balance: 25000,
      status: 'active'
    },
    {
      accountNumber: '1001-2345-6791',
      customerName: 'Carlos López',
      customerEmail: 'carlos.lopez@email.com',
      type: 'Ahorros',
      balance: -500,
      status: 'suspended'
    },
    {
      accountNumber: '1001-2345-6792',
      customerName: 'Ana Martínez',
      customerEmail: 'ana.martinez@email.com',
      type: 'Corriente',
      balance: 8750,
      status: 'active'
    }
  ];

  getStatusText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'active': 'Activa',
      'inactive': 'Inactiva',
      'suspended': 'Suspendida'
    };
    return statusMap[status] || status;
  }

  getTotalBalance(): number {
    return this.accounts.reduce((total, account) => total + account.balance, 0);
  }

  getActiveAccounts(): number {
    return this.accounts.filter(account => account.status === 'active').length;
  }
}