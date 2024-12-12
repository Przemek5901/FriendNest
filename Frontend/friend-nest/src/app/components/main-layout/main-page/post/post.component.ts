import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  NgClass,
  NgOptimizedImage,
  NgStyle,
  NgTemplateOutlet,
} from '@angular/common';
import { User } from '../../../../models/User';
import { BaseComponent } from '../../../../utils/base-component';
import { Button } from 'primeng/button';
import { ImageModule } from 'primeng/image';
import { InteractionService } from '../../../../services/interaction.service';
import { AddInteracionRequest } from '../../../../models/request/AddInteracionRequest';
import { PostTo } from '../../../../models/response/PostTo';
import { UserInteractions } from '../../../../models/response/UserInteractionsToPost';
import { Router, RouterLink } from '@angular/router';
import { PostService } from '../../../../services/post.service';
import { CommentService } from '../../../../services/comment.service';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { PaginatorModule } from 'primeng/paginator';
import { ReactiveFormsModule } from '@angular/forms';
import { AddPostComponent } from '../add-post/add-post.component';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { Post } from '../../../../models/Post';
import { QuotePostRequest } from '../../../../models/request/QuotePostRequest';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    NgOptimizedImage,
    Button,
    NgClass,
    ImageModule,
    RouterLink,
    DialogModule,
    DropdownModule,
    InputTextModule,
    PaginatorModule,
    ReactiveFormsModule,
    AddPostComponent,
    NgTemplateOutlet,
    InputTextareaModule,
    NgStyle,
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent extends BaseComponent {
  private _postTo!: PostTo;
  private _commentId!: number;
  quoteContent: string = '';
  @Input() isNested: boolean = false;
  buttonStates: {
    [key: number]: { disabled: boolean };
  } = {
    1: { disabled: false },
    2: { disabled: false },
    3: { disabled: false },
    4: { disabled: false },
    5: { disabled: false },
  };

  user: User = this.getUser();
  visible: boolean = false;

  constructor(
    private interactionService: InteractionService,
    private router: Router,
    private postService: PostService,
    private commentService: CommentService,
  ) {
    super();
  }

  @Input() set commentId(value: number) {
    this._commentId = value;
  }

  get commentId(): number {
    return this._commentId;
  }

  @Input() set postTo(value: PostTo) {
    this._postTo = value;
  }

  get postTo(): PostTo {
    return this._postTo;
  }

  @Output() deleted = new EventEmitter<void>();

  toggleButton(button: number, event: MouseEvent) {
    event.stopPropagation();
    const interaction: AddInteracionRequest = {
      userId: this.user.userId,
      postId: this.postTo.post.postId,
      commentId: this._commentId ? this._commentId : null,
      reactionType: button,
    };

    this.interactionService
      .addInteraction(interaction)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToAddReaction(button, value),
        error: (error) => this.hadleHttpError(error),
      });
  }

  private respondToAddReaction(
    button: number,
    interaction: UserInteractions,
  ): void {
    const buttonState = this.buttonStates[button];
    buttonState.disabled = true;
    if (button === 4 && interaction.isReposted) {
      this.openSuccessToast(`Podano post dalej!`);
    }
    this.postTo.userInteractions = interaction;
    setTimeout(() => {
      buttonState.disabled = false;
    }, 1000);
  }

  deltePostComment(event: MouseEvent) {
    event.stopPropagation();
    if (this.commentId) {
      this.commentService
        .deleteComment(this.commentId)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToDeletePostComment(value),
          error: (error) => this.hadleHttpError(error),
        });
    } else {
      if (this.postTo.post.postId) {
        this.postService
          .deletePost(this.postTo.post.postId)
          .pipe(this.autoUnsubscribe())
          .subscribe({
            next: (value) => this.respondToDeletePostComment(value),
            error: (error) => this.hadleHttpError(error),
          });
      }
    }
  }

  private respondToDeletePostComment(response: any) {
    if ('commentId' in response) {
      this.openSuccessToast('Usunięto komentarz');
      this.deleted.emit();
    } else {
      this.openSuccessToast('Usunięto post');
      this.deleted.emit();
    }
  }

  openQuoteModal(event: MouseEvent) {
    event.stopPropagation();
    this.visible = !this.visible;
  }

  quotePost(): void {
    if (
      this.user?.userId &&
      this.postTo?.post?.postId &&
      this.quoteContent?.length > 0
    ) {
      const quotePostRequest: QuotePostRequest = {
        postId: this.postTo.post.postId,
        userId: this.user.userId,
        content: this.quoteContent,
      };

      this.postService
        .quotePost(quotePostRequest)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToQuotePost(value),
          error: (error) => this.hadleHttpError(error),
        });
    }
  }

  private respondToQuotePost(value: Post) {
    this.quoteContent = '';
    this.visible = false;
    this.openSuccessToast('Zacytowano post!');
  }

  openPost(postTo: PostTo): void {
    let route: any[];

    if (postTo.isReposted) {
      route = ['post', postTo.post.postId, 'repost', postTo.reposter?.userId];
    } else if (postTo.isQuoted) {
      route = [
        'post',
        postTo.post.postId,
        'quote',
        postTo.quotedPost?.post.postId,
      ];
    } else {
      route = ['post', postTo.post.postId];
    }

    this.router.navigate(route);
  }
}
