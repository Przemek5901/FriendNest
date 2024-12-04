import { Component, OnChanges, OnInit } from '@angular/core';
import { BaseComponent } from '../../../utils/base-component';
import { PostService } from '../../../services/post.service';
import { PostDetails } from '../../../models/response/PostDetails';
import { GetPostDetails } from '../../../models/request/GetPostDetails';
import { User } from '../../../models/User';
import { ActivatedRoute } from '@angular/router';
import { PostComponent } from '../main-page/post/post.component';
import { AsyncPipe, NgClass, NgIf, NgOptimizedImage } from '@angular/common';
import { Button } from 'primeng/button';
import { Location } from '@angular/common';
import { ImageModule } from 'primeng/image';
import { AddInteracionRequest } from '../../../models/request/AddInteracionRequest';
import { UserInteractions } from '../../../models/response/UserInteractionsToPost';
import { InteractionService } from '../../../services/interaction.service';
import { PostTo } from '../../../models/response/PostTo';
import { AddPostComponent } from '../main-page/add-post/add-post.component';
import { ToastModule } from 'primeng/toast';
import { mapCommentToPost } from '../../../utils/PostDTO';
import { Post } from '../../../models/Post';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [
    PostComponent,
    AsyncPipe,
    Button,
    ImageModule,
    NgOptimizedImage,
    NgClass,
    AddPostComponent,
    ToastModule,
    NgIf,
  ],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.scss',
})
export class PostDetailsComponent
  extends BaseComponent
  implements OnInit, OnChanges
{
  user: User = this.getUser();
  postDetails!: PostDetails;
  mappedComments: { postTo: PostTo; commentId: number }[] = [];

  buttonStates: {
    [key: number]: { disabled: boolean };
  } = {
    1: { disabled: false },
    2: { disabled: false },
    3: { disabled: false },
    4: { disabled: false },
    5: { disabled: false },
  };

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private location: Location,
    private interactionService: InteractionService,
  ) {
    super();
  }

  ngOnInit() {
    this.getPostDetails();
  }

  ngOnChanges(): void {
    if (this.postDetails && this.postDetails.commentTo) {
      this.mappedComments = this.postDetails.commentTo.map((comment) => ({
        postTo: mapCommentToPost(comment),
        commentId: comment.comment.commentId,
      }));
    }
  }

  getPostDetails(): void {
    const payload: GetPostDetails = {
      userId: this.user.userId,
      postId: this.route.snapshot.params['postId'],
    };
    this.postService
      .getPostsDetails(payload)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToGetPostDetails(value),
        error: (error) => this.hadleHttpError(error),
      });
  }

  private respondToGetPostDetails(postDetails: PostDetails) {
    this.postDetails = postDetails;

    if (postDetails.commentTo) {
      this.mappedComments = postDetails.commentTo.map((comment) => ({
        postTo: mapCommentToPost(comment),
        commentId: comment.comment.commentId,
      }));
    } else {
      this.mappedComments = [];
    }
  }

  goBack(): void {
    this.location.back();
  }

  toggleButton(button: number) {
    const interaction: AddInteracionRequest = {
      userId: this.user.userId,
      postId: this.postDetails.postTo.post.postId,
      commentId: null,
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
      this.openSuccessToast('Podano post dalej!');
    }
    this.postDetails.postTo.userInteractions = interaction;
    setTimeout(() => {
      buttonState.disabled = false;
    }, 1000);
  }

  deltePost(): void {
    if (this.postDetails.postTo.post.postId) {
      this.postService
        .deletePost(this.postDetails.postTo.post.postId)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (value) => this.respondToDeletePost(value),
          error: (error) => this.hadleHttpError(error),
        });
    }
  }

  private respondToDeletePost(value: Post) {
    this.openSuccessToast('Ususnięto post');
    this.location.back();
  }
}
