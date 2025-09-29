import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectivityService, ServiceStatus } from '../../services/connectivity.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-cors-status',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="cors-status-panel">
      <div class="cors-header">
        <h3>Estado de Servicios Backend</h3>
        <button 
          class="refresh-btn" 
          (click)="refreshStatus()"
          [disabled]="isRefreshing">
          {{ isRefreshing ? 'Verificando...' : 'Actualizar' }}
        </button>
      </div>
      
      <div class="services-status">
        <div 
          *ngFor="let service of servicesStatus" 
          class="service-item"
          [class.connected]="service.status === 'connected'"
          [class.disconnected]="service.status === 'disconnected'"
          [class.checking]="service.status === 'checking'">
          
          <div class="service-info">
            <div class="service-name">{{ service.name }}</div>
            <div class="service-url">{{ service.url }}</div>
          </div>
          
          <div class="service-status">
            <div class="status-indicator">
              <span class="status-dot"></span>
              <span class="status-text">{{ getStatusText(service.status) }}</span>
            </div>
            
            <div class="service-details" *ngIf="service.status !== 'checking'">
              <div *ngIf="service.responseTime" class="response-time">
                {{ service.responseTime }}ms
              </div>
              <div class="last-checked">
                Último check: {{ service.lastChecked | date:'HH:mm:ss' }}
              </div>
              <div *ngIf="service.error" class="error-message">
                {{ service.error }}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="cors-info" *ngIf="showCorsHelp">
        <h4>🔧 Configuración CORS Requerida</h4>
        <div class="cors-requirements">
          <p><strong>Origen permitido:</strong> http://localhost:4200</p>
          <p><strong>Métodos:</strong> GET, POST, PUT, DELETE, OPTIONS</p>
          <p><strong>Headers:</strong> Content-Type, Accept, Authorization</p>
          <p><strong>Credenciales:</strong> true</p>
        </div>
        
        <div class="spring-boot-config" *ngIf="hasDisconnectedServices">
          <h5>Configuración CORS requerida en Spring Boot:</h5>
          <div class="config-steps">
            <p>1. Crear clase CorsConfig con &#64;Configuration</p>
            <p>2. Implementar WebMvcConfigurer</p>
            <p>3. Permitir origen: http://localhost:4200</p>
            <p>4. Métodos: GET, POST, PUT, DELETE, OPTIONS</p>
            <p>5. Headers: * (todos)</p>
            <p>6. Credenciales: true</p>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .cors-status-panel {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
      margin: 20px 0;
    }

    .cors-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .cors-header h3 {
      margin: 0;
      color: #333;
    }

    .refresh-btn {
      padding: 8px 16px;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      transition: background 0.3s;
    }

    .refresh-btn:hover:not(:disabled) {
      background: #0056b3;
    }

    .refresh-btn:disabled {
      background: #ccc;
      cursor: not-allowed;
    }

    .services-status {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .service-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 15px;
      border-radius: 6px;
      border: 2px solid #ddd;
      transition: all 0.3s;
    }

    .service-item.connected {
      border-color: #28a745;
      background-color: #f8fff9;
    }

    .service-item.disconnected {
      border-color: #dc3545;
      background-color: #fff8f8;
    }

    .service-item.checking {
      border-color: #ffc107;
      background-color: #fffcf0;
    }

    .service-info {
      flex: 1;
    }

    .service-name {
      font-weight: bold;
      color: #333;
      margin-bottom: 4px;
    }

    .service-url {
      font-size: 0.9em;
      color: #666;
      font-family: monospace;
    }

    .service-status {
      text-align: right;
    }

    .status-indicator {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 5px;
    }

    .status-dot {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      display: inline-block;
    }

    .connected .status-dot {
      background-color: #28a745;
    }

    .disconnected .status-dot {
      background-color: #dc3545;
    }

    .checking .status-dot {
      background-color: #ffc107;
      animation: pulse 1.5s infinite;
    }

    @keyframes pulse {
      0% { opacity: 1; }
      50% { opacity: 0.5; }
      100% { opacity: 1; }
    }

    .status-text {
      font-weight: bold;
      text-transform: uppercase;
      font-size: 0.9em;
    }

    .connected .status-text {
      color: #28a745;
    }

    .disconnected .status-text {
      color: #dc3545;
    }

    .checking .status-text {
      color: #ffc107;
    }

    .service-details {
      font-size: 0.85em;
      color: #666;
    }

    .response-time {
      color: #28a745;
      font-weight: bold;
    }

    .error-message {
      color: #dc3545;
      margin-top: 4px;
      font-weight: bold;
    }

    .cors-info {
      margin-top: 25px;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 6px;
      border-left: 4px solid #007bff;
    }

    .cors-info h4 {
      margin: 0 0 15px 0;
      color: #007bff;
    }

    .cors-info h5 {
      margin: 15px 0 10px 0;
      color: #333;
    }

    .cors-requirements p {
      margin: 5px 0;
      font-family: monospace;
      font-size: 0.9em;
    }

    .spring-boot-config {
      margin-top: 20px;
    }

    .config-steps {
      background: #f8f9fa;
      border: 1px solid #dee2e6;
      border-radius: 4px;
      padding: 15px;
      margin: 10px 0;
    }

    .config-steps p {
      margin: 8px 0;
      font-family: monospace;
      font-size: 0.9em;
      color: #495057;
    }
  `]
})
export class CorsStatusComponent implements OnInit, OnDestroy {
  servicesStatus: ServiceStatus[] = [];
  isRefreshing = false;
  showCorsHelp = true;
  
  private subscription: Subscription = new Subscription();

  constructor(private connectivityService: ConnectivityService) {}

  ngOnInit(): void {
    // Suscribirse a los cambios de estado
    this.subscription.add(
      this.connectivityService.getServicesStatus().subscribe(
        (status) => {
          this.servicesStatus = status;
          this.isRefreshing = false;
        }
      )
    );

    // Hacer una verificación inicial
    this.refreshStatus();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  refreshStatus(): void {
    this.isRefreshing = true;
    this.connectivityService.checkAllServices();
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'connected':
        return 'Conectado';
      case 'disconnected':
        return 'Desconectado';
      case 'checking':
        return 'Verificando...';
      default:
        return 'Desconocido';
    }
  }

  get hasDisconnectedServices(): boolean {
    return this.servicesStatus.some(service => service.status === 'disconnected');
  }
}