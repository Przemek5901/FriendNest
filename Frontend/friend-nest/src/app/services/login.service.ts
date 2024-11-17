import { Injectable } from '@angular/core';
import { delay, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { RegisterRequest } from '../models/RegisterRequest';
import { AuthenticationResponse } from '../models/AuthenticationResponse';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: HttpClient) {}

  login(login: string, password: string): Observable<AuthenticationResponse> {
    const loginCredentials = {
      login: login,
      password: password,
    };
    return this.http
      .post<AuthenticationResponse>(
        `http://localhost:8080/api/auth/authenticate`,
        loginCredentials,
      )
      .pipe(delay(1000));
  }

  register(user: RegisterRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(
      `http://localhost:8080/api/auth/register`,
      user,
    );
  }
}
