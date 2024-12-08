import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatTo } from '../models/response/ChatTo';
import { Message } from '../models/Message';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  constructor(private http: HttpClient) {}

  getChats(userId: number): Observable<ChatTo[]> {
    const payload = {
      userId: userId,
    };
    return this.http.post<ChatTo[]>(
      'http://localhost:8080/api/getChats',
      payload,
    );
  }

  getChatMessages(chatId: number): Observable<Message[]> {
    const payload = {
      chatId: chatId,
    };
    return this.http.post<Message[]>(
      'http://localhost:8080/api/getChatMessages',
      payload,
    );
  }

  sendMessage(message: Message): Observable<Message[]> {
    return this.http.post<Message[]>(
      'http://localhost:8080/api/sendMessage',
      message,
    );
  }
}
