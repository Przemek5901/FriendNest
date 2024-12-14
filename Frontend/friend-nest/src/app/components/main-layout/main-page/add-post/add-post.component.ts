import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { Button } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { BaseComponent } from '../../../../utils/base-component';
import { User } from '../../../../models/User';
import { AddPostRequest } from '../../../../models/request/AddPostRequest';
import { Dictionary } from '../../../../models/Dictionary';
import { categoryList } from '../../../../costants/CategoryList';
import { SpinnerService } from '../../../../services/spinner.service';
import { PostService } from '../../../../services/post.service';
import { CommentComponent } from '../comment/comment.component';
import { NgOptimizedImage } from '@angular/common';
import { InputTextarea, InputTextareaModule } from 'primeng/inputtextarea';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ImageModule } from 'primeng/image';
import { ToastModule } from 'primeng/toast';
import { AddCommentRequest } from '../../../../models/request/AddCommentRequest';
import { CommentService } from '../../../../services/comment.service';
import { ActivatedRoute } from '@angular/router';
import {HighlightHashtagsDirective} from '../../../../utils/highlight-hashtags.directive';

@Component({
  selector: 'app-add-post',
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
    HighlightHashtagsDirective,
  ],
  templateUrl: './add-post.component.html',
  styleUrl: './add-post.component.scss',
})
export class AddPostComponent extends BaseComponent implements AfterViewInit {
  @ViewChild('textArea') textarea!: ElementRef;
  user: User = this.getUser();
  emojiBar: boolean = false;
  imageUrl: string | ArrayBuffer | null = '';
  post: AddPostRequest = {
    content: '',
    category: '0',
  };
  categories: Dictionary[] = categoryList;
  private _isComment: boolean = false;
  private _postId!: number;

  constructor(
    private spinnerService: SpinnerService,
    private postService: PostService,
    private commentService: CommentService,
    private route: ActivatedRoute,
  ) {
    super();
  }

  ngAfterViewInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params['focus'] === 'true') {
        this.setFocusOnTextarea();
      }
    });
  }

  setFocusOnTextarea(): void {
    if (this.textarea?.nativeElement) {
      setTimeout(() => {
        this.textarea.nativeElement.focus();
      }, 100);
    }
  }

  @Input() set postId(value: number) {
    this._postId = value;
  }

  get postId(): number {
    return <number>this._postId;
  }

  @Input() set isComment(value: boolean) {
    this._isComment = value;
  }

  get isComment(): boolean {
    return this._isComment;
  }

  @Output() commentAdded = new EventEmitter<void>();

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
        next: (value) => this.respondToAddPostComment(true),
        error: (error) => this.hadleHttpError(error),
      });
  }

  addComment(): void {
    const addCommentRequest: AddCommentRequest = {
      postId: this.postId,
      userId: this.user.userId,
      content: this.post.content,
      imageBase64: this.post.imageBase64,
    };

    this.commentService
      .addComment(addCommentRequest)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToAddPostComment(false),
        error: (error) => this.hadleHttpError(error),
      });
  }

  private respondToAddPostComment(isPost: boolean): void {
    this.post.content = '';
    this.post.imageBase64 = '';
    this.openSuccessToast(isPost ? 'Dodano post' : 'Dodano komentarz');
    if (!isPost) {
      this.commentAdded.emit();
    }
  }

  isAddButtonDisabled(): boolean {
    return (
      (!this.post.content || this.post.content.trim() === '') &&
      !this.post.imageBase64
    );
  }
}
