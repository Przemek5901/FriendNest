import { Routes } from '@angular/router';

import { AuthGuard } from './utils/auth-guard';
import { LoginPageComponent } from './login-page/login-page.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'main-page',
    loadComponent: () =>
      import('./main-page/main-page.component').then(
        (m) => m.MainPageComponent,
      ),
    canActivate: [AuthGuard],
  },
  {
    path: 'profile',
    loadComponent: () =>
      import('./profile/profile.component').then((m) => m.ProfileComponent),
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    component: LoginPageComponent,
  },
];
