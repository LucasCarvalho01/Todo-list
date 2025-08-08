import { AfterViewInit, Component, inject, OnInit, ViewChild } from '@angular/core';
import { Task } from '../../models';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TaskFilter } from '../task-filter/task-filter';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { TaskService } from '../../services/task.service';
import { MatIconModule } from '@angular/material/icon';
import { catchError, map, throwError } from 'rxjs';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TaskCreateDialog } from '../task-create-dialog/task-create-dialog';
import { MatDialog } from '@angular/material/dialog';
import { TaskEditDialog } from '../task-edit-dialog/task-edit-dialog';

@Component({
  selector: 'app-task-list',
  imports: [MatTableModule, TaskFilter, MatPaginatorModule, MatIconModule, MatSnackBarModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css'
})
export class TaskList implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'title', 'user', 'actions'];
  // tasks: Task[] = [
  //   {
  //     id: 1,
  //     title: "Task 1",
  //     description: "Task asdfas asdf",
  //     priority: 'HIGH',
  //     status: 'IN_PROGRESS',
  //     deadline: new Date(),
  //     user: {
  //       name: 'John Doe',
  //       email: 'john.doe@example.com'
  //     }
  //   },
  //   {
  //     id: 2,
  //     title: "Task 2",
  //     priority: 'LOW',
  //     status: 'DONE',
  //     deadline: new Date(),
  //     user: {
  //       name: 'Jane Doe',
  //       email: 'jane.doe@example.com'
  //     }
  //   },
  //   {
  //     id: 3,
  //     title: "Configurar banco de dados Configurar banco de dados Configurar banco de dados Configurar banco de dados",
  //     description: "Setup inicial do banco",
  //     priority: 'HIGH',
  //     status: 'IN_PROGRESS',
  //     deadline: new Date(),
  //     user: {
  //       name: 'John Doe',
  //       email: 'john.doe@example.com'
  //     }
  //   },
  //   {
  //     id: 4,
  //     title: "Testes unitários",
  //     description: "Implementar testes automatizados",
  //     priority: 'MEDIUM',
  //     status: 'DONE',
  //     deadline: new Date(),
  //     user: {
  //       name: 'Jane Doe Silva ribeiro nome grande muito grande',
  //       email: 'jane.doe@example.com'
  //     }
  //   },
  //   {
  //     id: 5,
  //     title: "Documentação da API",
  //     description: "Criar documentação completa",
  //     priority: 'LOW',
  //     status: 'IN_PROGRESS',
  //     deadline: new Date(),
  //     user: {
  //       name: 'Carlos Silva',
  //       email: 'carlos.silva@example.com'
  //     }
  //   },
  //   {
  //     id: 8,
  //     title: "Task asd ssss",
  //     priority: 'LOW',
  //     status: 'DONE',
  //     deadline: new Date(),
  //     user: {
  //       name: 'Jane Doe',
  //       email: 'jane.doe@example.com'
  //     }
  //   }
  // ]
  tasks: Task[] = [];
  dataSource = new MatTableDataSource<Task>(this.tasks);
  private snackBar = inject(MatSnackBar);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
    private editDialog: MatDialog
  ) {}
  
  ngOnInit() {
    this.loadTasks();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openCreateTaskDialog() {
    const availableUsers = this.getUniqueUsers();
    
    const dialogRef = this.dialog.open(TaskCreateDialog, {
      width: '500px',
      maxWidth: '90vw',
      data: { users: availableUsers }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('Nova task:', result);
        this.snackBar.open('Tarefa criada com sucesso!', 'Ok', {
          duration: 3000,
          panelClass: 'snackbar-success'
        });
        
        this.loadTasks();
      }
    });
  }

  openEditTaskDialog(id: number) {  
    const dialogRef = this.dialog.open(TaskEditDialog, {
      width: '500px',
      maxWidth: '90vw',
      data: { taskId: id }
    });

    // dialogRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     console.log('task:', result);
    //     this.snackBar.open('Tarefa atualizada com sucesso!', 'Ok', {
    //       duration: 3000,
    //       panelClass: 'snackbar-success'
    //     });
        
    //     this.loadTasks();
    //   }
    // });
  }


  private getUniqueUsers() {
    const users = this.tasks.map(task => task.user);
    const uniqueUsers = users.filter((user, index, self) => 
      index === self.findIndex(u => u.email === user.email)
    );
    return uniqueUsers;
  }

  private loadTasks() {
    this.taskService.getTasks().subscribe((tasks) => {
      this.tasks = tasks;
      this.dataSource.data = tasks;
    });
  }

  onFilterChange(filters: any) {
    let filteredData = this.tasks;

    if (filters.id) {
      filteredData = filteredData.filter(task => 
        task.id.toString().includes(filters.id.toString())
      );
    }

    if (filters.title) {
      filteredData = filteredData.filter(task => 
        task.title.toLowerCase().includes(filters.title.toLowerCase()) ||
        (task.description && task.description.toLowerCase().includes(filters.title.toLowerCase()))
      );
    }

    if (filters.status) {
      filteredData = filteredData.filter(task => 
        task.status === filters.status
      );
    }

    if (filters.user) {
      filteredData = filteredData.filter(task => 
        task.user.name === filters.user
      );
    }

    this.dataSource.data = filteredData;
  }

  finishTask(task: Task) {
    this.taskService.finishTask(task.id, 'DONE')
      .pipe(
        catchError(error => {
          console.error('Error finishing task:', error);
          return throwError(() => error);
        })
      )
      .subscribe({
        next: (updatedTask) => {
          const index = this.tasks.findIndex(t => t.id === updatedTask.id);
          if (index !== -1) {
            this.tasks[index] = updatedTask;
            this.dataSource.data = [...this.tasks];
          }
        },
        error: (error) => {
          this.snackBar.open('Erro ao finalizar tarefa', 'Ok', {
            duration: 3000,
            panelClass: 'snackbar-error'
          });
        }
      });
  }

  deleteTask(task: Task) {
    this.taskService.deleteTask(task.id)
    .pipe(
      map(() => task.id),
      catchError(error => {
        console.error('Error deleteing task:', error);
        return throwError(() => error);
      })
    )
    .subscribe({
      next: (deletedTaskId) => {
        const index = this.tasks.findIndex(t => t.id === deletedTaskId);
        if (index !== -1) {
          this.tasks.splice(index, 1);
          this.dataSource.data = [...this.tasks];
        }

        this.snackBar.open('Tarefa excluída com sucesso', 'Ok', {
          duration: 3000,
          panelClass: 'snackbar-success'
        });
      },
      error: (error) => {
        this.snackBar.open('Erro ao excluir tarefa', 'Ok', {
          duration: 3000,
          panelClass: 'snackbar-error'
        });
      }
    });
  }
}
