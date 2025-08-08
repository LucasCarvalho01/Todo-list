import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TaskService } from '../../services/task.service';
import { EditTask, Task } from '../../models';
import { MatProgressSpinnerModule, MatSpinner } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-task-edit-dialog',
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
    MatNativeDateModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './task-edit-dialog.html',
  styleUrl: './task-edit-dialog.css'
})
export class TaskEditDialog implements OnInit {
  taskForm!: FormGroup;
  taskId: number;
  task: Task | null = null;
  priorityOptions = [
    { value: 'LOW', label: 'Baixa' },
    { value: 'MEDIUM', label: 'Média' },
    { value: 'HIGH', label: 'Alta' }
  ];

  constructor(
    private dialogRef: MatDialogRef<TaskEditDialog>,
    private taskService: TaskService,
    @Inject(MAT_DIALOG_DATA) public data: { taskId: number }
  ) {
    this.taskId = data.taskId;
  }

  ngOnInit(): void {
    this.taskService.getTaskById(this.taskId)
    .subscribe((task) => {
      this.task = task;
      this.initForm();
    });
  }

  private initForm() {
    if (!this.task) return;

    this.taskForm = new FormGroup({
      title: new FormControl(this.task.title, [Validators.required, Validators.minLength(3)]),
      description: new FormControl(this.task.description || ''),
      priority: new FormControl(this.task.priority, [Validators.required]),
      deadline: new FormControl(this.task.deadline ? new Date(this.task.deadline) : ''),
    });
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    if (this.taskForm.valid) {
      const formValue = this.taskForm.value;
      const editTask: EditTask = {
        title: formValue.title,
        description: formValue.description,
        priority: formValue.priority,
        deadline: formValue.deadline ? this.formatDateForBackend(formValue.deadline) : null
      };

      console.log(editTask);
      this.taskService.updateTask(this.task!.id, editTask).subscribe((task) => {
        this.dialogRef.close(task);
      });

      this.dialogRef.close(editTask);
    } else {
      this.taskForm.markAllAsTouched();
    }
  }

  private formatDateForBackend(date: Date): string {
    return date.toISOString();
  }
}
