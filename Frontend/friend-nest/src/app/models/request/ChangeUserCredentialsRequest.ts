export interface ChangeUserCredentialsRequest {
  login: string;
  oldPassword: string;
  newPassword: string;
  repeatNewPassword: string;
  birthday: Date | null;
  phoneNumber: string;
}
