import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="reports-container">
      <div class="page-header">
        <h1>Reportes y Análisis</h1>
        <div class="header-actions">
          <select class="input">
            <option value="">Período: Último mes</option>
            <option value="week">Última semana</option>
            <option value="quarter">Último trimestre</option>
            <option value="year">Último año</option>
          </select>
          <button class="btn btn-primary">
            <span>📊</span>
            Generar Reporte
          </button>
        </div>
      </div>
      
      <div class="reports-grid">
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">📈</div>
            <div class="report-info">
              <h3>Reporte de Transacciones</h3>
              <p>Análisis detallado del flujo de transacciones por período</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">1,234</span>
              <span class="stat-label">Transacciones</span>
            </div>
            <div class="stat">
              <span class="stat-value">$567,890</span>
              <span class="stat-label">Volumen total</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-outline btn-sm">
              👁️ Ver detalles
            </button>
            <button class="btn btn-primary btn-sm">
              📄 Descargar PDF
            </button>
            <button class="btn btn-secondary btn-sm">
              📊 Excel
            </button>
          </div>
        </div>
        
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">🏦</div>
            <div class="report-info">
              <h3>Reporte de Cuentas</h3>
              <p>Estado y distribución de cuentas bancarias</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">456</span>
              <span class="stat-label">Cuentas activas</span>
            </div>
            <div class="stat">
              <span class="stat-value">23</span>
              <span class="stat-label">Nuevas cuentas</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-outline btn-sm">
              👁️ Ver detalles
            </button>
            <button class="btn btn-primary btn-sm">
              📄 Descargar PDF
            </button>
            <button class="btn btn-secondary btn-sm">
              📊 Excel
            </button>
          </div>
        </div>
        
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">👥</div>
            <div class="report-info">
              <h3>Reporte de Clientes</h3>
              <p>Análisis demográfico y comportamental de clientes</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">789</span>
              <span class="stat-label">Clientes totales</span>
            </div>
            <div class="stat">
              <span class="stat-value">45</span>
              <span class="stat-label">Nuevos clientes</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-outline btn-sm">
              👁️ Ver detalles
            </button>
            <button class="btn btn-primary btn-sm">
              📄 Descargar PDF
            </button>
            <button class="btn btn-secondary btn-sm">
              📊 Excel
            </button>
          </div>
        </div>
        
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">💰</div>
            <div class="report-info">
              <h3>Reporte Financiero</h3>
              <p>Resumen financiero y métricas de rentabilidad</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">$2.5M</span>
              <span class="stat-label">Balance total</span>
            </div>
            <div class="stat">
              <span class="stat-value">+15%</span>
              <span class="stat-label">Crecimiento</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-outline btn-sm">
              👁️ Ver detalles
            </button>
            <button class="btn btn-primary btn-sm">
              📄 Descargar PDF
            </button>
            <button class="btn btn-secondary btn-sm">
              📊 Excel
            </button>
          </div>
        </div>
        
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">⚠️</div>
            <div class="report-info">
              <h3>Reporte de Riesgos</h3>
              <p>Análisis de riesgos y transacciones sospechosas</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">12</span>
              <span class="stat-label">Alertas activas</span>
            </div>
            <div class="stat">
              <span class="stat-value">3</span>
              <span class="stat-label">Casos críticos</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-outline btn-sm">
              👁️ Ver detalles
            </button>
            <button class="btn btn-primary btn-sm">
              📄 Descargar PDF
            </button>
            <button class="btn btn-secondary btn-sm">
              📊 Excel
            </button>
          </div>
        </div>
        
        <div class="report-card">
          <div class="report-header">
            <div class="report-icon">📅</div>
            <div class="report-info">
              <h3>Reporte Personalizado</h3>
              <p>Crear reportes personalizados con filtros específicos</p>
            </div>
          </div>
          <div class="report-stats">
            <div class="stat">
              <span class="stat-value">∞</span>
              <span class="stat-label">Posibilidades</span>
            </div>
            <div class="stat">
              <span class="stat-value">5</span>
              <span class="stat-label">Plantillas</span>
            </div>
          </div>
          <div class="report-actions">
            <button class="btn btn-primary">
              ⚡ Crear Reporte
            </button>
          </div>
        </div>
      </div>

      <!-- Quick Stats -->
      <div class="quick-stats">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">Estadísticas Rápidas</h3>
          </div>
          <div class="card-content">
            <div class="stats-row">
              <div class="quick-stat">
                <span class="stat-label">Reportes generados hoy</span>
                <span class="stat-value">23</span>
              </div>
              <div class="quick-stat">
                <span class="stat-label">Reportes programados</span>
                <span class="stat-value">8</span>
              </div>
              <div class="quick-stat">
                <span class="stat-label">Usuarios con acceso</span>
                <span class="stat-value">15</span>
              </div>
              <div class="quick-stat">
                <span class="stat-label">Almacenamiento usado</span>
                <span class="stat-value">2.3 GB</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .reports-container {
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

    .reports-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
      gap: var(--spacing-lg);
      margin-bottom: var(--spacing-xl);
    }

    .report-card {
      background: var(--card-background);
      border-radius: var(--radius);
      padding: var(--spacing-lg);
      box-shadow: var(--shadow);
      border: 1px solid var(--border-color);
      display: flex;
      flex-direction: column;
      height: 100%;
    }

    .report-header {
      display: flex;
      gap: var(--spacing-md);
      margin-bottom: var(--spacing-lg);
    }

    .report-icon {
      font-size: 2.5rem;
      width: 60px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border-radius: var(--radius);
      flex-shrink: 0;
    }

    .report-info h3 {
      margin: 0 0 var(--spacing-sm) 0;
      color: var(--text-primary);
      font-size: 1.25rem;
    }

    .report-info p {
      margin: 0;
      color: var(--text-secondary);
      font-size: 0.875rem;
    }

    .report-stats {
      display: flex;
      justify-content: space-around;
      margin-bottom: var(--spacing-lg);
      padding: var(--spacing-md) 0;
      border-top: 1px solid var(--border-color);
      border-bottom: 1px solid var(--border-color);
    }

    .stat {
      text-align: center;
    }

    .stat-value {
      display: block;
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--primary-color);
    }

    .stat-label {
      display: block;
      font-size: 0.75rem;
      color: var(--text-secondary);
      margin-top: var(--spacing-xs);
    }

    .report-actions {
      display: flex;
      gap: var(--spacing-sm);
      flex-wrap: wrap;
      margin-top: auto;
    }

    .quick-stats {
      margin-top: var(--spacing-xl);
    }

    .stats-row {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: var(--spacing-lg);
    }

    .quick-stat {
      display: flex;
      flex-direction: column;
      text-align: center;
    }

    .quick-stat .stat-label {
      font-size: 0.875rem;
      color: var(--text-secondary);
      margin-bottom: var(--spacing-sm);
    }

    .quick-stat .stat-value {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--primary-color);
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

      .reports-grid {
        grid-template-columns: 1fr;
      }

      .report-actions {
        justify-content: center;
      }
      
      .stats-row {
        grid-template-columns: repeat(2, 1fr);
      }
    }

    @media (max-width: 480px) {
      .stats-row {
        grid-template-columns: 1fr;
      }

      .report-actions {
        flex-direction: column;
      }
    }
  `]
})
export class ReportsComponent {}