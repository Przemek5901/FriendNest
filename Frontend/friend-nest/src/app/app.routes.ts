import { Routes } from '@angular/router';
import { MainPageComponent } from './main-page/main-page.component';
import { LoginPageComponent } from './login-page/login-page.component';
import {AuthGuard} from './utils/auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'main-page',
    loadComponent: () => import('./main-page/main-page.component').then((m) => m.MainPageComponent),
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    component: LoginPageComponent,

  },
];
