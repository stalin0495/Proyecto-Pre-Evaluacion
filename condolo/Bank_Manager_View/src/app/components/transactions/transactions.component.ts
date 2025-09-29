import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="transactions-container">
      <div class="page-header">
        <h1>Historial de Transacciones</h1>
        <div class="header-actions">
          <select class="input">
            <option value="">Todos los tipos</option>
            <option value="credit">Crédito</option>
            <option value="debit">Débito</option>
          </select>
          <button class="btn btn-primary">
            <span>📄</span>
            Exportar
          </button>
        </div>
      </div>
      
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">Transacciones Recientes</h3>
          <div class="search-box">
            <input type="text" class="input" placeholder="Buscar transacciones...">
          </div>
        </div>
        
        <div class="card-content">
          <div class="table-container">
            <table class="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Fecha</th>
                  <th>Tipo</th>
                  <th>Monto</th>
                  <th>Cuenta</th>
                  <th>Descripción</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let transaction of transactions">
                  <td>
                    <span class="transaction-id">{{transaction.id}}</span>
                  </td>
                  <td>
                    <div class="date-info">
                      <strong>{{transaction.date | date:'short'}}</strong>
                    </div>
                  </td>
                  <td>
                    <span class="badge" 
                          [class.badge-success]="transaction.type === 'credit'"
                          [class.badge-error]="transaction.type === 'debit'">
                      {{getTypeText(transaction.type)}}
                    </span>
                  </td>
                  <td>
                    <span class="amount" 
                          [class.credit]="transaction.type === 'credit'" 
                          [class.debit]="transaction.type === 'debit'">
                      {{transaction.type === 'credit' ? '+' : '-'}}{{transaction.amount | currency}}
                    </span>
                  </td>
                  <td>
                    <span class="account-number">{{transaction.account}}</span>
                  </td>
                  <td>
                    <span class="description">{{transaction.description}}</span>
                  </td>
                  <td>
                    <span class="badge"
                          [class.badge-success]="transaction.status === 'completed'"
                          [class.badge-warning]="transaction.status === 'pending'"
                          [class.badge-error]="transaction.status === 'failed'">
                      {{getStatusText(transaction.status)}}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Transaction Summary -->
      <div class="summary-grid">
        <div class="summary-card">
          <h4>Total Créditos</h4>
          <div class="summary-value positive">+{{getTotalCredits() | currency}}</div>
        </div>
        <div class="summary-card">
          <h4>Total Débitos</h4>
          <div class="summary-value negative">-{{getTotalDebits() | currency}}</div>
        </div>
        <div class="summary-card">
          <h4>Balance Neto</h4>
          <div class="summary-value" [class.positive]="getNetBalance() > 0" [class.negative]="getNetBalance() < 0">
            {{getNetBalance() | currency}}
          </div>
        </div>
        <div class="summary-card">
          <h4>Transacciones Hoy</h4>
          <div class="summary-value">{{getTransactionsToday()}}</div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .transactions-container {
      max-width: 1400px;
      margin: 0 auto;
    }

    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--spacing-xl);
    }

    .header-actions {
      display: flex;
      gap: var(--spacing-md);
      align-items: center;
    }

    .header-actions select {
      width: 200px;
    }

    .search-box {
      width: 300px;
    }

    .table-container {
      overflow-x: auto;
    }

    .transaction-id {
      font-family: 'Courier New', monospace;
      font-weight: 600;
      color: var(--primary-color);
    }

    .date-info strong {
      color: var(--text-primary);
    }

    .amount {
      font-weight: 700;
      font-size: 1rem;
    }

    .amount.credit {
      color: var(--success-color);
    }

    .amount.debit {
      color: var(--error-color);
    }

    .account-number {
      font-family: 'Courier New', monospace;
      font-size: 0.875rem;
      color: var(--text-secondary);
    }

    .description {
      color: var(--text-primary);
      max-width: 200px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .summary-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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

    .summary-value.positive {
      color: var(--success-color);
    }

    .summary-value.negative {
      color: var(--error-color);
    }

    @media (max-width: 768px) {
      .page-header {
        flex-direction: column;
        gap: var(--spacing-md);
        align-items: stretch;
      }

      .header-actions {
        justify-content: space-between;
      }

      .header-actions select {
        width: auto;
        flex: 1;
      }

      .search-box {
        width: 100%;
      }

      .summary-grid {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (max-width: 480px) {
      .summary-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class TransactionsComponent {
  transactions = [
    {
      id: 'TXN001',
      date: new Date(),
      type: 'credit',
      amount: 1000,
      account: '1001-2345-6789',
      description: 'Depósito en efectivo',
      status: 'completed'
    },
    {
      id: 'TXN002',
      date: new Date(Date.now() - 3600000),
      type: 'debit',
      amount: 500,
      account: '1001-2345-6789',
      description: 'Retiro ATM',
      status: 'completed'
    },
    {
      id: 'TXN003',
      date: new Date(Date.now() - 7200000),
      type: 'credit',
      amount: 2500,
      account: '1001-2345-6790',
      description: 'Transferencia recibida',
      status: 'completed'
    },
    {
      id: 'TXN004',
      date: new Date(Date.now() - 10800000),
      type: 'debit',
      amount: 750,
      account: '1001-2345-6791',
      description: 'Pago de servicio',
      status: 'pending'
    },
    {
      id: 'TXN005',
      date: new Date(Date.now() - 14400000),
      type: 'credit',
      amount: 3000,
      account: '1001-2345-6792',
      description: 'Depósito por transferencia',
      status: 'completed'
    }
  ];

  getTypeText(type: string): string {
    return type === 'credit' ? 'Crédito' : 'Débito';
  }

  getStatusText(status: string): string {
    const statusMap: { [key: string]: string } = {
      'completed': 'Completada',
      'pending': 'Pendiente',
      'failed': 'Fallida'
    };
    return statusMap[status] || status;
  }

  getTotalCredits(): number {
    return this.transactions
      .filter(t => t.type === 'credit')
      .reduce((total, t) => total + t.amount, 0);
  }

  getTotalDebits(): number {
    return this.transactions
      .filter(t => t.type === 'debit')
      .reduce((total, t) => total + t.amount, 0);
  }

  getNetBalance(): number {
    return this.getTotalCredits() - this.getTotalDebits();
  }

  getTransactionsToday(): number {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return this.transactions.filter(t => {
      const transactionDate = new Date(t.date);
      transactionDate.setHours(0, 0, 0, 0);
      return transactionDate.getTime() === today.getTime();
    }).length;
  }
}