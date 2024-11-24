import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgOptimizedImage } from '@angular/common';
import { BaseComponent } from '../../../utils/base-component';
import { User } from '../../../models/User';
import { MessageService } from 'primeng/api';
import { Button, ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { SpinnerComponent } from '../../../utils/spinner/spinner.component';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../../../services/profile.service';
import { Profile } from '../../../models/Profile';
import { TabViewModule } from 'primeng/tabview';
import { FileUploadModule } from 'primeng/fileupload';
import { Gender } from '../../../models/Gender';
import { genderList } from '../../../costants/GenderList';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ProfileRequest } from '../../../models/request/ProfileRequest';
import { SpinnerService } from '../../../services/spinner.service';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgOptimizedImage,
    Button,
    CommonModule,
    DialogModule,
    ReactiveFormsModule,
    DropdownModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
    NgIf,
    SpinnerComponent,
    TabViewModule,
    FileUploadModule,
    InputTextareaModule,
    ToastModule,
  ],
  providers: [MessageService],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  profile: Profile = {};
  genders: Gender[] = genderList;
  profileImageUrl: string | ArrayBuffer | null = '';
  backgroundImageUrl: string | ArrayBuffer | null = '';

  visible: boolean = false;

  editDataForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private spinnerService: SpinnerService,
  ) {
    super();
    this.editDataForm = this.fb.group({
      profileName: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      description: [''],
      profileImageBase64: [''],
      backgroundBase64: [''],
    });
  }

  ngOnInit() {
    this.getProfile();
  }

  private getProfile() {
    this.profileService
      .getProfile(this.route.snapshot.params['login'])
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToGetProfile(value),
        error: (err) => this.hadleHttpError(err),
      });
  }

  private respondToGetProfile(profile: Profile) {
    this.profile = profile;
    console.log(this.profile);

    this.editDataForm.controls['profileName'].setValue(
      this.profile.user?.profileName,
    );
    this.editDataForm.controls['description'].setValue(
      this.profile.user?.profileDesc,
    );
    this.editDataForm.controls['gender'].setValue(this.profile.user?.gender);
    this.profileImageUrl =
      profile.user?.profileImageUrl ??
      'http://localhost:8080/defaults/profile.png';
    this.backgroundImageUrl =
      profile.user?.backgroundImageUrl ??
      'http://localhost:8080/defaults/background.png';
  }

  changeProfilePicture(event: any) {
    this.handleFileChange(event, 'profileImageBase64', 'profileImageUrl');
  }

  changeBackground(event: any) {
    this.handleFileChange(event, 'backgroundBase64', 'backgroundImageUrl');
  }

  handleFileChange(
    event: any,
    formControlName: string,
    imageProperty: 'profileImageUrl' | 'backgroundImageUrl',
  ) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      this.spinnerService.show();
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this[imageProperty] = reader.result;
        this.editDataForm.controls[formControlName].setValue(
          reader.result as string,
        );
        this.spinnerService.hide();
      };

      reader.readAsDataURL(file);
    }
  }

  editProfile(): void {
    if (this.editDataForm.invalid) {
      this.showFieldErrors(this.editDataForm);
      return;
    }
    if (this.editDataForm.valid) {
      this.profileService
        .editProfile(this.profileFormToProfileRequest())
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToEditProfile(value),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondToEditProfile(profile: Profile): void {
    this.profile = profile;
    window.location.reload();
  }

  profileFormToProfileRequest(): ProfileRequest {
    console.log(this.profile);
    return {
      userId: this.profile.user?.userId,
      profileImageBase64:
        this.editDataForm.controls['profileImageBase64'].value,
      backgroundImageBase64:
        this.editDataForm.controls['backgroundBase64'].value,
      profileName: this.editDataForm.controls['profileName'].value,
      description: this.editDataForm.controls['description'].value,
      gender: this.editDataForm.controls['gender'].value,
    };
  }

  editData(): void {
    this.visible = true;
  }
}
