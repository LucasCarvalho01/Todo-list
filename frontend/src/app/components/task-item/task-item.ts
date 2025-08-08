import { Component, Input } from '@angular/core';
import { Task } from '../../models';

@Component({
  selector: 'app-task-item',
  imports: [],
  templateUrl: './task-item.html',
  styleUrl: './task-item.css'
})
export class TaskItem {
  @Input() task!: Task;
}
