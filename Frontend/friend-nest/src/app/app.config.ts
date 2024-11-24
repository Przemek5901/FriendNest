import {
  ApplicationConfig,
  LOCALE_ID,
  provideZoneChangeDetection,
} from '@angular/core';
import { MessageService } from 'primeng/api';
import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './utils/auth-interceptor';
import { provideAnimations } from '@angular/platform-browser/animations';

registerLocaleData(localePl);

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimations(),
    MessageService,
    { provide: LOCALE_ID, useValue: 'pl-PL' },
  ],
};
