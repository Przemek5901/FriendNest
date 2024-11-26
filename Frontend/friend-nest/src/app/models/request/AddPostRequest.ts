export interface AddPostRequest {
  userId?: number;
  content: string;
  imageBase64?: string;
  category?: string;
}
