import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Follow } from '../models/Follow';
import { FollowerTo } from '../models/response/FollowerTo';
import { FollowerToRequest } from '../models/request/FollowerToRequest';

@Injectable({
  providedIn: 'root',
})
export class FollowService {
  constructor(private http: HttpClient) {}

  switchFollow(followingId: number, followedId: number): Observable<Follow> {
    const payload = {
      followingId: followingId,
      followedId: followedId,
    };
    return this.http.post<Follow>(
      'http://localhost:8080/api/switchFollow',
      payload,
    );
  }

  isFollowedByLoggedUser(
    followingId: number,
    followedId: number,
  ): Observable<boolean> {
    const payload = {
      followingId: followingId,
      followedId: followedId,
    };
    return this.http.post<boolean>(
      'http://localhost:8080/api/isFollowedByLoggedUser',
      payload,
    );
  }

  getFollowerList(
    followerToRequest: FollowerToRequest,
  ): Observable<FollowerTo[]> {
    return this.http.post<FollowerTo[]>(
      'http://localhost:8080/api/getFollowerList',
      followerToRequest,
    );
  }
}
