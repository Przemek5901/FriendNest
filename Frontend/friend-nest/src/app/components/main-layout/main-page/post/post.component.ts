import { Component, Input } from '@angular/core';
import { NgClass, NgOptimizedImage } from '@angular/common';
import { User } from '../../../../models/User';
import { BaseComponent } from '../../../../utils/base-component';
import { Button } from 'primeng/button';
import { ImageModule } from 'primeng/image';
import { InteractionService } from '../../../../services/interaction.service';
import { AddInteracionRequest } from '../../../../models/request/AddInteracionRequest';
import { PostTo } from '../../../../models/response/PostTo';
import { UserInteractions } from '../../../../models/response/UserInteractionsToPost';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [NgOptimizedImage, Button, NgClass, ImageModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent extends BaseComponent {
  private _postTo!: PostTo;
  private _commentId!: number;
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

  constructor(
    private interactionService: InteractionService,
    private router: Router,
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

  openPost(): void {
    this.router.navigate(['post', this.postTo.post.postId]);
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
}
