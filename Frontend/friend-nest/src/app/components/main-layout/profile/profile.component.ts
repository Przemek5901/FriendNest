import { Component, OnInit } from '@angular/core';
import {
  CommonModule,
  Location,
  NgIf,
  NgOptimizedImage,
} from '@angular/common';
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
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProfileService } from '../../../services/profile.service';
import { Profile } from '../../../models/Profile';
import { TabViewModule } from 'primeng/tabview';
import { FileUploadModule } from 'primeng/fileupload';
import { Dictionary } from '../../../models/Dictionary';
import { genderList } from '../../../costants/GenderList';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ProfileRequest } from '../../../models/request/ProfileRequest';
import { SpinnerService } from '../../../services/spinner.service';
import { ToastModule } from 'primeng/toast';
import { PostComponent } from '../main-page/post/post.component';
import { PostTo } from '../../../models/response/PostTo';
import { mapCommentToPost } from '../../../utils/PostDTO';
import { AuthService } from '../../../services/auth.service';
import { FollowService } from '../../../services/follow.service';
import { Follow } from '../../../models/Follow';
import { FollowerTo } from '../../../models/response/FollowerTo';
import { FollowerToRequest } from '../../../models/request/FollowerToRequest';

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
    PostComponent,
    RouterLink,
  ],
  providers: [MessageService],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent extends BaseComponent implements OnInit {
  user: User = this.getUser();
  profile: Profile = { comments: [], posts: [] };
  genders: Dictionary[] = genderList;
  profileImageUrl: string | ArrayBuffer | null = '';
  backgroundImageUrl: string | ArrayBuffer | null = '';
  isFollowed: boolean = false;
  followerToList: FollowerTo[] = [];
  visible: boolean = false;
  visibleFollowers: boolean = false;
  followerModalHeader: string = '';
  isFollowers: boolean = false;
  mappedComments: { postTo: PostTo; commentId: number }[] = [];

  editDataForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private profileService: ProfileService,
    private spinnerService: SpinnerService,
    private router: Router,
    private authService: AuthService,
    private location: Location,
    private followService: FollowService,
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
    this.spinnerService.show();
    this.getProfile();
    this.subscribeToLoginChanges();
  }

  private getIsFollowedByLoggedUser(): void {
    if (
      this?.user?.userId &&
      this.profile.user?.userId &&
      this?.user?.userId !== this.profile.user?.userId
    ) {
      this.followService
        .isFollowedByLoggedUser(this.user.userId, this.profile.user?.userId)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToIsFollowedByLoggedUser(value),
          error: (error) => this.hadleHttpError(error),
        });
    }
  }

  private respondToIsFollowedByLoggedUser(
    isFollowedByLoggedUser: boolean,
  ): void {
    this.isFollowed = isFollowedByLoggedUser;
    this.spinnerService.hide();
  }

  private subscribeToLoginChanges(): void {
    this.route.paramMap.pipe(this.autoUnsubscribe()).subscribe((params) => {
      const login = params.get('login');
      if (login) {
        this.getProfile();
      }
    });
  }

  getProfile(): void {
    this.profileService
      .getProfile(this.route.snapshot.params['login'])
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToGetProfile(value),
        error: (err) => this.hadleHttpError(err),
      });
  }

  private respondToGetProfile(profile: Profile): void {
    this.profile = profile;

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

    if (profile.comments) {
      this.mappedComments = profile.comments.map((comment) => ({
        postTo: mapCommentToPost(comment),
        commentId: comment.comment.commentId,
      }));
    } else {
      this.mappedComments = [];
    }
    this.getIsFollowedByLoggedUser();
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
    if (profile.user) {
      this.authService.setUser(profile.user);
    }
    window.location.reload();
  }

  profileFormToProfileRequest(): ProfileRequest {
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

  openPost(postTo: PostTo): void {
    this.router.navigate(['post', postTo.post.postId]);
  }

  goBack(): void {
    this.location.back();
  }

  sendMessage(user: User | undefined) {
    if (user) {
      this.router.navigate(['messages'], {
        queryParams: { userId: user.userId },
      });
    }
  }

  switchFollow(selectedUserId?: number): void {
    if (this.user?.userId && this.profile.user?.userId) {
      this.followService
        .switchFollow(
          this.user.userId,
          selectedUserId ? selectedUserId : this.profile.user?.userId,
        )
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToSwitchFollow(value),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondToSwitchFollow(follow: Follow): void {
    this.isFollowed = !!follow;
    this.getProfile();
    this.openFollowers(this.isFollowers, false);
  }

  openFollowers(isFollowers: boolean, closeModal: boolean): void {
    this.isFollowers = isFollowers;
    if (this?.user?.userId && this.profile.user?.userId) {
      const followerToRequest: FollowerToRequest = {
        searchedUserId: this.profile.user?.userId,
        loggedUserId: this?.user?.userId,
        isFollower: this.isFollowers,
      };
      this.followService
        .getFollowerList(followerToRequest)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToGetFollowerList(value, closeModal),
          error: (err) => this.hadleHttpError(err),
        });
    }
  }

  private respondToGetFollowerList(
    followerToList: FollowerTo[],
    closeModal: boolean,
  ) {
    this.followerToList = followerToList;
    this.followerModalHeader = this.isFollowers
      ? 'Obserwowane profile'
      : 'Profile obserwujące';
    if (closeModal) {
      this.visibleFollowers = !this.visibleFollowers;
    }
  }
}
