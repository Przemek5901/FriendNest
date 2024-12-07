import { Component, OnInit } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { NgIf, NgOptimizedImage } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { AvatarModule } from 'primeng/avatar';
import { DropdownModule } from 'primeng/dropdown';
import { Dictionary } from '../../../models/Dictionary';
import { genderList } from '../../../costants/GenderList';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { LoginService } from '../../../services/login.service';
import { SpinnerComponent } from '../../../utils/spinner/spinner.component';
import { AuthService } from '../../../services/auth.service';
import { BaseComponent } from '../../../utils/base-component';
import { RegisterRequest } from '../../../models/request/RegisterRequest';
import { Router } from '@angular/router';
import { AuthenticationResponse } from '../../../models/response/AuthenticationResponse';
import { MessageService } from 'primeng/api';
import { InputTextareaModule } from 'primeng/inputtextarea';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [
    InputTextModule,
    FormsModule,
    ButtonModule,
    NgOptimizedImage,
    DialogModule,
    AvatarModule,
    DropdownModule,
    ReactiveFormsModule,
    ToastModule,
    RippleModule,
    NgIf,
    SpinnerComponent,
    InputTextareaModule,
  ],
  providers: [MessageService],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent extends BaseComponent implements OnInit {
  genders: Dictionary[] | undefined = genderList;
  visible: boolean = false;
  registerForm: FormGroup;
  loginForm: FormGroup;

  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private loginService: LoginService,
    private authService: AuthService,
  ) {
    super();
    this.registerForm = this.fb.group({
      userName: ['', [Validators.required]],
      login: ['', [Validators.required]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
          ),
        ],
      ],
      gender: ['', [Validators.required]],
    });

    this.loginForm = this.fb.group({
      login: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit() {}

  onRegister() {
    if (this.registerForm.invalid) {
      this.showFieldErrors(this.registerForm);
      return;
    }
    if (this.registerForm.valid) {
      this.loginService
        .register(this.userFormToRegisterRequest(this.registerForm))
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToAuthenticate(value),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.showFieldErrors(this.loginForm);
      return;
    }
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.loginService
        .login(
          this.loginForm.controls['login'].value.trim(),
          this.loginForm.controls['password'].value,
        )
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToAuthenticate(value),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondToAuthenticate(value: AuthenticationResponse) {
    this.router.navigate(['main-page']);
    this.authService.setToken(value);
  }

  private userFormToRegisterRequest(userForm: FormGroup): RegisterRequest {
    return {
      login: userForm.controls['login'].value,
      password: userForm.controls['password'].value,
      gender: userForm.controls['gender'].value.code,
      userName: userForm.controls['userName'].value,
    };
  }

  showDialog(): void {
    this.visible = true;
  }
}
