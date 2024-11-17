import {inject} from '@angular/core';
import {HttpInterceptorFn,} from '@angular/common/http';
import {AuthService} from '../services/auth.service';
import {finalize} from 'rxjs';
import {SpinnerService} from '../services/spinner.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const spinnerService = inject(SpinnerService);
  const token = authService.getToken();

  spinnerService.show();

  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(clonedRequest).pipe(
      finalize(() => spinnerService.hide())
    );
  } else {
    return next(req).pipe(
      finalize(() => spinnerService.hide())
    );
  }
};
