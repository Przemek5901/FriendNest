import { User } from '../User';

export interface AuthenticationResponse {
  token: string;
  user: User;
}
