import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { Task } from '../../models';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TaskFilter } from '../task-filter/task-filter';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { TaskService } from '../../services/task.service';

@Component({
  selector: 'app-task-list',
  imports: [MatTableModule, TaskFilter, MatPaginatorModule],
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

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private taskService: TaskService) {}
  
  ngOnInit() {
    // this.dataSource.data = this.tasks;
    this.taskService.getTasks().subscribe((tasks) => {
      this.tasks = tasks;
      this.dataSource.data = tasks;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
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
}
