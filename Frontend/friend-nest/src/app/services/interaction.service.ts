import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AddInteracionRequest } from '../models/request/AddInteracionRequest';
import { Observable } from 'rxjs';
import { UserInteractions } from '../models/response/UserInteractionsToPost';

@Injectable({
  providedIn: 'root',
})
export class InteractionService {
  constructor(private http: HttpClient) {}

  addInteraction(request: AddInteracionRequest): Observable<UserInteractions> {
    return this.http.post<UserInteractions>(
      'http://localhost:8080/api/addInteraction',
      request,
    );
  }
}
