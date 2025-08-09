import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatOptionModule } from '@angular/material/core';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { CreateTask, User } from '../../models';
import { TaskService } from '../../services/task.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-task-create-dialog',
  imports: [
    MatButtonModule, 
    MatIconModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatDialogModule, 
    ReactiveFormsModule, 
    MatOptionModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  standalone: true,
  templateUrl: './task-create-dialog.html',
  styleUrl: './task-create-dialog.css'
})
export class TaskCreateDialog implements OnInit {
  taskForm: FormGroup;
  availableUsers: User[] = [];
  priorityOptions = [
    { value: 'LOW', label: 'Baixa' },
    { value: 'MEDIUM', label: 'Média' },
    { value: 'HIGH', label: 'Alta' }
  ];

  constructor(
    private dialogRef: MatDialogRef<TaskCreateDialog>,
    private taskService: TaskService,
    private userService: UserService
  ) {
    this.taskForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.minLength(3)]),
      description: new FormControl(''),
      priority: new FormControl('MEDIUM', [Validators.required]),
      deadline: new FormControl(''),
      userId: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() {
    this.getAvailableUsers();
  }

  onSubmit() {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      const createTask: CreateTask = {
        title: formValue.title,
        description: formValue.description,
        priority: formValue.priority,
        deadline: formValue.deadline ? this.formatDateForBackend(formValue.deadline) : null,
        userId: formValue.userId
      };

      this.taskService.createTask(createTask).subscribe((task) => {
        this.dialogRef.close(task);
      });

      this.dialogRef.close(createTask);
      
    } else {
      this.taskForm.markAllAsTouched();
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  private getAvailableUsers() {
    this.userService.getUsers().subscribe((users) => {
      this.availableUsers = users;
    });
  }

  private formatDateForBackend(date: Date): string {
    return date.toISOString();
  }

  getErrorMessage(fieldName: string): string {
    const field = this.taskForm.get(fieldName);
    if (field?.hasError('required')) {
      return 'Este campo é obrigatório';
    }
    if (field?.hasError('minlength')) {
      return 'Mínimo de 3 caracteres';
    }
    return '';
  }
}
