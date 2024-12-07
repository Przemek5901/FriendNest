import { User } from '../User';
import { PostTo } from './PostTo';

export interface SearchResults {
  users: User[];
  posts: PostTo[];
}
