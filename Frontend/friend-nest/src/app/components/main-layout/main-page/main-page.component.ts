import { Component } from '@angular/core';
import { CommentComponent } from './comment/comment.component';
import { BaseComponent } from '../../../utils/base-component';
import { User } from '../../../models/User';
import { NgOptimizedImage } from '@angular/common';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { Button } from 'primeng/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SpinnerService } from '../../../services/spinner.service';
import { AddPostRequest } from '../../../models/request/AddPostRequest';
import { ImageModule } from 'primeng/image';
import { DropdownModule } from 'primeng/dropdown';
import { Dictionary } from '../../../models/Dictionary';
import { categoryList } from '../../../costants/CategoryList';
import { PostService } from '../../../services/post.service';
import { Post } from '../../../models/Post';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    CommentComponent,
    NgOptimizedImage,
    InputTextareaModule,
    PickerComponent,
    Button,
    ReactiveFormsModule,
    FormsModule,
    ImageModule,
    DropdownModule,
    ToastModule,
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent extends BaseComponent {
  user: User = this.getUser();
  emojiBar: boolean = false;
  imageUrl: string | ArrayBuffer | null = '';
  post: AddPostRequest = {
    content: '',
    category: '0',
  };
  categories: Dictionary[] = categoryList;

  constructor(
    private spinnerService: SpinnerService,
    private postService: PostService,
  ) {
    super();
  }

  addEmoji(event: any) {
    if (this.post.content.length < 150) {
      this.post.content = this.post.content + event.emoji.native;
    }
  }

  addPhoto(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      this.spinnerService.show();
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.imageUrl = reader.result;
        this.post.imageBase64 = reader.result as string;
        this.spinnerService.hide();
      };

      reader.readAsDataURL(file);
    }
  }

  deletePhoto() {
    this.imageUrl = '';
    this.post.imageBase64 = '';
  }

  addPost(): void {
    this.post.userId = this.user.userId;
    this.postService
      .addPost(this.post)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToAddPost(value),
        error: (error) => this.hadleHttpError(error),
      });
  }

  private respondToAddPost(post: Post): void {
    this.post.content = '';
    this.post.imageBase64 = '';
    this.openSuccessToast('Dodano post');
  }

  isAddButtonDisabled(): boolean {
    return (
      (!this.post.content || this.post.content.trim() === '') &&
      !this.post.imageBase64
    );
  }
}
