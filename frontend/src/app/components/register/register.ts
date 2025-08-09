import { Component, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormGroup, Validators, FormBuilder, FormGroupDirective } from '@angular/forms';
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
  registerForm!: FormGroup;
  isLoading = false;
  @ViewChild(FormGroupDirective) formGroupDirective!: FormGroupDirective;

  constructor(
    private authService: AuthService, 
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) {
    this.createForm();
  }

  private createForm() {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  register() {
    this.isLoading = true;
    this.authService.signup(this.registerForm.value as SignupRequest)
    .pipe(finalize(() => this.isLoading = false))
    .subscribe({
      next: (response) => {
        console.log(response);
        
        this.formGroupDirective.resetForm();
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
