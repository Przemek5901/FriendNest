import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Profile } from '../models/Profile';
import { Observable } from 'rxjs';
import { ProfileRequest } from '../models/request/ProfileRequest';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  constructor(private http: HttpClient) {}

  getProfile(login: string): Observable<Profile> {
    const payload = {
      login: login,
    };
    return this.http.post<Profile>(
      'http://localhost:8080/api/getProfile',
      payload,
    );
  }

  editProfile(profile: ProfileRequest): Observable<Profile> {
    return this.http.post<Profile>(
      'http://localhost:8080/api/editProfile',
      profile,
    );
  }
}
