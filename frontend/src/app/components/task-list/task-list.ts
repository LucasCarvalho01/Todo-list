import { Component } from '@angular/core';
import { TaskItem } from '../task-item/task-item';
import { Task } from '../../models';

@Component({
  selector: 'app-task-list',
  imports: [TaskItem],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css'
})
export class TaskList {
  tasks: Task[] = [
    {
      id: 1,
      title: "Task 1",
      description: "Task asdfas asdf",
      priority: 'HIGH',
      status: 'IN_PROGRESS',
      deadline: new Date()
    },
    {
      id: 2,
      title: "Task 2",
      priority: 'LOW',
      status: 'IN_PROGRESS',
      deadline: new Date()
    }
  ]
}
