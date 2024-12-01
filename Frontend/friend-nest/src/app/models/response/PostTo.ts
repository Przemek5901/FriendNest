import { Post } from '../Post';
import { UserInteractionsToPost } from './UserInteractionsToPost';

export interface PostTo {
  post: Post;
  userInteractionsToPost: UserInteractionsToPost;
}
