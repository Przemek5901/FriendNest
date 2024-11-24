export interface Message {
  messageId: number;
  chatId: number;
  senderId: number;
  content: string;
  imageUrl: string;
  createdAt: Date;
}
