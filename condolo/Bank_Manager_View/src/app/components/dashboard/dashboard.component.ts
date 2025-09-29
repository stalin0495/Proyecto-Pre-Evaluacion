import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
// import { CorsStatusComponent } from '../cors-status/cors-status.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="dashboard-container">
      <h1>🏦 Dashboard - Bank Manager View</h1>
      
      <!-- Mensaje de bienvenida simple -->
      <div class="welcome-message">
        <h2>¡Bienvenido al Sistema Bancario!</h2>
        <p>La aplicación se ha cargado correctamente.</p>
      </div>
      
      <div class="metrics-grid">
        <div class="metric-card">
          <div class="metric-header">
            <span class="metric-icon">🏦</span>
            <h3>Cuentas Totales</h3>
          </div>
          <div class="metric-value">{{ stats.totalAccounts }}</div>
          <div class="metric-change positive">{{ stats.activeAccounts }} activas</div>
        </div>

        <div class="metric-card">
          <div class="metric-header">
            <span class="metric-icon">👥</span>
            <h3>Clientes</h3>
          </div>
          <div class="metric-value">{{ stats.totalCustomers }}</div>
          <div class="metric-change positive">{{ stats.activeCustomers }} activos</div>
        </div>

        <div class="metric-card">
          <div class="metric-header">
            <span class="metric-icon">📋</span>
            <h3>Transacciones Hoy</h3>
          </div>
          <div class="metric-value">{{ stats.todayTransactions }}</div>
          <div class="metric-change positive">{{ stats.totalTransactions }} total</div>
        </div>

        <div class="metric-card">
          <div class="metric-header">
            <span class="metric-icon">💰</span>
            <h3>Balance Total</h3>
          </div>
          <div class="metric-value">{{ '$' + stats.totalBalance.toLocaleString() }}</div>
          <div class="metric-change positive">+{{ stats.monthlyGrowth }}% este mes</div>
        </div>
      </div>

      <div class="dashboard-section">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Actividad Reciente</h3>
            <button class="btn btn-outline btn-sm">Ver todo</button>
          </div>
          <div class="card-content">
            <div class="activity-list">
              <div class="activity-item" *ngFor="let activity of recentActivity">
                <span class="activity-icon">{{ activity.icon }}</span>
                <div class="activity-details">
                  <p><strong>{{ activity.title }}</strong></p>
                  <small>{{ activity.description }}</small>
                </div>
                <span class="activity-time">{{ activity.time }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 20px;
    }

    .welcome-message {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 30px;
      border-radius: 10px;
      text-align: center;
      margin-bottom: 30px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .welcome-message h2 {
      margin: 0 0 10px 0;
      font-size: 2rem;
    }

    .welcome-message p {
      margin: 0;
      font-size: 1.1rem;
      opacity: 0.9;
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

    .metrics-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: var(--spacing-lg);
      margin-bottom: var(--spacing-xl);
    }

    .metric-card {
      background: var(--card-background);
      border-radius: var(--radius);
      padding: var(--spacing-lg);
      box-shadow: var(--shadow);
      border: 1px solid var(--border-color);
    }

    .metric-header {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
      margin-bottom: var(--spacing-md);
    }

    .metric-icon {
      font-size: 2rem;
      width: 48px;
      height: 48px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f0f9ff;
      border-radius: var(--radius);
    }

    .metric-header h3 {
      font-size: 0.875rem;
      color: var(--text-secondary);
      font-weight: 500;
      margin: 0;
    }

    .metric-value {
      font-size: 2rem;
      font-weight: 700;
      color: var(--text-primary);
      margin-bottom: var(--spacing-sm);
    }

    .metric-change {
      font-size: 0.875rem;
      font-weight: 500;
    }

    .metric-change.positive {
      color: var(--success-color);
    }

    .metric-change.negative {
      color: var(--error-color);
    }

    .dashboard-section {
      margin-bottom: var(--spacing-xl);
    }

    .activity-list {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-md);
    }

    .activity-item {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
      padding: var(--spacing-md);
      border-radius: var(--radius);
      background: #f8fafc;
    }

    .activity-icon {
      font-size: 1.5rem;
      width: 40px;
      height: 40px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--card-background);
      border-radius: var(--radius);
      box-shadow: var(--shadow);
    }

    .activity-details {
      flex: 1;
    }

    .activity-details p {
      margin: 0 0 var(--spacing-xs) 0;
      font-weight: 500;
      color: var(--text-primary);
    }

    .activity-details small {
      color: var(--text-secondary);
    }

    .activity-time {
      font-size: 0.875rem;
      color: var(--text-secondary);
    }

    @media (max-width: 768px) {
      .metrics-grid {
        grid-template-columns: 1fr;
        gap: var(--spacing-md);
      }

      .metric-value {
        font-size: 1.75rem;
      }
      
      .activity-item {
        flex-direction: column;
        align-items: flex-start;
        text-align: left;
      }
    }
  `]
})
export class DashboardComponent {
  stats = {
    totalCustomers: 150,
    activeCustomers: 142,
    totalAccounts: 89,
    activeAccounts: 85,
    totalTransactions: 456,
    todayTransactions: 23,
    totalBalance: 2456789.50,
    monthlyGrowth: 5.2
  };
  
  recentActivity = [
    {
      icon: '💳',
      title: 'Nueva cuenta creada',
      description: 'Cuenta de ahorros - Juan Pérez',
      time: 'Hace 5 min'
    },
    {
      icon: '💸',
      title: 'Transferencia procesada',
      description: '$5,000.00 - María García',
      time: 'Hace 15 min'
    },
    {
      icon: '📊',
      title: 'Reporte generado',
      description: 'Reporte mensual de transacciones',
      time: 'Hace 1 hora'
    }
  ];
}