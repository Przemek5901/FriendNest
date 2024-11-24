export interface Comment {
  commentId: number;
  postId: number;
  userId: number;
  parentCommentId: number;
  content?: string;
  imageUrl?: string;
  createdAt: Date;
}
