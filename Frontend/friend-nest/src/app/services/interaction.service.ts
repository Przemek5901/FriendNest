import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AddInteracionRequest } from '../models/request/AddInteracionRequest';
import { Observable } from 'rxjs';
import { UserInteractionsToPost } from '../models/response/UserInteractionsToPost';

@Injectable({
  providedIn: 'root',
})
export class InteractionService {
  constructor(private http: HttpClient) {}

  addInteraction(
    request: AddInteracionRequest,
  ): Observable<UserInteractionsToPost> {
    return this.http.post<UserInteractionsToPost>(
      'http://localhost:8080/api/addInteraction',
      request,
    );
  }
}
