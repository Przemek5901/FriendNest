import { User } from './User';
import { Post } from './Post';

export interface Comment {
  commentId: number;
  parentCommentId: number;
  content?: string;
  imageUrl?: string;
  createdAt: Date;
  user: User;
  post: Post;
}
