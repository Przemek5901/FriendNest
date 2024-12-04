import { User } from './User';
import { Interaction } from './Interaction';
import { PostTo } from './response/PostTo';
import { CommentTo } from './CommentTo';

export interface Profile {
  user?: User;
  postCount?: number;
  followersCount?: number;
  followingCount?: number;
  posts: PostTo[];
  comments: CommentTo[];
  interactions?: Interaction[];
}
