import { Component } from '@angular/core';
import { TaskItem } from '../task-item/task-item';
import { Task } from '../../models';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-task-list',
  imports: [MatTableModule],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css'
})
export class TaskList {
  displayedColumns = ['id', 'title', 'user', 'actions'];

  tasks: Task[] = [
    {
      id: 1,
      title: "Task 1",
      description: "Task asdfas asdf",
      priority: 'HIGH',
      status: 'IN_PROGRESS',
      deadline: new Date(),
      user: {
        name: 'John Doe',
        email: 'john.doe@example.com'
      }
    },
    {
      id: 2,
      title: "Task 2",
      priority: 'LOW',
      status: 'IN_PROGRESS',
      deadline: new Date(),
      user: {
        name: 'Jane Doe',
        email: 'jane.doe@example.com'
      }
    }
  ]
}
