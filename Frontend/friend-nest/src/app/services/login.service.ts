import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: HttpClient) {}

  login(login: string, password: string): Observable<User> {
    const loginCredentials = {
      login: login,
      password: password,
    };
    return this.http.post<User>(
      `http://localhost:8080/authenticate`,
      loginCredentials,
    );
  }

  register(user: User): Observable<User> {
    return this.http.post(`http://localhost:8080/register`, user);
  }
}
