import { Post } from '../Post';
import { UserInteractions } from './UserInteractionsToPost';
import { User } from '../User';

export interface PostTo {
  quotedPost?: PostTo;
  reposter?: User;
  post: Post;
  isReposted?: boolean;
  isQuoted?: boolean;
  userInteractions: UserInteractions;
}
