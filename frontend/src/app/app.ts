import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TaskList } from './components/task-list/task-list';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { BrazilianDateAdapter } from './shared/br-date-adapter';

export const BR_DATE_FORMATS = {
  parse: {
    dateInput: 'input'
  },
  display: {
    dateInput: 'input',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  }
};  

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TaskList],
  templateUrl: './app.html',
  styleUrl: './app.css',
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },
    { provide: DateAdapter, useClass: BrazilianDateAdapter, deps: [MAT_DATE_LOCALE] },
    { provide: MAT_DATE_FORMATS, useValue: BR_DATE_FORMATS }
  ]
})
export class App {
  protected readonly title = signal('todo-app');
}
