import { User } from './User';
import { Chat } from './Chat';

export interface Message {
  messageId?: number;
  chat: Chat;
  sender?: User;
  content?: string;
  imageUrl?: string;
  imageBase64?: string;
  createdAt?: Date;
}
