import { Post } from '../models/Post';
import { PostTo } from '../models/response/PostTo';
import { CommentTo } from '../models/CommentTo';

export function mapCommentToPost(commentTo: CommentTo): PostTo {
  const post: Post = {
    postId: commentTo.comment.post.postId,
    userId: commentTo.comment.user.userId,
    content: commentTo.comment.content,
    imageUrl: commentTo.comment.imageUrl,
    category: undefined,
    createdAt: commentTo.comment.createdAt,
    user: commentTo.comment.user,
  };
  return {
    post: post,
    userInteractions: commentTo.userInteractions,
  };
}
