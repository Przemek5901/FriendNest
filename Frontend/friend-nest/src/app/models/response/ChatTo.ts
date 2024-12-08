import { Message } from '../Message';
import { Chat } from '../Chat';

export interface ChatTo {
  chat: Chat;
  lastMessage?: Message;
}
