import { inject } from '@angular/core';
import {
  HttpEvent,
  HttpHandlerFn,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { finalize, Observable } from 'rxjs';
import { SpinnerService } from '../services/spinner.service';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
): Observable<HttpEvent<unknown>> => {
  const authService = inject(AuthService);
  const spinnerService = inject(SpinnerService);
  const token = authService.getToken();

  //spinnerService.show();

  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(clonedRequest).pipe(finalize(() => spinnerService.hide()));
  } else {
    return next(req).pipe(finalize(() => spinnerService.hide()));
  }
};
