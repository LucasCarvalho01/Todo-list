import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LoginRequest } from '../../models';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSnackBarModule],
  standalone: true,
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });
  private router = inject(Router);
  isLoading = false;

  constructor(private authService: AuthService, private snackBar: MatSnackBar) {}

  login() {
    this.isLoading = true;
    this.authService.login(this.loginForm.value as LoginRequest)
    .pipe(finalize(() => this.isLoading = false))
    .subscribe({
      next: (response) => {
        this.loginForm.reset();
        this.router.navigate(['/tasks']);
      },
      error: (error) => {
        console.error(error);
        this.snackBar.open('Login inválido', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }
}
