import { Post } from '../Post';
import { UserInteractions } from './UserInteractionsToPost';

export interface PostTo {
  post: Post;
  userInteractions: UserInteractions;
}
