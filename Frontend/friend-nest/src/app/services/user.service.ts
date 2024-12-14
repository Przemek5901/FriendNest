import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChangeUserCredentialsRequest } from '../models/request/ChangeUserCredentialsRequest';
import { Observable } from 'rxjs';
import { User } from '../models/User';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient) {}

  changeUserCredentails(
    changeUserCredentialsRequest: ChangeUserCredentialsRequest,
  ): Observable<User> {
    return this.http.post<User>(
      'http://localhost:8080/api/changeUserCredentails',
      changeUserCredentialsRequest,
    );
  }
}
