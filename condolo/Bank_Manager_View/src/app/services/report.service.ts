import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { CustomerService } from './customer.service';
import { AccountService } from './account.service';
import { environment } from '../../environments/environment';

export interface DashboardStats {
  totalCustomers: number;
  activeCustomers: number;
  totalAccounts: number;
  activeAccounts: number;
  totalTransactions: number;
  todayTransactions: number;
  totalBalance: number;
  monthlyGrowth: number;
}

export interface ReportData {
  customers: any[];
  accounts: any[];
  transactions: any[];
  summary: any;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private baseUrl = environment.api.reportService; // Puerto para servicio de reportes si existe

  constructor(
    private http: HttpClient,
    private customerService: CustomerService,
    private accountService: AccountService
  ) { }

  // Obtener estadísticas del dashboard
  getDashboardStats(): Observable<DashboardStats> {
    return combineLatest([
      this.customerService.getAllCustomersSimple(),
      this.accountService.getAllAccountsSimple(),
      this.accountService.getAllTransactionsSimple()
    ]).pipe(
      map(([customers, accounts, transactions]) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        
        const todayTransactions = transactions.filter(t => {
          const transactionDate = new Date(t.date);
          transactionDate.setHours(0, 0, 0, 0);
          return transactionDate.getTime() === today.getTime();
        }).length;

        const totalBalance = accounts.reduce((sum, account) => sum + account.initialBalance, 0);
        
        const activeCustomers = customers.filter(c => c.status === 'A').length;
        const activeAccounts = accounts.filter(a => a.status === 'A').length;

        return {
          totalCustomers: customers.length,
          activeCustomers,
          totalAccounts: accounts.length,
          activeAccounts,
          totalTransactions: transactions.length,
          todayTransactions,
          totalBalance,
          monthlyGrowth: 5.2 // Este valor podría calcularse basado en datos históricos
        };
      })
    );
  }

  // Generar reporte de clientes
  generateCustomerReport(startDate?: string, endDate?: string): Observable<any> {
    let url = `${this.baseUrl}/customers`;
    if (startDate && endDate) {
      url += `?start=${startDate}&end=${endDate}`;
    }
    
    // Fallback a datos locales si el servicio no existe
    return this.customerService.getAllCustomersSimple().pipe(
      map(customers => ({
        reportType: 'customers',
        generatedAt: new Date().toISOString(),
        period: { startDate, endDate },
        data: customers,
        summary: {
          total: customers.length,
          active: customers.filter(c => c.status === 'A').length,
          inactive: customers.filter(c => c.status === 'I').length
        }
      }))
    );
  }

  // Generar reporte de cuentas
  generateAccountReport(startDate?: string, endDate?: string): Observable<any> {
    return this.accountService.getAllAccountsSimple().pipe(
      map(accounts => ({
        reportType: 'accounts',
        generatedAt: new Date().toISOString(),
        period: { startDate, endDate },
        data: accounts,
        summary: {
          total: accounts.length,
          active: accounts.filter(a => a.status === 'A').length,
          totalBalance: accounts.reduce((sum, acc) => sum + acc.initialBalance, 0),
          byType: this.groupAccountsByType(accounts)
        }
      }))
    );
  }

  // Generar reporte de transacciones
  generateTransactionReport(startDate?: string, endDate?: string): Observable<any> {
    return this.accountService.getAllTransactionsSimple().pipe(
      map(transactions => {
        let filteredTransactions = transactions;
        
        if (startDate && endDate) {
          const start = new Date(startDate);
          const end = new Date(endDate);
          filteredTransactions = transactions.filter(t => {
            const transactionDate = new Date(t.date);
            return transactionDate >= start && transactionDate <= end;
          });
        }

        const deposits = filteredTransactions.filter(t => t.transactionType === 'DEPOSIT');
        const withdrawals = filteredTransactions.filter(t => t.transactionType === 'WITHDRAWAL');

        return {
          reportType: 'transactions',
          generatedAt: new Date().toISOString(),
          period: { startDate, endDate },
          data: filteredTransactions,
          summary: {
            total: filteredTransactions.length,
            deposits: {
              count: deposits.length,
              amount: deposits.reduce((sum, t) => sum + t.amount, 0)
            },
            withdrawals: {
              count: withdrawals.length,
              amount: withdrawals.reduce((sum, t) => sum + t.amount, 0)
            },
            netFlow: deposits.reduce((sum, t) => sum + t.amount, 0) - withdrawals.reduce((sum, t) => sum + t.amount, 0)
          }
        };
      })
    );
  }

  // Generar reporte financiero consolidado
  generateFinancialReport(startDate?: string, endDate?: string): Observable<any> {
    return combineLatest([
      this.generateCustomerReport(startDate, endDate),
      this.generateAccountReport(startDate, endDate),
      this.generateTransactionReport(startDate, endDate)
    ]).pipe(
      map(([customerReport, accountReport, transactionReport]) => ({
        reportType: 'financial',
        generatedAt: new Date().toISOString(),
        period: { startDate, endDate },
        summary: {
          customers: customerReport.summary,
          accounts: accountReport.summary,
          transactions: transactionReport.summary,
          overview: {
            totalCustomers: customerReport.summary.total,
            totalAccounts: accountReport.summary.total,
            totalBalance: accountReport.summary.totalBalance,
            transactionVolume: transactionReport.summary.netFlow
          }
        }
      }))
    );
  }

  // Exportar reporte como CSV
  exportReportAsCSV(reportData: any, filename: string): void {
    const csvContent = this.convertToCSV(reportData.data);
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    
    const link = document.createElement('a');
    link.href = url;
    link.download = `${filename}_${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
    
    window.URL.revokeObjectURL(url);
  }

  // Exportar reporte como PDF (simple implementation)
  exportReportAsPDF(reportData: any, filename: string): void {
    // Implementación básica - en producción usarías una librería como jsPDF
    const printContent = this.generatePrintableReport(reportData);
    const printWindow = window.open('', '', 'width=800,height=600');
    
    if (printWindow) {
      printWindow.document.write(`
        <html>
          <head>
            <title>${filename}</title>
            <style>
              body { font-family: Arial, sans-serif; margin: 20px; }
              table { border-collapse: collapse; width: 100%; }
              th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
              th { background-color: #f2f2f2; }
              .header { text-align: center; margin-bottom: 20px; }
            </style>
          </head>
          <body>
            ${printContent}
          </body>
        </html>
      `);
      printWindow.document.close();
      printWindow.print();
    }
  }

  private groupAccountsByType(accounts: any[]): any {
    return accounts.reduce((acc, account) => {
      acc[account.accountType] = (acc[account.accountType] || 0) + 1;
      return acc;
    }, {});
  }

  private convertToCSV(data: any[]): string {
    if (!data || data.length === 0) return '';
    
    const headers = Object.keys(data[0]);
    const csvRows = [
      headers.join(','),
      ...data.map(row => headers.map(header => {
        const value = row[header];
        return typeof value === 'string' && value.includes(',') ? `"${value}"` : value;
      }).join(','))
    ];
    
    return csvRows.join('\n');
  }

  private generatePrintableReport(reportData: any): string {
    return `
      <div class="header">
        <h1>Reporte ${reportData.reportType}</h1>
        <p>Generado: ${new Date(reportData.generatedAt).toLocaleString()}</p>
      </div>
      <div class="content">
        <h3>Resumen</h3>
        <pre>${JSON.stringify(reportData.summary, null, 2)}</pre>
      </div>
    `;
  }
}