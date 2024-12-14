import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../utils/base-component';
import { ToastModule } from 'primeng/toast';
import { DropdownModule } from 'primeng/dropdown';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputMaskModule } from 'primeng/inputmask';
import { CalendarModule } from 'primeng/calendar';
import { User } from '../../../models/User';
import { mapFormToChangeUserCredentialsRequest } from '../../../utils/mapFormToChangeUserCredentialsRequest';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-account-settings',
  standalone: true,
  imports: [
    ToastModule,
    DropdownModule,
    FormsModule,
    InputTextModule,
    ReactiveFormsModule,
    InputMaskModule,
    CalendarModule,
  ],
  templateUrl: './account-settings.component.html',
  styleUrl: './account-settings.component.scss',
})
export class AccountSettingsComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  accountCredentials: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
  ) {
    super();
    this.accountCredentials = this.createRegisterForm();
  }

  private createRegisterForm(): FormGroup {
    return this.fb.group({
      login: ['', [Validators.required]],
      oldPassword: [''],
      newPassword: [
        '',
        [
          Validators.pattern(
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
          ),
        ],
      ],
      repeatNewPassword: [
        '',
        [
          Validators.pattern(
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
          ),
        ],
      ],
      birthday: [''],
      phoneNumber: [''],
    });
  }

  ngOnInit() {
    this.accountCredentials.patchValue({
      login: this.user.login,
      birthday: this.user.birthday ? new Date(this.user.birthday) : null,
      phoneNumber: this.user.phoneNumber,
    });
  }

  saveAccountData(): void {
    if (this.accountCredentials.invalid) {
      this.showFieldErrors(this.accountCredentials);
      return;
    }
    if (this.accountCredentials.valid) {
      const changeUserCredentialsRequest =
        mapFormToChangeUserCredentialsRequest(this.accountCredentials);
      this.userService
        .changeUserCredentails(changeUserCredentialsRequest)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondTosaveAccountData(value),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondTosaveAccountData(user: User): void {
    if (user) {
      this.authService.setUser(user);
      this.openSuccessToast('Zapisano nowe dane użytkownika');
      this.accountCredentials.controls['newPassword'].setValue('');
      this.accountCredentials.controls['oldPassword'].setValue('');
      this.accountCredentials.controls['repeatNewPassword'].setValue('');
    }
  }
}
