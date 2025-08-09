import { Component } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AuthService } from '../../services/auth';
import { SignupRequest } from '../../models';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSnackBarModule],
  standalone: true,
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  registerForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });
  isLoading = false;

  constructor(private authService: AuthService, private snackBar: MatSnackBar) {}

  register() {
    this.isLoading = true;
    this.authService.signup(this.registerForm.value as SignupRequest)
    .pipe(finalize(() => this.isLoading = false))
    .subscribe({
      next: (response) => {
        console.log(response);
        this.registerForm.reset();
        this.snackBar.open('Registrado com sucesso', 'Fechar', {
          duration: 3000,
        });
      },
      error: (error) => {
        console.error(error);
        this.snackBar.open('Erro ao registrar', 'Fechar', {
          duration: 3000,
        });
      }
    });
  }
}
