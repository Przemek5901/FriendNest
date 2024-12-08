import { Component, OnInit, ViewChild } from '@angular/core';
import { Button } from 'primeng/button';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { PrimeTemplate } from 'primeng/api';
import { BaseComponent } from '../../../utils/base-component';
import { User } from '../../../models/User';
import { FormsModule } from '@angular/forms';
import { MessageService } from '../../../services/message.service';
import { ChatTo } from '../../../models/response/ChatTo';
import { Message } from '../../../models/Message';
import { DatePipe, NgClass, NgOptimizedImage, NgStyle } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { PickerComponent } from '@ctrl/ngx-emoji-mart';
import { ChatComponent } from './chat/chat.component';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ImageModule } from 'primeng/image';
import { ProfileService } from '../../../services/profile.service';

@Component({
  selector: 'app-messages',
  standalone: true,
  imports: [
    Button,
    AutoCompleteModule,
    PrimeTemplate,
    FormsModule,
    NgClass,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    PickerComponent,
    NgStyle,
    NgOptimizedImage,
    ChatComponent,
    DatePipe,
    RouterLink,
    ImageModule,
  ],
  templateUrl: './messages.component.html',
  styleUrl: './messages.component.scss',
})
export class MessagesComponent extends BaseComponent implements OnInit {
  @ViewChild('chatComponent') chatComponent!: ChatComponent;
  loggedUser: User = this.getUser();
  chats: ChatTo[] = [];
  selectedChat!: ChatTo;
  messages: Message[] = [];
  user!: any;
  messageToSend: string = '';
  emojiBar: boolean = false;
  imageUrl: string | ArrayBuffer | null = '';
  imageBase64: string = '';

  constructor(
    private message: MessageService,
    private route: ActivatedRoute,
    private profileService: ProfileService,
  ) {
    super();
  }

  ngOnInit() {
    this.getChats(false);
  }

  private getUserFromParam(): void {
    this.route.queryParamMap
      .pipe(this.autoUnsubscribe())
      .subscribe((params) => {
        const userId = params.get('userId');
        if (userId) {
          this.profileService
            .getUser(+userId)
            .pipe(this.autoUnsubscribe())
            .subscribe((user: User) => {
              this.chatComponent.selectUser(user);
            });
        }
      });
  }

  private getChats(isNewChat: boolean): void {
    if (this.loggedUser.userId) {
      this.message
        .getChats(this.loggedUser.userId)
        .pipe(this.autoUnsubscribe())
        .subscribe({
          next: (res) => this.respondToGetChats(res, isNewChat),
          error: (error) => this.hadleHttpError(error),
        });
    }
  }

  private respondToGetChats(chats: ChatTo[], isNewChat: boolean) {
    this.chats = chats;
    if (chats.length > 0) {
      if (isNewChat) {
        this.selectedChat = chats[chats.length - 1];
      } else {
        this.selectedChat = chats[0];
      }
      if (this.selectedChat?.chat?.chatId) {
        this.getMessages(this.selectedChat?.chat?.chatId);
      }
    }
    this.getUserFromParam();
  }

  getMessages(chatId: number): void {
    this.message
      .getChatMessages(chatId)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (res) => this.respondToGetMessages(res),
        error: (error) => this.hadleHttpError(error),
      });
  }

  respondToGetMessages(messages: Message[]): void {
    this.messages = messages;
  }

  addPhoto(event: Event) {
    const input = event.target as HTMLInputElement;

    if (input.files && input.files[0]) {
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.imageUrl = reader.result;
        this.imageBase64 = reader.result as string;
      };

      reader.readAsDataURL(file);
    }
  }

  addEmoji(event: any) {
    if (this.messageToSend.length < 250) {
      this.messageToSend = this.messageToSend + event.emoji.native;
    }
  }

  getSecondChatter(chat: ChatTo): User | null {
    if (
      chat.chat &&
      chat.chat.user1 &&
      chat.chat.user2 &&
      chat.chat.user1.userId &&
      chat.chat.user2.userId
    ) {
      if (chat.chat.user1.userId === this.loggedUser.userId) {
        return chat.chat.user2;
      } else {
        return chat.chat.user1;
      }
    }
    return null;
  }

  deleteImage(): void {
    this.imageUrl = '';
    this.imageBase64 = '';
  }

  onSelectedChatChange(newChat: ChatTo): void {
    if (newChat?.chat?.chatId) {
      this.getMessages(newChat.chat.chatId);
    } else {
      this.messages = [];
    }
  }

  sendMessage(): void {
    const message: Message = {
      chat: this.selectedChat.chat,
      sender: this.loggedUser,
      content: this.messageToSend,
      imageBase64: this.imageBase64,
    };

    this.message
      .sendMessage(message)
      .pipe(this.autoUnsubscribe())
      .subscribe({
        next: (value) => this.respondToSendMessage(value),
        error: (error) => this.hadleHttpError(error),
      });
  }

  respondToSendMessage(messages: Message[]): void {
    messages.forEach((message) => {
      let chatFound = false;
      this.chats.forEach((chat) => {
        if (chat.chat?.chatId === message.chat?.chatId) {
          chat.lastMessage = message;
          chatFound = true;
        }
      });
      if (!chatFound) {
        this.getChats(true);
      }
    });
    this.messages = messages;
    this.messageToSend = '';
    this.imageBase64 = '';
    this.imageUrl = '';
  }
}
