import { Routes } from '@angular/router';

import { AuthGuard } from './utils/auth-guard';
import { EmptyLayoutComponent } from './components/empty-layout/empty-layout.component';
import { LoginPageComponent } from './components/empty-layout/login-page/login-page.component';
import { MainLayoutComponent } from './components/main-layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: '',
    component: EmptyLayoutComponent,
    children: [{ path: 'login', component: LoginPageComponent }],
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'main-page',
        loadComponent: () =>
          import('./components/main-layout/main-page/main-page.component').then(
            (m) => m.MainPageComponent,
          ),
      },
      {
        path: 'profile/:login',
        loadComponent: () =>
          import('./components/main-layout/profile/profile.component').then(
            (m) => m.ProfileComponent,
          ),
      },
      {
        path: 'post/:postId/repost/:reposterId',
        loadComponent: () =>
          import(
            './components/main-layout/post-details/post-details.component'
          ).then((m) => m.PostDetailsComponent),
      },
      {
        path: 'post/:postId/quote/:quotedPostId',
        loadComponent: () =>
          import(
            './components/main-layout/post-details/post-details.component'
          ).then((m) => m.PostDetailsComponent),
      },
      {
        path: 'post/:postId',
        loadComponent: () =>
          import(
            './components/main-layout/post-details/post-details.component'
          ).then((m) => m.PostDetailsComponent),
      },

      {
        path: 'search',
        loadComponent: () =>
          import('./components/main-layout/search/search.component').then(
            (m) => m.SearchComponent,
          ),
      },
      {
        path: 'messages',
        loadComponent: () =>
          import('./components/main-layout/messages/messages.component').then(
            (m) => m.MessagesComponent,
          ),
      },
      {
        path: 'account-settings',
        loadComponent: () =>
          import(
            './components/main-layout/account-settings/account-settings.component'
          ).then((m) => m.AccountSettingsComponent),
      },
    ],
  },
];
