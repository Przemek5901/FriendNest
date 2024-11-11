import { MessageService } from 'primeng/api';
import { FormGroup } from '@angular/forms';

export abstract class FormErrorBase {
  private fieldNames: { [key: string]: string } = {
    userName: 'Nazwa użytkownika',
    login: 'Login',
    password: 'Hasło',
    gender: 'Płeć',
  };

  protected constructor(private messageService: MessageService) {}

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
}
