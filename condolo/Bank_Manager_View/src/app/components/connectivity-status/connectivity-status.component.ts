import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectivityService, ServiceStatus } from '../../services/connectivity.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-connectivity-status',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="connectivity-container" [class.collapsed]="isCollapsed">
      <div class="connectivity-header" (click)="toggleCollapsed()">
        <div class="status-indicator" [class]="getOverallStatusClass()">
          <span class="status-dot"></span>
          <span class="status-text">{{ getOverallStatusText() }}</span>
        </div>
        <button class="toggle-btn" [attr.aria-label]="isCollapsed ? 'Expandir' : 'Contraer'">
          {{ isCollapsed ? '▼' : '▲' }}
        </button>
      </div>
      
      <div class="services-list" *ngIf="!isCollapsed">
        <div class="service-item" *ngFor="let service of servicesStatus">
          <div class="service-info">
            <div class="service-name">
              <span class="service-dot" [class]="'dot-' + service.status"></span>
              {{ service.name }}
            </div>
            <div class="service-details">
              <small class="service-url">{{ service.url }}</small>
              <small class="service-time" *ngIf="service.responseTime">
                {{ service.responseTime }}ms
              </small>
            </div>
          </div>
          
          <div class="service-status">
            <span class="status-badge" [class]="'badge-' + service.status">
              {{ getStatusText(service.status) }}
            </span>
            <button 
              class="retry-btn" 
              *ngIf="service.status === 'disconnected'"
              (click)="retryService(service.name)"
              [disabled]="retrying.includes(service.name)">
              {{ retrying.includes(service.name) ? '⟳' : '↻' }}
            </button>
          </div>
          
          <div class="service-error" *ngIf="service.error && service.status === 'disconnected'">
            <small>{{ service.error }}</small>
          </div>
        </div>
        
        <div class="connectivity-actions">
          <button class="btn btn-sm btn-outline" (click)="refreshAll()" [disabled]="refreshing">
            {{ refreshing ? '⟳ Verificando...' : '🔄 Verificar Todo' }}
          </button>
          <small class="last-check">
            Última verificación: {{ getLastCheckTime() }}
          </small>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .connectivity-container {
      position: fixed;
      top: 20px;
      right: 20px;
      background: var(--card-background);
      border: 1px solid var(--border-color);
      border-radius: var(--radius);
      box-shadow: var(--shadow);
      z-index: 1000;
      min-width: 280px;
      max-width: 350px;
      transition: all 0.3s ease;
    }

    .connectivity-container.collapsed {
      max-width: 200px;
    }

    .connectivity-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: var(--spacing-md);
      cursor: pointer;
      user-select: none;
      background: rgba(0,0,0,0.02);
      border-radius: var(--radius) var(--radius) 0 0;
    }

    .connectivity-header:hover {
      background: rgba(0,0,0,0.05);
    }

    .status-indicator {
      display: flex;
      align-items: center;
      gap: var(--spacing-sm);
    }

    .status-dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      display: inline-block;
      animation: pulse 2s infinite;
    }

    @keyframes pulse {
      0% { opacity: 1; }
      50% { opacity: 0.7; }
      100% { opacity: 1; }
    }

    .status-indicator.connected .status-dot {
      background-color: var(--success-color);
    }

    .status-indicator.disconnected .status-dot {
      background-color: var(--error-color);
    }

    .status-indicator.partial .status-dot {
      background-color: var(--warning-color);
    }

    .status-indicator.checking .status-dot {
      background-color: var(--info-color);
      animation: pulse 1s infinite;
    }

    .status-text {
      font-weight: 600;
      font-size: 0.875rem;
    }

    .toggle-btn {
      background: none;
      border: none;
      font-size: 0.75rem;
      color: var(--text-secondary);
      cursor: pointer;
      padding: var(--spacing-xs);
      border-radius: var(--radius);
      transition: all 0.2s;
    }

    .toggle-btn:hover {
      background: rgba(0,0,0,0.1);
      color: var(--text-primary);
    }

    .services-list {
      padding: var(--spacing-md);
      border-top: 1px solid var(--border-color);
      max-height: 400px;
      overflow-y: auto;
    }

    .service-item {
      display: flex;
      flex-direction: column;
      gap: var(--spacing-xs);
      padding: var(--spacing-sm) 0;
      border-bottom: 1px solid #f0f0f0;
    }

    .service-item:last-child {
      border-bottom: none;
    }

    .service-info {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
    }

    .service-name {
      display: flex;
      align-items: center;
      gap: var(--spacing-xs);
      font-weight: 500;
      font-size: 0.875rem;
    }

    .service-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      display: inline-block;
    }

    .dot-connected {
      background-color: var(--success-color);
    }

    .dot-disconnected {
      background-color: var(--error-color);
    }

    .dot-checking {
      background-color: var(--info-color);
      animation: pulse 1s infinite;
    }

    .service-details {
      display: flex;
      flex-direction: column;
      align-items: flex-end;
      gap: 2px;
    }

    .service-url {
      color: var(--text-secondary);
      font-size: 0.75rem;
      max-width: 150px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .service-time {
      color: var(--info-color);
      font-size: 0.75rem;
      font-weight: 500;
    }

    .service-status {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .status-badge {
      font-size: 0.75rem;
      padding: 2px 6px;
      border-radius: 4px;
      font-weight: 500;
    }

    .badge-connected {
      background-color: var(--success-color);
      color: white;
    }

    .badge-disconnected {
      background-color: var(--error-color);
      color: white;
    }

    .badge-checking {
      background-color: var(--info-color);
      color: white;
    }

    .retry-btn {
      background: none;
      border: 1px solid var(--border-color);
      border-radius: 4px;
      padding: 2px 6px;
      cursor: pointer;
      font-size: 0.75rem;
      color: var(--text-secondary);
      transition: all 0.2s;
    }

    .retry-btn:hover:not(:disabled) {
      border-color: var(--primary-color);
      color: var(--primary-color);
    }

    .retry-btn:disabled {
      opacity: 0.5;
      cursor: not-allowed;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      from { transform: rotate(0deg); }
      to { transform: rotate(360deg); }
    }

    .service-error {
      margin-top: var(--spacing-xs);
      padding: var(--spacing-xs);
      background-color: #fff5f5;
      border: 1px solid #fed7d7;
      border-radius: 4px;
    }

    .service-error small {
      color: var(--error-color);
      font-size: 0.75rem;
    }

    .connectivity-actions {
      margin-top: var(--spacing-md);
      padding-top: var(--spacing-md);
      border-top: 1px solid var(--border-color);
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .last-check {
      color: var(--text-secondary);
      font-size: 0.75rem;
    }

    @media (max-width: 768px) {
      .connectivity-container {
        position: relative;
        top: auto;
        right: auto;
        margin: var(--spacing-md) 0;
        width: 100%;
        max-width: none;
      }
    }
  `]
})
export class ConnectivityStatusComponent implements OnInit, OnDestroy {
  servicesStatus: ServiceStatus[] = [];
  isCollapsed = false;
  refreshing = false;
  retrying: string[] = [];
  private subscription?: Subscription;

  constructor(private connectivityService: ConnectivityService) {}

  ngOnInit(): void {
    this.subscription = this.connectivityService.getServicesStatus().subscribe(
      status => {
        this.servicesStatus = status;
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  toggleCollapsed(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  getOverallStatusClass(): string {
    if (this.servicesStatus.length === 0) return 'checking';
    
    const connected = this.servicesStatus.filter(s => s.status === 'connected').length;
    const total = this.servicesStatus.length;
    
    if (connected === total) return 'connected';
    if (connected === 0) return 'disconnected';
    return 'partial';
  }

  getOverallStatusText(): string {
    if (this.servicesStatus.length === 0) return 'Verificando...';
    
    const connected = this.servicesStatus.filter(s => s.status === 'connected').length;
    const total = this.servicesStatus.length;
    
    if (connected === total) return 'Todos Conectados';
    if (connected === 0) return 'Sin Conexión';
    return `${connected}/${total} Conectados`;
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'connected': return 'Conectado';
      case 'disconnected': return 'Desconectado';
      case 'checking': return 'Verificando';
      default: return 'Desconocido';
    }
  }

  retryService(serviceName: string): void {
    this.retrying.push(serviceName);
    
    this.connectivityService.retryConnection(serviceName).subscribe({
      next: () => {
        this.retrying = this.retrying.filter(name => name !== serviceName);
      },
      error: () => {
        this.retrying = this.retrying.filter(name => name !== serviceName);
      }
    });
  }

  refreshAll(): void {
    this.refreshing = true;
    this.connectivityService.checkAllServices();
    
    // Simular un delay mínimo para UX
    setTimeout(() => {
      this.refreshing = false;
    }, 1500);
  }

  getLastCheckTime(): string {
    if (this.servicesStatus.length === 0) return 'Nunca';
    
    const latest = this.servicesStatus.reduce((latest, service) => {
      return service.lastChecked > latest ? service.lastChecked : latest;
    }, new Date(0));
    
    return latest.toLocaleTimeString();
  }
}