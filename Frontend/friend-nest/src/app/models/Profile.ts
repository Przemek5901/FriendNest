import { User } from './User';
import { Post } from './Post';
import { Interaction } from './Interaction';

export interface Profile {
  user?: User;
  postCount?: number;
  followersCount?: number;
  followingCount?: number;
  posts?: Post[];
  comments?: Comment[];
  interactions?: Interaction[];
}
