import { User } from './User';

export interface Post {
  postId: number;
  userId?: number;
  content?: string;
  imageUrl?: string;
  category?: string;
  createdAt?: Date;
  user: User;
}
