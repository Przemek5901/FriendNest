export interface ProfileRequest {
  userId?: number;
  profileImageBase64?: string;
  backgroundImageBase64?: string;
  profileName?: string;
  description?: string;
  gender?: string;
}
