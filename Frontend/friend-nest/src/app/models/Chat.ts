import { User } from './User';

export interface Chat {
  chatId?: number;
  user1?: User;
  user2?: User;
  createdAt?: Date;
}
