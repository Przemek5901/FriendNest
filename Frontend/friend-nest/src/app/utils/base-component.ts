import { Directive, inject, OnDestroy } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../models/User';

@Directive()
export abstract class BaseComponent implements OnDestroy {
  private destroy$ = new Subject<void>();
  private messageService = inject(MessageService);

  private fieldNames: { [key: string]: string } = {
    userName: 'Nazwa użytkownika',
    login: 'Login',
    password: 'Hasło',
    gender: 'Płeć',
    profileName: 'Nazwa profilu',
  };

  protected autoUnsubscribe<T>() {
    return takeUntil<T>(this.destroy$);
  }

  protected showFieldErrors(form: FormGroup): void {
    Object.keys(form.controls).forEach((field) => {
      const control = form.get(field);
      const fieldName = this.fieldNames[field] || field;

      if (control && control.errors) {
        if (control.errors['required']) {
          this.messageService.add({
            severity: 'error',
            summary: 'Wymagane pole',
            detail: `Pole "${fieldName}" jest wymagane.`,
          });
        }
        if (control.errors['pattern'] && field === 'password') {
          const actualValue = control.value || '';
          const regex =
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
          if (!regex.test(actualValue)) {
            this.messageService.add({
              severity: 'error',
              summary: 'Nieprawidłowe hasło',
              detail: `${fieldName} musi zawierać przynajmniej jedną wielką literę, cyfrę oraz znak specjalny.`,
            });
          }
        }
      }
    });
  }

  hadleHttpError(error: HttpErrorResponse): void {
    if (error.error.message) {
      this.messageService.add({
        severity: 'error',
        summary: 'Błąd',
        detail: error.error.message,
      });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Błąd',
        detail: 'Nieznany błąd, skontaktuj się z administratorem',
      });
    }
  }

  getUser(): User {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      return JSON.parse(storedUser);
    }
    return {};
  }

  ngOnDestroy(): void {
    this.destroy$.next(undefined);
    this.destroy$.complete();
  }
}
