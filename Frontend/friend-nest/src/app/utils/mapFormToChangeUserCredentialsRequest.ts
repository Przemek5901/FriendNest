import { FormGroup } from '@angular/forms';
import { ChangeUserCredentialsRequest } from '../models/request/ChangeUserCredentialsRequest';

export function mapFormToChangeUserCredentialsRequest(
  form: FormGroup,
): ChangeUserCredentialsRequest {
  return {
    login: form.get('login')?.value || '',
    oldPassword: form.get('oldPassword')?.value || '',
    newPassword: form.get('newPassword')?.value || '',
    repeatNewPassword: form.get('repeatNewPassword')?.value || '',
    birthday: form.get('birthday')?.value
      ? new Date(form.get('birthday')?.value)
      : null,
    phoneNumber: form.get('phoneNumber')?.value || '',
  };
}
