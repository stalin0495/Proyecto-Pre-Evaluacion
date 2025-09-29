import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { PaginationRequest, PaginatedResponse } from '../models/bank.models';

@Injectable({
  providedIn: 'root'
})
export class BaseHttpService {
  protected readonly defaultHeaders: HttpHeaders;
  protected readonly baseUrl: string;

  constructor(protected http: HttpClient) {
    this.baseUrl = environment.api.baseUrl;
    this.defaultHeaders = new HttpHeaders(environment.headers);
  }

  /**
   * Construye HttpOptions con headers personalizados
   */
  protected getHttpOptions(additionalHeaders?: { [key: string]: string }) {
    let headers = this.defaultHeaders;
    
    if (additionalHeaders) {
      Object.keys(additionalHeaders).forEach(key => {
        headers = headers.set(key, additionalHeaders[key]);
      });
    }

    return { headers };
  }

  /**
   * Construye parámetros de paginación para las requests
   */
  protected buildPaginationParams(pagination?: PaginationRequest): HttpParams {
    let params = new HttpParams();

    if (pagination) {
      params = params.set('page', pagination.page.toString());
      params = params.set('size', pagination.size.toString());
      
      if (pagination.sort) {
        const sortParam = pagination.direction 
          ? `${pagination.sort},${pagination.direction}`
          : pagination.sort;
        params = params.set('sort', sortParam);
      }
    } else {
      // Usar valores por defecto del environment
      params = params.set('page', environment.pagination.defaultPage.toString());
      params = params.set('size', environment.pagination.defaultPageSize.toString());
    }

    return params;
  }

  /**
   * Construye parámetros de fecha en formato ISO
   */
  protected buildDateParams(startDate?: string, endDate?: string): HttpParams {
    let params = new HttpParams();

    if (startDate) {
      params = params.set('startDate', this.formatDateToISO(startDate));
    }
    
    if (endDate) {
      params = params.set('endDate', this.formatDateToISO(endDate));
    }

    return params;
  }

  /**
   * Convierte fecha a formato ISO requerido por el backend
   */
  protected formatDateToISO(date: string | Date): string {
    const dateObj = typeof date === 'string' ? new Date(date) : date;
    return dateObj.toISOString().slice(0, 19); // YYYY-MM-DDTHH:mm:ss
  }

  /**
   * GET request con soporte para paginación
   */
  protected getPaginated<T>(
    endpoint: string, 
    pagination?: PaginationRequest,
    additionalParams?: { [key: string]: string }
  ): Observable<PaginatedResponse<T>> {
    let params = this.buildPaginationParams(pagination);

    // Agregar parámetros adicionales si existen
    if (additionalParams) {
      Object.keys(additionalParams).forEach(key => {
        params = params.set(key, additionalParams[key]);
      });
    }

    return this.http.get<PaginatedResponse<T>>(endpoint, {
      ...this.getHttpOptions(),
      params
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * GET request simple
   */
  protected get<T>(endpoint: string, params?: { [key: string]: string }): Observable<T> {
    let httpParams = new HttpParams();

    if (params) {
      Object.keys(params).forEach(key => {
        httpParams = httpParams.set(key, params[key]);
      });
    }

    return this.http.get<T>(endpoint, {
      ...this.getHttpOptions(),
      params: httpParams
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * POST request
   */
  protected post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(endpoint, data, this.getHttpOptions()).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * PUT request
   */
  protected put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(endpoint, data, this.getHttpOptions()).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * DELETE request
   */
  protected delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(endpoint, this.getHttpOptions()).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Manejo centralizado de errores HTTP
   */
  protected handleError = (error: HttpErrorResponse): Observable<never> => {
    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error del cliente: ${error.error.message}`;
    } else {
      // Error del lado del servidor
      switch (error.status) {
        case 400:
          errorMessage = `Solicitud inválida: ${error.error?.message || 'Datos incorrectos'}`;
          break;
        case 401:
          errorMessage = 'No autorizado. Por favor, inicie sesión nuevamente.';
          break;
        case 403:
          errorMessage = 'Acceso denegado. No tiene permisos para esta operación.';
          break;
        case 404:
          errorMessage = 'Recurso no encontrado.';
          break;
        case 500:
          errorMessage = 'Error interno del servidor. Intente más tarde.';
          break;
        case 503:
          errorMessage = 'Servicio no disponible temporalmente.';
          break;
        default:
          errorMessage = `Error del servidor (${error.status}): ${error.error?.message || error.message}`;
      }
    }

    if (environment.features.enableLogging) {
      console.error('HTTP Error:', {
        status: error.status,
        message: errorMessage,
        error: error.error,
        url: error.url
      });
    }

    return throwError(() => new Error(errorMessage));
  };

  /**
   * Construye URL completa para un endpoint
   */
  protected buildUrl(service: 'customerService' | 'accountService' | 'reportService', path: string): string {
    const serviceUrl = environment.api[service];
    return `${serviceUrl}${path.startsWith('/') ? path : '/' + path}`;
  }

  /**
   * Valida si el servicio está disponible antes de hacer la request
   */
  protected validateServiceAvailability(serviceName: string): void {
    // Esta función puede expandirse para incluir verificaciones de conectividad
    if (!navigator.onLine) {
      throw new Error('Sin conexión a internet. Verifique su conexión de red.');
    }
  }

  /**
   * Utilidad para logging condicional
   */
  protected log(message: string, data?: any): void {
    if (environment.features.enableLogging) {
      console.log(`[${new Date().toISOString()}] ${message}`, data);
    }
  }
}