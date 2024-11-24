import { Injectable } from '@angular/core';
import { AuthenticationResponse } from '../models/response/AuthenticationResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenKey = 'authToken';
  private user = 'user';

  setToken(token: AuthenticationResponse): void {
    if (token.token) {
      localStorage.setItem(this.tokenKey, token.token);
    }
    if (token.user) {
      localStorage.setItem(this.user, JSON.stringify(token.user));
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  removeToken(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.user);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
