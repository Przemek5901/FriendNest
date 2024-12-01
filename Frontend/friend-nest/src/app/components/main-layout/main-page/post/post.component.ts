import { Component, Input } from '@angular/core';
import { NgClass, NgOptimizedImage } from '@angular/common';
import { User } from '../../../../models/User';
import { BaseComponent } from '../../../../utils/base-component';
import { Button } from 'primeng/button';
import { ImageModule } from 'primeng/image';
import { InteractionService } from '../../../../services/interaction.service';
import { AddInteracionRequest } from '../../../../models/request/AddInteracionRequest';
import { PostTo } from '../../../../models/response/PostTo';
import { UserInteractionsToPost } from '../../../../models/response/UserInteractionsToPost';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [NgOptimizedImage, Button, NgClass, ImageModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent extends BaseComponent {
  private _postTo!: PostTo;
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
    override messageService: MessageService,
  ) {
    super();
  }

  @Input() set postTo(value: PostTo) {
    this._postTo = value;
  }

  get postTo(): PostTo {
    return this._postTo;
  }

  toggleButton(button: number) {
    const interaction: AddInteracionRequest = {
      userId: this.user.userId,
      postId: this.postTo.post.postId,
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
    interaction: UserInteractionsToPost,
  ): void {
    const buttonState = this.buttonStates[button];
    buttonState.disabled = true;
    if (button === 4 && interaction.isReposted) {
      this.messageService.add({
        severity: 'success',
        summary: 'Sukces',
        detail: `Podano post dalej!`,
      });
    }
    this._postTo.userInteractionsToPost = interaction;
    setTimeout(() => {
      buttonState.disabled = false;
    }, 1000);
  }
}
