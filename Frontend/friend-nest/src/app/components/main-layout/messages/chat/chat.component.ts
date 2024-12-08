import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BaseComponent } from '../../../../utils/base-component';
import { User } from '../../../../models/User';
import { ChatTo } from '../../../../models/response/ChatTo';
import { NgClass } from '@angular/common';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { SearchService } from '../../../../services/search.service';
import { MessageService } from '../../../../services/message.service';
import { FormsModule } from '@angular/forms';
import { Chat } from '../../../../models/Chat';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [NgClass, AutoCompleteModule, FormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss',
})
export class ChatComponent extends BaseComponent {
  loggedUser: User = this.getUser();
  filteredGroups: any[] = [];
  lastQuery: string = '';
  private _chats: ChatTo[] = [];
  private _selectedChat!: ChatTo;
  private _user!: any;

  constructor(
    private searchService: SearchService,
    private message: MessageService,
  ) {
    super();
  }

  @Input() set user(value: any) {
    this._user = value;
    this.userChange.emit(this._user);
  }

  get user(): any {
    return this._user;
  }

  @Output() userChange = new EventEmitter<any>();

  @Input() set chats(value: ChatTo[]) {
    this._chats = value;
  }

  @Input() set selectedChat(value: ChatTo) {
    this._selectedChat = value;
    this.selectedChatChange.emit(this._selectedChat);
  }

  @Output() selectedChatChange = new EventEmitter<ChatTo>();

  get chats(): ChatTo[] {
    return this._chats;
  }

  get selectedChat(): ChatTo {
    return this._selectedChat;
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

  onInputChange(event: any) {
    const query = event.target.value.trim();

    if (
      query.length > 0 &&
      query !== this.lastQuery &&
      this.loggedUser?.userId
    ) {
      this.lastQuery = query;
      this.searchService
        .searchUsers(query, this.loggedUser.userId)
        .pipe(this.autoUnsubscribe())
        .subscribe((response) => {
          this.filteredGroups = this.transformToGroups(response);
        });
    } else if (query.length === 0) {
      this.filteredGroups = []; //
    }
  }

  transformToGroups(data: User[]): any[] {
    const groups = [];

    if (data && data.length > 0) {
      groups.push({
        label: 'Użytkowicy',
        value: 'Users',
        items: data.map((user: User) => ({
          label: user.profileName,
          value: user,
        })),
      });
    }

    return groups;
  }

  selectUser(user?: User): void {
    const selectedUser = user || this.user.value;

    const existingChat = this.chats.find(
      (chat: ChatTo) =>
        this.getSecondChatter(chat)?.userId === selectedUser.userId,
    );

    if (existingChat) {
      this.selectChat(existingChat);
      if (user) {
        this.user.value = user;
      }
      return;
    }

    const newChat: Chat = {
      user1: this.loggedUser,
      user2: selectedUser,
    };

    const newChatTo: ChatTo = {
      chat: newChat,
      lastMessage: undefined,
    };

    this.selectChat(newChatTo);
  }

  selectChat(chat: ChatTo): void {
    this.selectedChat = chat;
  }

  getLastMessage(chat: ChatTo): string {
    if (chat.lastMessage?.content) {
      if (chat.lastMessage?.sender?.userId === this.loggedUser.userId) {
        return 'ty: ' + chat.lastMessage?.content?.substring(0, 20);
      } else {
        return <string>chat.lastMessage?.content?.substring(0, 20);
      }
    } else {
      return 'Zdjęcie';
    }
  }
}
