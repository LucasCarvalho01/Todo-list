import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { Task } from '../../models';

@Component({
  selector: 'app-task-filter',
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, ReactiveFormsModule, MatButtonModule],
  templateUrl: './task-filter.html',
  styleUrl: './task-filter.css'
})
export class TaskFilter implements OnInit, OnChanges {
  @Input() tasks: Task[] = [];
  @Output() filterChange = new EventEmitter<any>();

  filterForm: FormGroup;
  availableUsers: string[] = [];

  constructor() {
    this.filterForm = new FormGroup({
      id: new FormControl(''),
      user: new FormControl(''),
      title: new FormControl(''),
      status: new FormControl('')
    });
  }

  ngOnInit() {
    this.extractAvailableUsers();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tasks'] && changes['tasks'].currentValue) {
      this.extractAvailableUsers();
    }
  }

  extractAvailableUsers() {
    const userNames = this.tasks.map(task => task.user.name);
    this.availableUsers = [...new Set(userNames)].sort();
  }

  onSubmit() {
    this.applyFilters();
  }

  applyFilters() {
    const filters = this.filterForm.value;
    this.filterChange.emit(filters);
  }

  clearFilters() {
    this.filterForm.reset();
    this.filterChange.emit({});
  }
}
