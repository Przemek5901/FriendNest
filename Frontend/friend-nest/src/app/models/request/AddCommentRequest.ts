export interface AddCommentRequest {
  postId: number;
  userId?: number;
  content: string;
  imageBase64?: string;
}
