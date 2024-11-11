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
import { NgOptimizedImage } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { AvatarModule } from 'primeng/avatar';
import { DropdownModule } from 'primeng/dropdown';
import { Gender } from '../models/Gender';
import { genderList } from '../costants/GenderList';
import { MessageService } from 'primeng/api';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { FormErrorBase } from '../utils/form-error-base';
import { Router } from '@angular/router';
import { LoginService } from '../services/login.service';
import { User } from '../models/User';

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
  ],
  providers: [MessageService],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss',
})
export class LoginPageComponent extends FormErrorBase implements OnInit {
  genders: Gender[] | undefined = genderList;
  visible: boolean = false;
  registerForm: FormGroup;
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    messageService: MessageService,
    private router: Router,
    private loginService: LoginService,
  ) {
    super(messageService);
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
        .register(this.userFormToUser(this.registerForm))
        .subscribe();
    }
  }

  onLogin() {
    // if (this.loginForm.invalid) {
    //   this.showFieldErrors(this.loginForm);
    //   return;
    // }
    // if (this.loginForm.valid) {
    //   this.loginService
    //     .login(
    //       this.loginForm.controls['login'].value,
    //       this.loginForm.controls['password'].value,
    //     )
    //     .subscribe();
    // }
    {
      this.router.navigate(['main-page']);
    }
  }

  private userFormToUser(userForm: FormGroup): User {
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
