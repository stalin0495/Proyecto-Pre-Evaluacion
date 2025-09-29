import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink } from '@angular/router';
import { ConnectivityStatusComponent } from './components/connectivity-status/connectivity-status.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink
  ],
  template: `
    <div class="app-container">
      <!-- Sidebar -->
      <nav class="sidebar">
        <div class="sidebar-header">
          <h2>Bank Manager</h2>
        </div>
        <ul class="nav-menu">
          <li>
            <a routerLink="/dashboard" class="nav-link">
              <span class="nav-icon">📊</span>
              <span>Dashboard</span>
            </a>
          </li>
          <li>
            <a routerLink="/accounts" class="nav-link">
              <span class="nav-icon">🏦</span>
              <span>Cuentas</span>
            </a>
          </li>
          <li>
            <a routerLink="/transactions" class="nav-link">
              <span class="nav-icon">📋</span>
              <span>Transacciones</span>
            </a>
          </li>
          <li>
            <a routerLink="/customers" class="nav-link">
              <span class="nav-icon">👥</span>
              <span>Clientes</span>
            </a>
          </li>
          <li>
            <a routerLink="/reports" class="nav-link">
              <span class="nav-icon">📈</span>
              <span>Reportes</span>
            </a>
          </li>
        </ul>
      </nav>

      <!-- Main Content -->
      <div class="main-content">
        <!-- Header -->
        <header class="header">
          <h1>{{title}}</h1>
          <div class="header-actions">
            <button class="btn btn-outline">
              <span class="nav-icon">👤</span>
              <span>Perfil</span>
            </button>
          </div>
        </header>

        <!-- Content Area -->
        <main class="content">
          <router-outlet></router-outlet>
        </main>
      </div>
    </div>
  `,
  styles: [`
    .app-container {
      display: flex;
      height: 100vh;
      background-color: var(--background-color);
    }

    .sidebar {
      width: 250px;
      background: var(--card-background);
      border-right: 1px solid var(--border-color);
      box-shadow: var(--shadow);
    }

    .sidebar-header {
      padding: var(--spacing-lg);
      border-bottom: 1px solid var(--border-color);
    }

    .sidebar-header h2 {
      color: var(--primary-color);
      font-size: 1.5rem;
      margin: 0;
    }

    .nav-menu {
      list-style: none;
      padding: var(--spacing-md) 0;
      margin: 0;
    }

    .nav-menu li {
      margin: 0;
    }

    .nav-link {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
      padding: var(--spacing-md) var(--spacing-lg);
      text-decoration: none;
      color: var(--text-secondary);
      transition: all 0.2s ease;
    }

    .nav-link:hover {
      background-color: #f8fafc;
      color: var(--primary-color);
    }

    .nav-icon {
      font-size: 1.25rem;
      width: 24px;
      text-align: center;
    }

    .main-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }

    .header {
      background: var(--card-background);
      border-bottom: 1px solid var(--border-color);
      padding: var(--spacing-md) var(--spacing-lg);
      display: flex;
      align-items: center;
      justify-content: space-between;
      box-shadow: var(--shadow);
    }

    .header h1 {
      margin: 0;
      font-size: 1.5rem;
      color: var(--text-primary);
    }

    .header-actions {
      display: flex;
      gap: var(--spacing-md);
    }

    .content {
      flex: 1;
      padding: var(--spacing-lg);
      overflow-y: auto;
      background-color: var(--background-color);
    }

    @media (max-width: 768px) {
      .app-container {
        flex-direction: column;
      }

      .sidebar {
        width: 100%;
        height: auto;
      }

      .nav-menu {
        display: flex;
        overflow-x: auto;
        padding: var(--spacing-sm);
      }

      .nav-menu li {
        flex-shrink: 0;
      }

      .nav-link {
        padding: var(--spacing-sm) var(--spacing-md);
        white-space: nowrap;
      }
    }
  `]
})
export class AppComponent {
  title = 'Bank Manager View';
}