import { PostTo } from './PostTo';
import { CommentTo } from '../CommentTo';

export interface PostDetails {
  postTo: PostTo;
  commentTo: CommentTo[];
}
