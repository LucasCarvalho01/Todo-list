import { Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, retry, throwError } from 'rxjs';
import { CreateTask, Task, TaskStatus } from '../models';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/tasks`)
      .pipe(
        retry(1),
        catchError(this.handleError)
      );
  }

  finishTask(id: number, status: TaskStatus): Observable<Task> {
    return this.http.put<Task>(
      `${this.apiUrl}/tasks/${id}/status`,
      { status }
    ).pipe(
      catchError(this.handleError)
    );
  }

  createTask(task: CreateTask): Observable<Task> {
    console.log(task);
    return this.http.post<Task>(`${this.apiUrl}/tasks`, task)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/tasks/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      errorMessage = `Código: ${error.status}, mensagem: ${error.message}`;
    }
    return throwError(() => errorMessage);
  }
}
