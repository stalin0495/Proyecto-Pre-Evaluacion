import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-connection-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="alert-container" *ngIf="show" [class]="'alert-' + type">
      <div class="alert-content">
        <div class="alert-icon">
          <span *ngIf="type === 'error'">⚠️</span>
          <span *ngIf="type === 'warning'">⚠️</span>
          <span *ngIf="type === 'info'">ℹ️</span>
          <span *ngIf="type === 'success'">✅</span>
        </div>
        
        <div class="alert-message">
          <h4 *ngIf="title">{{ title }}</h4>
          <p>{{ message }}</p>
          <div class="alert-actions" *ngIf="showActions">
            <button class="btn btn-sm btn-outline" (click)="onRetry()" *ngIf="showRetry">
              Reintentar
            </button>
            <button class="btn btn-sm btn-outline" (click)="onOfflineMode()" *ngIf="showOfflineMode">
              Modo Offline
            </button>
            <button class="btn btn-sm btn-outline" (click)="onClose()">
              {{ closeText || 'Cerrar' }}
            </button>
          </div>
        </div>
        
        <button class="alert-close" (click)="onClose()" *ngIf="closable">
          ✕
        </button>
      </div>
    </div>
  `,
  styles: [`
    .alert-container {
      position: fixed;
      top: 80px;
      left: 50%;
      transform: translateX(-50%);
      z-index: 1001;
      max-width: 500px;
      width: 90%;
      border-radius: var(--radius);
      box-shadow: var(--shadow-lg);
      animation: slideIn 0.3s ease-out;
    }

    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateX(-50%) translateY(-20px);
      }
      to {
        opacity: 1;
        transform: translateX(-50%) translateY(0);
      }
    }

    .alert-content {
      display: flex;
      align-items: flex-start;
      padding: var(--spacing-lg);
      gap: var(--spacing-md);
    }

    .alert-icon {
      font-size: 1.5rem;
      flex-shrink: 0;
      margin-top: 2px;
    }

    .alert-message {
      flex: 1;
    }

    .alert-message h4 {
      margin: 0 0 var(--spacing-sm) 0;
      font-size: 1rem;
      font-weight: 600;
    }

    .alert-message p {
      margin: 0 0 var(--spacing-md) 0;
      font-size: 0.875rem;
      line-height: 1.4;
    }

    .alert-actions {
      display: flex;
      gap: var(--spacing-sm);
      flex-wrap: wrap;
    }

    .alert-close {
      background: none;
      border: none;
      font-size: 1.25rem;
      cursor: pointer;
      padding: var(--spacing-xs);
      border-radius: 50%;
      width: 24px;
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0.7;
      transition: all 0.2s;
      flex-shrink: 0;
    }

    .alert-close:hover {
      opacity: 1;
      background: rgba(0,0,0,0.1);
    }

    .alert-error {
      background-color: #fee;
      border: 1px solid #fcc;
      color: #c53030;
    }

    .alert-warning {
      background-color: #fffbeb;
      border: 1px solid #fed7aa;
      color: #92400e;
    }

    .alert-info {
      background-color: #eff6ff;
      border: 1px solid #bfdbfe;
      color: #1d4ed8;
    }

    .alert-success {
      background-color: #f0fff4;
      border: 1px solid #9ae6b4;
      color: #276749;
    }

    @media (max-width: 768px) {
      .alert-container {
        top: 60px;
        width: 95%;
      }

      .alert-content {
        padding: var(--spacing-md);
      }

      .alert-actions {
        flex-direction: column;
      }

      .alert-actions .btn {
        width: 100%;
      }
    }
  `]
})
export class ConnectionAlertComponent {
  @Input() show = false;
  @Input() type: 'error' | 'warning' | 'info' | 'success' = 'info';
  @Input() title?: string;
  @Input() message = '';
  @Input() closable = true;
  @Input() showActions = false;
  @Input() showRetry = false;
  @Input() showOfflineMode = false;
  @Input() closeText?: string;

  @Output() close = new EventEmitter<void>();
  @Output() retry = new EventEmitter<void>();
  @Output() offlineMode = new EventEmitter<void>();

  onClose(): void {
    this.close.emit();
  }

  onRetry(): void {
    this.retry.emit();
  }

  onOfflineMode(): void {
    this.offlineMode.emit();
  }
}