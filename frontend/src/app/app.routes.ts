import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { TaskList } from './components/task-list/task-list';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
    {
        path: '',
        component: Home
    },
    {
        path: 'tasks',
        component: TaskList,
        canActivate: [authGuard]
    }
];
