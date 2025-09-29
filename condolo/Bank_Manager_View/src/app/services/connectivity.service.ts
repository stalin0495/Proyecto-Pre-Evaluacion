import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, BehaviorSubject, timer, of } from 'rxjs';
import { catchError, map, timeout, switchMap } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface ServiceStatus {
  name: string;
  url: string;
  status: 'connected' | 'disconnected' | 'checking';
  lastChecked: Date;
  responseTime?: number;
  error?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConnectivityService {
  private servicesStatus = new BehaviorSubject<ServiceStatus[]>([]);
  private checkInterval = 30000; // 30 segundos
  private timeoutMs = 5000; // 5 segundos timeout

  private services = [
    {
      name: 'Customer Service',
      url: environment.api.customerService,
      healthEndpoint: '/actuator/health'
    },
    {
      name: 'Account Service', 
      url: environment.api.accountService,
      healthEndpoint: '/actuator/health'
    },
    {
      name: 'Report Service',
      url: environment.api.reportService,
      healthEndpoint: '/actuator/health'
    }
  ];

  constructor(private http: HttpClient) {
    this.initializeStatusCheck();
    this.startPeriodicCheck();
  }

  private initializeStatusCheck(): void {
    const initialStatus: ServiceStatus[] = this.services.map(service => ({
      name: service.name,
      url: service.url,
      status: 'checking',
      lastChecked: new Date()
    }));
    
    this.servicesStatus.next(initialStatus);
    this.checkAllServices();
  }

  private startPeriodicCheck(): void {
    timer(0, this.checkInterval).pipe(
      switchMap(() => this.checkAllServicesObservable())
    ).subscribe();
  }

  private checkAllServicesObservable(): Observable<ServiceStatus[]> {
    const checks = this.services.map(service => this.checkSingleService(service));
    
    return new Observable<ServiceStatus[]>(observer => {
      Promise.all(checks).then(results => {
        this.servicesStatus.next(results);
        observer.next(results);
        observer.complete();
      });
    });
  }

  private async checkSingleService(service: any): Promise<ServiceStatus> {
    const startTime = Date.now();
    const status: ServiceStatus = {
      name: service.name,
      url: service.url,
      status: 'checking',
      lastChecked: new Date()
    };

    try {
      // Primero intentar con health endpoint
      await this.http.get(`${service.url}${service.healthEndpoint}`)
        .pipe(
          timeout(this.timeoutMs),
          catchError(() => {
            // Si health endpoint falla, intentar endpoint base
            return this.http.get(`${service.url}/api`, { responseType: 'text' })
              .pipe(
                timeout(this.timeoutMs),
                catchError(() => {
                  // Si ambos fallan, hacer una llamada HEAD simple
                  return this.http.request('HEAD', service.url)
                    .pipe(
                      timeout(this.timeoutMs),
                      catchError(() => of(null))
                    );
                })
              );
          })
        )
        .toPromise();

      status.status = 'connected';
      status.responseTime = Date.now() - startTime;
      
    } catch (error: any) {
      status.status = 'disconnected';
      status.error = this.getErrorMessage(error);
      status.responseTime = Date.now() - startTime;
    }

    return status;
  }

  private getErrorMessage(error: any): string {
    if (error.name === 'TimeoutError') {
      return 'Timeout - El servicio no responde';
    }
    if (error.status === 0) {
      return 'No se puede conectar - Servicio no disponible';
    }
    if (error.status >= 500) {
      return `Error del servidor (${error.status})`;
    }
    if (error.status >= 400) {
      return `Error del cliente (${error.status})`;
    }
    return error.message || 'Error desconocido';
  }

  public checkAllServices(): void {
    this.checkAllServicesObservable().subscribe();
  }

  public getServicesStatus(): Observable<ServiceStatus[]> {
    return this.servicesStatus.asObservable();
  }

  public getCurrentStatus(): ServiceStatus[] {
    return this.servicesStatus.value;
  }

  public isServiceConnected(serviceName: string): boolean {
    const status = this.servicesStatus.value.find(s => s.name === serviceName);
    return status?.status === 'connected';
  }

  public getServiceStatus(serviceName: string): ServiceStatus | undefined {
    return this.servicesStatus.value.find(s => s.name === serviceName);
  }

  public getAllConnected(): boolean {
    return this.servicesStatus.value.every(s => s.status === 'connected');
  }

  // Método para validar conexión antes de hacer una llamada API
  public validateConnection(serviceName: string): Observable<boolean> {
    return new Observable<boolean>(observer => {
      const status = this.getServiceStatus(serviceName);
      
      if (!status) {
        observer.next(false);
        observer.complete();
        return;
      }

      if (status.status === 'connected') {
        observer.next(true);
        observer.complete();
      } else {
        // Hacer una verificación inmediata
        const service = this.services.find(s => s.name === serviceName);
        if (service) {
          this.checkSingleService(service).then(newStatus => {
            const currentStatuses = this.servicesStatus.value;
            const index = currentStatuses.findIndex(s => s.name === serviceName);
            if (index !== -1) {
              currentStatuses[index] = newStatus;
              this.servicesStatus.next([...currentStatuses]);
            }
            observer.next(newStatus.status === 'connected');
            observer.complete();
          });
        } else {
          observer.next(false);
          observer.complete();
        }
      }
    });
  }

  // Método para reintentar conexión a un servicio específico
  public retryConnection(serviceName: string): Observable<ServiceStatus> {
    const service = this.services.find(s => s.name === serviceName);
    
    if (!service) {
      return of({
        name: serviceName,
        url: '',
        status: 'disconnected',
        lastChecked: new Date(),
        error: 'Servicio no encontrado'
      });
    }

    return new Observable<ServiceStatus>(observer => {
      this.checkSingleService(service).then(status => {
        const currentStatuses = this.servicesStatus.value;
        const index = currentStatuses.findIndex(s => s.name === serviceName);
        if (index !== -1) {
          currentStatuses[index] = status;
          this.servicesStatus.next([...currentStatuses]);
        }
        observer.next(status);
        observer.complete();
      });
    });
  }
}