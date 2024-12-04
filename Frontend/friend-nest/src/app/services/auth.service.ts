import { Injectable } from '@angular/core';
import { AuthenticationResponse } from '../models/response/AuthenticationResponse';
import { User } from '../models/User';

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

  setUser(user: User): void {
    if (user) {
      localStorage.setItem(this.user, JSON.stringify(user));
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
