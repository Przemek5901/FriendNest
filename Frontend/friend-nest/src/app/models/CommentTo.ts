import { UserInteractions } from './response/UserInteractionsToPost';
import { Comment } from './Comment';

export interface CommentTo {
  comment: Comment;
  userInteractions: UserInteractions;
}
